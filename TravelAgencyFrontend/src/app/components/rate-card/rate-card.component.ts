import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { Rate } from 'src/app/models/rate';

@Component({
  selector: 'app-rate-card',
  templateUrl: './rate-card.component.html',
  styleUrls: ['./rate-card.component.css'],
  standalone:true,
  imports:[MatCardModule,CommonModule,MatIconModule]
})
export class RateCardComponent {
  @Input() rate:Rate;

  stars: number[] = [1, 2, 3, 4, 5];

}
