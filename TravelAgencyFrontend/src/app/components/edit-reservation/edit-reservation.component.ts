import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { Arrangement } from 'src/app/models/arrangement';
import { User } from 'src/app/models/user';
import { ReservationService } from 'src/app/services/reservation/reservation.service';
import { MatOptionModule } from '@angular/material/core';
import { MatSelectModule} from '@angular/material/select';
import { MatChipsModule } from '@angular/material/chips';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { LayoutModule } from 'src/app/common/layout/layout.module';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Reservation } from 'src/app/models/reservation';
import { MaterialModule } from 'src/app/common/material/material.module';

@Component({
  selector: 'app-edit-reservation',
  templateUrl: './edit-reservation.component.html',
  styleUrls: ['./edit-reservation.component.css'],
  standalone:true,
  imports: [ReactiveFormsModule, MaterialModule, CommonModule, LayoutModule]

})
export class EditReservationComponent implements OnInit{

  user:User;
  arrangement:Arrangement;

  reservationForm: FormGroup;

  reservation:Reservation;

  totalPrice:number=0;

  constructor(
    private fb: FormBuilder,
    private reservationService:ReservationService,
    public dialogRef: MatDialogRef<EditReservationComponent>,
    private snackBar:MatSnackBar,
    @Inject(MAT_DIALOG_DATA) public data: { reservation:Reservation }
  ) {

    this.reservationForm = this.fb.group({

      date_from: [{ value: this.data.reservation.arrangement?.date_from || '', disabled: true }],
      date_to: [{ value: this.data.reservation.arrangement?.date_to || '', disabled: true }],
      destination: [{ value: this.data.reservation.arrangement?.destination?.city_name || '', disabled: true }],
      number_of_people: [{ value: this.data.reservation.number_of_people || '', disabled: false }],
      totalPrice: [{ value: this.data.reservation.full_price || '', disabled: true }],
      price_per_person:[{value:this.arrangement?.price_per_person || '',disabled:true}],
      full_price:[{value:this.reservation?.full_price || '',disabled:true}]


    });

  }
  ngOnInit(): void {

    this.reservation=this.data.reservation;

    this.arrangement=this.reservation?.arrangement;
    this.user=this.reservation?.user;

    this.reservationForm.get('price_per_person')?.valueChanges.subscribe(() => this.calculatePrice());
    this.reservationForm.get('number_of_people')?.valueChanges.subscribe(() => this.calculatePrice());

    if (this.arrangement.destination) {
      this.reservationForm.patchValue({
        destination: this.arrangement.destination.city_name +', '+ this.arrangement.destination.country_name,
        price_per_person:this.data?.reservation.arrangement?.price_per_person,
        date_from:this.arrangement.date_from,
        date_to:this.arrangement.date_to,
        full_price:this.reservation.full_price

      });
    }
  }



  calculatePrice() {
    const numberOfPeople = this.reservationForm.get('number_of_people')?.value || 0;
    const pricePerPerson = this.reservationForm.get('price_per_person')?.value || 0;
    this.totalPrice = numberOfPeople * pricePerPerson;
  }

  onSubmit(){

    const numOfPeople = this.reservationForm.get('number_of_people')?.value

    if((numOfPeople-this.reservation?.number_of_people)>this.arrangement.free_seats){
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
      id: this.reservation.id,
      number_of_people: numOfPeople,
      full_price: this.totalPrice,
      arrangement: this.arrangement,
      user: this.user
    };

    console.log('Reservation updated:', reservation);

    if(reservation.id)

    this.reservationService.update(reservation.id,reservation).subscribe(
      response => {
        console.log('Reservation updated:', response);
        this.snackBar.open('Reservation updated successfully!', 'Close', {
          duration: 3000,
          panelClass: ['success-snackbar']
        });
      },
      error => {
        console.error('Error updating reservation:', error);
        this.snackBar.open('Failed to update reservation.', 'Close', {
          duration: 3000,
          panelClass: ['error-snackbar']
        });
      }
    );

    this.dialogRef.close();

}

onCancel(){
  this.dialogRef.close();
}
}
