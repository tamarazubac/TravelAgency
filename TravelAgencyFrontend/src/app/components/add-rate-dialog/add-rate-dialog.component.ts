import { MatInputModule } from '@angular/material/input';
import { Component, EventEmitter, Inject, Output } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Arrangement } from 'src/app/models/arrangement';
import { Rate } from 'src/app/models/rate';
import { User } from 'src/app/models/user';
import { RateService } from 'src/app/services/rate/rate.service';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-add-rate-dialog',
  templateUrl: './add-rate-dialog.component.html',
  styleUrls: ['./add-rate-dialog.component.css'],
  standalone: true,
  imports: [MatButtonModule,MatFormFieldModule,ReactiveFormsModule,MatIconModule,MatInputModule,CommonModule]
})
export class AddRateDialogComponent {
  rateForm: FormGroup;

  rating = 0;
  stars = Array(5).fill(0);

  @Output() ratingChange = new EventEmitter<number>();

  setRating(value: number): void {
    this.rating = value;
    this.ratingChange.emit(this.rating);
  }

  constructor(
    private fb: FormBuilder,
    private rateService: RateService,
    public dialogRef: MatDialogRef<AddRateDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { arrangement: Arrangement; user: User }
  ) {
    this.rateForm = this.fb.group({
      comment: ['', Validators.required]
    });
  }

  submitFeedback(): void {
    if (this.rateForm.valid) {

      console.log("Rating : ",this.rating)

      const rate: Rate = {
        id: undefined,
        comment: this.rateForm.get('comment')?.value,
        user: this.data.user,
        arrangement: this.data.arrangement,
        rateNum: this.rating
      };

      this.rateService.create(rate).subscribe({
        next: () => {
          this.dialogRef.close(true);
        },
        error: (err) => {
          console.error('Error adding rate:', err);
        }
      });
    }
  }

  cancelFeedback(): void {
    this.dialogRef.close();
  }
}
