import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Destination } from 'src/app/models/destination';
import { DestinationService } from 'src/app/services/destination/destination.service';
import { MaterialModule } from 'src/app/common/material/material.module';

@Component({
  selector: 'app-create-destination-dialog',
  templateUrl: './create-destination-dialog.component.html',
  styleUrls: ['./create-destination-dialog.component.css'],
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, MaterialModule, CommonModule]
})
export class CreateDestinationDialogComponent implements OnInit {

  destinationForm: FormGroup;
  destinations:Destination[]=[];

  constructor(
    private fb: FormBuilder,
    private snackBar: MatSnackBar,
    private destinationService:DestinationService,
    private dialogRef: MatDialogRef<CreateDestinationDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {}
  ) {
    this.destinationForm = this.fb.group({
      country: [''],
      city: ['']
    });
  }

  ngOnInit(): void {
    this.loadDestinations();


  }

  closeDialog(): void {
    this.dialogRef.close();
  }

  saveDestination(): void {
    const country = this.destinationForm.get('country')?.value;
    const city = this.destinationForm.get('city')?.value;
    const destinationString = `${country}, ${city}`;

    if (country && city) {
      const newDestination: Destination = {
        id: undefined, // id is autogenerated
        city_name: city,
        country_name: country
      };

      this.destinationService.create(newDestination).subscribe({
        next: (response: Destination) => {
          this.snackBar.open('Destination created successfully!', 'Close', { duration: 2000 });
          this.destinationForm.reset();
        },
        error: (err) => {
          console.error('Error creating destination', err);
          this.snackBar.open('Failed to create destination - already exists.', 'Close', { duration: 3000 });
        }
      });
    } else {
      this.snackBar.open('Please fill in both country and city.', 'Close', { duration: 2000 });
    }

}


  loadDestinations():void{
    this.destinationService.getAll().subscribe({
      next: (data: Destination[]) => {
        this.destinations = data;
        console.log('Destinations loaded:', this.destinations);
      },
      error: (err) => {
        console.error('Failed to load destinations', err);
      }
    });
  }
}
