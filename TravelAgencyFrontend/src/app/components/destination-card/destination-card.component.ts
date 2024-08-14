import { Component, Input } from '@angular/core';
import { Arrangement } from 'src/app/models/arrangement';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatNativeDateModule } from '@angular/material/core';
import { CommonModule, DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import { MaterialModule } from 'src/app/common/material/material.module';
import { Destination } from 'src/app/models/destination';
import { DestinationService } from 'src/app/services/destination/destination.service';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DeleteDialogComponent } from '../delete-dialog/delete-dialog.component';
import { EditDestinationDialogComponent } from '../edit-destination-dialog/edit-destination-dialog.component';



@Component({
  selector: 'app-destination-card',
  templateUrl: './destination-card.component.html',
  styleUrls: ['./destination-card.component.css'],
  standalone: true,
  imports: [MatCardModule,MaterialModule,DatePipe,CommonModule]
})
export class DestinationCardComponent {

  @Input()
  destination:Destination;

  constructor(private router:Router, private destinationService:DestinationService,
              private dialog:MatDialog,private snackBar:MatSnackBar
  ){}

  images: string[] = [];
  currentImageIndex = 0;

  ngOnInit(): void {
    this.loadImages();

  }

  prevImage(): void {
    if (this.images.length > 0) {
      this.currentImageIndex = (this.currentImageIndex - 1 + this.images.length) % this.images.length;
    }
  }

  nextImage(): void {
    if (this.images.length > 0) {
      this.currentImageIndex = (this.currentImageIndex + 1) % this.images.length;
    }
  }

  loadImages(): void {
    if (this.destination && this.destination.id) {
      this.destinationService.getImages(this.destination.id).subscribe({
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


  openDeleteDialog(itemId: number|undefined, itemName: string): void {
    const dialogRef = this.dialog.open(DeleteDialogComponent, {
      data: { itemType: 'Destination', itemId, itemName }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && itemId) {
        this.delete(itemId);
      }
    });
  }

  openEditDialog(destination:Destination): void {
    const dialogRef = this.dialog.open(EditDestinationDialogComponent, {
      data: { destination },
      minHeight:'600px',
      minWidth:'700px'
    });

    dialogRef.afterClosed().subscribe(result => {

        this.loadImages();

    });
  }


  delete(id:number):void{

    this.destinationService.delete(id).subscribe({
      next: () => {
        console.log(`Destination with id ${id} deleted successfully`);
        this.snackBar.open('Destination deleted successfully!', 'Close', {
          duration: 3000,
          panelClass: ['success-snackbar']
        });
      },
      error: (err) => {
        console.error('Failed to delete user', err);
      }
    });

  }

}
