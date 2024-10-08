import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { ActivatedRoute } from '@angular/router';
import { Arrangement } from 'src/app/models/arrangement';
import { Rate } from 'src/app/models/rate';
import { ArrangementService } from 'src/app/services/arrangement/arrangement.service';
import { RateService } from 'src/app/services/rate/rate.service';
import { RateCardComponent } from '../rate-card/rate-card.component';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { Role } from 'src/app/models/role';
import { MatDialogRef } from '@angular/material/dialog';
import { MatDialog } from '@angular/material/dialog';
import { AddRateDialogComponent } from '../add-rate-dialog/add-rate-dialog.component';
import { User } from 'src/app/models/user';
import { JwtHelperService } from '@auth0/angular-jwt';
import { UserService } from 'src/app/services/user/user.service';
import { MatChipsModule } from '@angular/material/chips';
import { CreateReservationDialogComponent } from '../create-reservation-dialog/create-reservation-dialog.component';


@Component({
  selector: 'app-arrangement-details',
  templateUrl: './arrangement-details.component.html',
  styleUrls: ['./arrangement-details.component.css'],
  standalone: true,
  imports: [CommonModule, MatCardModule, MatIconModule, MatButtonModule, RateCardComponent,MatChipsModule]
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

  // Define static images
  images: string[] = [
    "../../../assets/images/nice.jpg",
    "../../../assets/images/ven.jpg",
    "../../../assets/images/ljorent.jpg"
  ];

  constructor(
    private arrangementService: ArrangementService,
    private rateService:RateService,
    private authService:AuthenticationService,
    private userService:UserService,
    private route: ActivatedRoute,
    private dialog:MatDialog

  ) { }

  ngOnInit(): void {
    const id = +this.route.snapshot.paramMap.get('id')!;
    this.arrangementService.findById(id).subscribe({
      next: (data: Arrangement) => {
        this.arrangement = data;
        console.log(this.arrangement);

        this.loadRates();
        this.getUser();
      },
      error: () => {
        console.log('Error fetching arrangement details');
      }
    });

    this.authService.userState.subscribe((result) => {
      this.roles=[]
      this.rolesObjects=[]
      if(result &&
        result != null){
        this.rolesObjects=result.roles;
        this.roles = this.rolesObjects.map(role => role.roleName);

        if(this.roles.includes('UNAUTHENTICATED')){
          this.roles = this.roles.filter(role => role !== 'UNAUTHENTICATED');
        }

        console.log("Roles 1 : ",this.roles)

      }else{
        this.rolesObjects.push({ roleName:"UNAUTHENTICATED" });
        this.roles=[];
        this.roles = this.rolesObjects.map(role => role.roleName);
        console.log("Roles 2 : ",this.roles)
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
          if (user) {
            this.user=user
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
        this.getUser();
      },
      error: () => {
        console.log('Error fetching arrangement details');
      }
    });

    });

  }
}
