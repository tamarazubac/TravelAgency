import { MaterialModule } from 'src/app/common/material/material.module';
import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Arrangement } from 'src/app/models/arrangement';
import { Rate } from 'src/app/models/rate';
import { ArrangementService } from 'src/app/services/arrangement/arrangement.service';
import { RateService } from 'src/app/services/rate/rate.service';
import { RateCardComponent } from '../rate-card/rate-card.component';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { Role } from 'src/app/models/role';
import { MatDialog } from '@angular/material/dialog';
import { AddRateDialogComponent } from '../add-rate-dialog/add-rate-dialog.component';
import { User } from 'src/app/models/user';
import { JwtHelperService } from '@auth0/angular-jwt';
import { UserService } from 'src/app/services/user/user.service';
import { CreateReservationDialogComponent } from '../create-reservation-dialog/create-reservation-dialog.component';
import { DestinationService } from 'src/app/services/destination/destination.service';
import { ReservationService } from 'src/app/services/reservation/reservation.service';


@Component({
  selector: 'app-arrangement-details',
  templateUrl: './arrangement-details.component.html',
  styleUrls: ['./arrangement-details.component.css'],
  standalone: true,
  imports: [CommonModule, MaterialModule,RateCardComponent]
})
export class ArrangementDetailsComponent implements OnInit {
  arrangement: Arrangement;
  currentImageIndex = 0;
  rates:Rate[]=[];
  user:User;

  averageRating: number = 0;
  stars: number[] = [1, 2, 3, 4, 5];

  rolesObjects:Role[]=[];
  roles:string[]=[]

  fullStars: number = 0;
  partiallyFilledStar: boolean = false;
  unfilledStars: number = 0;

  images: string[] = [];

  isCustomerWithPastReservation: boolean = false;

  constructor(
    private arrangementService: ArrangementService,
    private rateService:RateService,
    private authService:AuthenticationService,
    private userService:UserService,
    private route: ActivatedRoute,
    private dialog:MatDialog,
    private destinationService:DestinationService,
    private reservationService:ReservationService

  ) { }

  ngOnInit(): void {
    const id = +this.route.snapshot.paramMap.get('id')!;

    this.arrangementService.findById(id).subscribe({
      next: (data: Arrangement) => {
        this.arrangement = data;
        console.log(this.arrangement);

        this.loadRates();
        this.getUser();
        this.loadImages();
      },
      error: () => {
        console.log('Error fetching arrangement details');
      }
    });

    this.authService.userState.subscribe((result) => {  //checking users roles
      this.roles=[]
      this.rolesObjects=[]
      if(result &&
        result != null){
        this.rolesObjects=result.roles;
        this.roles = this.rolesObjects.map(role => role.roleName);

        if(this.roles.includes('UNAUTHENTICATED')){
          this.roles = this.roles.filter(role => role !== 'UNAUTHENTICATED');
        }

      }else{
        this.rolesObjects.push({ roleName:"UNAUTHENTICATED" });
        this.roles=[];
        this.roles = this.rolesObjects.map(role => role.roleName);
      }

    })

  }

  prevImage(): void {
    this.currentImageIndex = (this.currentImageIndex - 1 + this.images.length) % this.images.length;
  }

  nextImage(): void {
    this.currentImageIndex = (this.currentImageIndex + 1) % this.images.length;
  }

  loadRates(): void {
    if(this.arrangement.id)
      this.rateService.getRatesByArrangementId(this.arrangement.id).subscribe({
        next: (data: Rate[]) => {
          this.rates = data;
          console.log('Rates loaded:', this.rates);
          this.calculateAverageRating();
        },
        error: (err) => {
          console.error('Error loading rates:', err);
        }
      });
  }

  getStarClass(index: number): string {
    if (index < Math.floor(this.averageRating)) {
      return 'filled';
    } else if (index === Math.floor(this.averageRating) && (this.averageRating % 1 > 0)) {
      return 'partially-filled';
    } else {
      return 'unfilled';
    }
  }

  calculateStarRatings(): void {
    if (this.averageRating > 0) {
      this.fullStars = Math.floor(this.averageRating);
      this.partiallyFilledStar = this.averageRating % 1 > 0;
      this.unfilledStars = 5 - this.fullStars - (this.partiallyFilledStar ? 1 : 0);
    } else {
      this.fullStars = 0;
      this.partiallyFilledStar = false;
      this.unfilledStars = 5;
    }
  }

  calculateAverageRating(): void {
    if (this.rates.length === 0) {
      this.averageRating = 0;
      return;
    }

    const total = this.rates.reduce((sum, rate) => sum + rate.rateNum, 0);
    this.averageRating = total / this.rates.length;
  }

  openAddRateDialog(){

    const dialogRef = this.dialog.open(AddRateDialogComponent, {
      width: '800px',
      data: { arrangement: this.arrangement,
              user:this.user
       }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadRates(); // reloading rates
      }
    });

  }


  getUser(){

    const accessToken: any = localStorage.getItem('user');
    const helper = new JwtHelperService();
    const decodedToken = helper.decodeToken(accessToken);

    if (decodedToken) {
      this.userService.getByUsername(decodedToken.sub).subscribe(
        (user: User) => {
          if (user && user.id) {
            this.user=user
            this.checkCustomerPastReservation(user.id);
          }
        },
        (error) => {
          console.error('Error fetching user details:', error);
        }
      );
    } else {
      console.error('Error decoding JWT token');
    }

  }


  book(){

    const dialogRef = this.dialog.open(CreateReservationDialogComponent, {
      width: '800px',
      maxHeight:'670px',
      data: { arrangement: this.arrangement,
        user:this.user
 }
    });


    dialogRef.afterClosed().subscribe(result => {

      const id = +this.route.snapshot.paramMap.get('id')!;
    this.arrangementService.findById(id).subscribe({
      next: (data: Arrangement) => {
        this.arrangement = data;
        console.log(this.arrangement);

        this.loadRates();
        this.getUser();  //for creating rate
      },
      error: () => {
        console.log('Error fetching arrangement details');
      }
    });

    });

  }

  loadImages(): void {
    if (this.arrangement && this.arrangement.destination && this.arrangement?.destination.id ) {
      this.destinationService.getImages(this.arrangement?.destination.id).subscribe({
        next: (data: string[]) => {
          this.images = data;
          console.log('Images loaded:', this.images);
        },
        error: (err) => {
          console.error('Error loading images:', err);
        }
      });
    }
  }


  isFutureDate(): boolean {
    if (this.arrangement && this.arrangement.date_from) {
      const today = new Date();
      return new Date(this.arrangement.date_from) > today;
    }
    return false;
  }


  checkCustomerPastReservation(arrangementId: number): void {
    if (this.user && this.user.id) {

      this.reservationService.getReservationsByUserId(this.user.id).subscribe({
        next: (reservations) => {

          const pastReservations = reservations.filter(reservation => {
            const dateTo = new Date(reservation.arrangement.date_to);
            const now = new Date();

            const dateToUTC = Date.UTC(dateTo.getFullYear(), dateTo.getMonth(), dateTo.getDate());
            const nowUTC = Date.UTC(now.getFullYear(), now.getMonth(), now.getDate());

            return dateToUTC < nowUTC;
          });

          console.log('Past reservations:', pastReservations);

          this.isCustomerWithPastReservation = pastReservations.some(reservation => {
            return reservation.arrangement.id == this.arrangement.id;
          });

          console.log('Result:', this.isCustomerWithPastReservation);
          console.log("Roles: ", this.roles);
        },
        error: (err) => {
          console.error('Error fetching reservations:', err);
        }
      });
    }
  }
}
