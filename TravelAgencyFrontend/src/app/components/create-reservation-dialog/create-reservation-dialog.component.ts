import { CommonModule } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Arrangement } from 'src/app/models/arrangement';
import { User } from 'src/app/models/user';
import { ReservationService } from 'src/app/services/reservation/reservation.service';
import { LayoutModule } from 'src/app/common/layout/layout.module';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Reservation } from 'src/app/models/reservation';
import { MaterialModule } from 'src/app/common/material/material.module';
@Component({
  selector: 'app-create-reservation-dialog',
  templateUrl: './create-reservation-dialog.component.html',
  styleUrls: ['./create-reservation-dialog.component.css'],
  standalone:true,
  imports: [ReactiveFormsModule,MaterialModule, CommonModule, LayoutModule]
})
export class CreateReservationDialogComponent implements OnInit{

  user:User;
  arrangement:Arrangement;

  reservationForm: FormGroup;

  totalPrice:number=0;

  constructor(
    private fb: FormBuilder,
    private reservationService:ReservationService,
    public dialogRef: MatDialogRef<CreateReservationDialogComponent>,
    private snackBar:MatSnackBar,
    @Inject(MAT_DIALOG_DATA) public data: { arrangement: Arrangement; user: User }
  ) {

    this.reservationForm = this.fb.group({

      date_from: [{ value: this.arrangement?.date_from || '', disabled: true }],
      date_to: [{ value: this.arrangement?.date_to || '', disabled: true }],
      destination: [{ value: this.arrangement?.destination?.city_name || '', disabled: true }],
      price_per_person:[{value:this.arrangement?.price_per_person || '',disabled:true}],
      number_of_people:['']
    });

  }
  ngOnInit(): void {

    this.user= this.data.user;
    this.arrangement= this.data.arrangement;

    console.log(this.user);
    console.log(this.arrangement);

    this.reservationForm.get('price_per_person')?.valueChanges.subscribe(() => this.calculatePrice());
    this.reservationForm.get('number_of_people')?.valueChanges.subscribe(() => this.calculatePrice());

    if (this.arrangement.destination) {
      this.reservationForm.patchValue({
        destination: this.arrangement.destination.city_name +', '+ this.arrangement.destination.country_name,
        price_per_person:this.arrangement?.price_per_person,
        date_from:this.arrangement.date_from,
        date_to:this.arrangement.date_to
      });
    }

  }

  calculatePrice() {
    const numberOfPeople = this.reservationForm.get('number_of_people')?.value || 0;
    const pricePerPerson = this.reservationForm.get('price_per_person')?.value || 0;
    this.totalPrice = numberOfPeople * pricePerPerson;
  }

  onCancel(){
    this.dialogRef.close();
  }

  onSubmit(){

    const numOfPeople = this.reservationForm.get('number_of_people')?.value

    if(numOfPeople>this.arrangement.free_seats){
      this.snackBar.open('Not enough free seats available.', 'Close', {
        duration: 3000,
        panelClass: ['error-snackbar']
      });

    }
    else if(numOfPeople<=0){

      this.snackBar.open('Number of people must be greater than zero.', 'Close', {
        duration: 3000,
        panelClass: ['error-snackbar']
      });

    }


    const reservation: Reservation = {
      id: undefined,
      number_of_people: numOfPeople,
      full_price: this.totalPrice,
      arrangement: this.arrangement,
      user: this.user
    };

    console.log('Reservation created:', reservation);


    this.reservationService.create(reservation).subscribe(
      response => {
        console.log('Reservation created:', response);
        this.snackBar.open('Reservation created successfully!', 'Close', {
          duration: 3000,
          panelClass: ['success-snackbar']
        });
      },
      error => {
        console.error('Error creating reservation:', error);
        this.snackBar.open('Failed to create reservation.', 'Close', {
          duration: 3000,
          panelClass: ['error-snackbar']
        });
      }
    );

    this.dialogRef.close();

}

}
