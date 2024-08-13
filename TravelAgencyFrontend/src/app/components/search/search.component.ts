import { CommonModule } from '@angular/common';
import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { DestinationService } from 'src/app/services/destination/destination.service';
import { ArrangementService } from 'src/app/services/arrangement/arrangement.service';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { Destination } from 'src/app/models/destination';
import { Arrangement } from 'src/app/models/arrangement';
import { MaterialModule } from 'src/app/common/material/material.module';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css'],
  standalone: true,
  imports: [CommonModule,MaterialModule, ReactiveFormsModule]
})
export class SearchComponent implements OnInit {
  searchForm: FormGroup;
  destinations: Destination[] = [];

  @Output() search = new EventEmitter<Arrangement[]>();

  constructor(
    private fb: FormBuilder,
    private destinationService: DestinationService,
    private arrangementService: ArrangementService
  ) {
    this.searchForm = this.fb.group({
      dateFrom: [null],
      dateTo: [null],
      destination: [null]
    });
  }

  ngOnInit(): void {
    this.destinationService.getAll().subscribe({
      next: (data) => {
        this.destinations = data;
      },
      error: (_) => {
        console.log("Error fetching destinations!");
      }
    });
  }

  onSearch(): void {
    const filters = this.searchForm.value;
    const { dateFrom, dateTo, destination } = filters;

    if (dateFrom && dateTo && destination) {
      this.arrangementService.findByDestinationAndDates(dateFrom, dateTo, destination).subscribe({
        next: (data) => {
          this.search.emit(data);
        },
        error: (_) => {
          console.log("Error during search!");
        }
      });
    } else if (dateFrom && dateTo) {
      this.arrangementService.findByDates(dateFrom, dateTo).subscribe({
        next: (data) => {
          this.search.emit(data);
        },
        error: (_) => {
          console.log("Error during search!");
        }
      });
    }
    else if(!dateFrom && !dateTo && destination){

      this.arrangementService.findByDestination(destination).subscribe({
        next: (data) => {
          this.search.emit(data);
        },
        error: (_) => {
          console.log("Error fetching arrangements by destination!");
        }
      });

    }
  }
}
