import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { MaterialModule } from 'src/app/common/material/material.module';
import { Rate } from 'src/app/models/rate';

@Component({
  selector: 'app-rate-card',
  templateUrl: './rate-card.component.html',
  styleUrls: ['./rate-card.component.css'],
  standalone:true,
  imports:[MaterialModule,CommonModule]
})
export class RateCardComponent {
  @Input() rate:Rate;

  stars: number[] = [1, 2, 3, 4, 5];

}
