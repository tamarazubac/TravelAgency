import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Arrangement } from 'src/app/models/arrangement';
import { CommonModule, DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import { DestinationService } from 'src/app/services/destination/destination.service';
import { MaterialModule } from 'src/app/common/material/material.module';
import { Role } from 'src/app/models/role';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { UpdateArrangementDialogComponent } from '../update-arrangement-dialog/update-arrangement-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { DeleteDialogComponent } from '../delete-dialog/delete-dialog.component';
import { ArrangementService } from 'src/app/services/arrangement/arrangement.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-arrangement-card',
  templateUrl: './arrangement-card.component.html',
  styleUrls: ['./arrangement-card.component.css'],
  standalone: true,
  imports: [MaterialModule,DatePipe,CommonModule]
})
export class ArrangementCardComponent implements OnInit{
  @Input()
  arrangement: Arrangement;

  images: string[] = [];

  roles:String[]=[];
  rolesObjects:Role[]=[];

  @Output() arrangementUpdated = new EventEmitter<void>();

  constructor(private router:Router,
              private destinationService:DestinationService,
              private authService:AuthenticationService,
              private dialog:MatDialog,
              private arrangementService:ArrangementService,
              private snackBar:MatSnackBar){}

  ngOnInit(): void {
    this.loadImages();

    this.authService.userState.subscribe((result) => {
      this.roles=[]
      this.rolesObjects=[]
      if(result && result != null){

        this.rolesObjects=result.roles;
        this.roles = this.rolesObjects.map(role => role.roleName);

        if(this.roles.includes('UNAUTHENTICATED')){
          this.roles = this.roles.filter(role => role !== 'UNAUTHENTICATED');
        }
      }
      else{
        this.rolesObjects.push({ roleName:"UNAUTHENTICATED" });
        this.roles=[];
        this.roles = this.rolesObjects.map(role => role.roleName);
      }
    })
  }


  viewDetails() {
    this.router.navigate(['/arrangementsDetails', this.arrangement.id]);
  }

  loadImages(): void {

    if (this.arrangement.destination && this.arrangement.destination.id) {

      this.destinationService.getImages(this.arrangement.destination.id).subscribe({
        next: (data: string[]) => {
          this.images = data;
          // console.log('Images loaded:', this.images);
        },
        error: (err) => {
          console.error('Error loading images:', err);
        }
      });
    }
  }

  isOutdated(): boolean {
    if (this.arrangement && this.arrangement.date_from) {
      const today = new Date();
      return new Date(this.arrangement.date_from) < today;
    }
    return false;
  }


  editArrangement(){
    const dialogRef = this.dialog.open(UpdateArrangementDialogComponent, {
      width: '800px',
      maxHeight:'800px',
      data: {element:JSON.parse(JSON.stringify(this.arrangement)) }
    });

    this.arrangementUpdated.emit();


    dialogRef.afterClosed().subscribe(result => {
      this.arrangementUpdated.emit();
    });
  }

  deleteArrangement(){
    this.openDeleteDialog(this.arrangement.id,'Arrangement');
  }


  openDeleteDialog(itemId: number|undefined, itemName: string): void {
    const dialogRef = this.dialog.open(DeleteDialogComponent, {
      data: { itemType: 'Arrangement', itemId, itemName }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && itemId) {
        this.delete(itemId);
        this.arrangementUpdated.emit();
      }
    });
  }

  delete(id:number):void{

    this.arrangementService.delete(id).subscribe({
      next: () => {
        console.log(`Arrangement with id ${id} deleted successfully`);
        this.snackBar.open('Arrangement deleted successfully!', 'Close', {
          duration: 3000,
          panelClass: ['success-snackbar']
        });
      },
      error: (err) => {
        console.error('Failed to delete arrangement', err);
      }
    });

  }
}
