import { Component, Input } from '@angular/core';
import { Arrangement } from 'src/app/models/arrangement';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatNativeDateModule } from '@angular/material/core';
import { CommonModule, DatePipe } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-arrangement-card',
  templateUrl: './arrangement-card.component.html',
  styleUrls: ['./arrangement-card.component.css'],
  standalone: true,
  imports: [MatCardModule,MatButtonModule,MatIconModule,MatNativeDateModule,DatePipe,CommonModule]
})
export class ArrangementCardComponent {
  @Input()
  arrangement: Arrangement;

  constructor(private router:Router){}


  viewDetails() {
    this.router.navigate(['/arrangementsDetails', this.arrangement.id]);
  }
}
