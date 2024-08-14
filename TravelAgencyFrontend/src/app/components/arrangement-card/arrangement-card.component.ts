import { Component, Input, OnInit } from '@angular/core';
import { Arrangement } from 'src/app/models/arrangement';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatNativeDateModule } from '@angular/material/core';
import { CommonModule, DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import { Destination } from 'src/app/models/destination';
import { DestinationService } from 'src/app/services/destination/destination.service';

@Component({
  selector: 'app-arrangement-card',
  templateUrl: './arrangement-card.component.html',
  styleUrls: ['./arrangement-card.component.css'],
  standalone: true,
  imports: [MatCardModule,MatButtonModule,MatIconModule,MatNativeDateModule,DatePipe,CommonModule]
})
export class ArrangementCardComponent implements OnInit{
  @Input()
  arrangement: Arrangement;

  images: string[] = [];

  constructor(private router:Router,private destinationService:DestinationService){}
  ngOnInit(): void {
    this.loadImages();
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
}
