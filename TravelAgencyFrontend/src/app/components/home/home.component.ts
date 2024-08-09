import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Arrangement } from 'src/app/models/arrangement';
import { ArrangementService } from 'src/app/services/arrangement/arrangement.service';
import { ReactiveFormsModule} from '@angular/forms';
import { MatFormField } from '@angular/material/form-field';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  arrangementList: Arrangement[] = [];

  constructor(
    private http: HttpClient,
    private router: Router,
    private arrangementService: ArrangementService
  ) {}

  ngOnInit(): void {
    const reloaded = localStorage.getItem('reloaded');

    if (!reloaded) {
      localStorage.setItem('reloaded', 'true');
      location.reload();
    } else {
      localStorage.removeItem('reloaded');
    }

    this.arrangementService.getAll().subscribe({
      next: (data: Arrangement[]) => {
        this.arrangementList = data;
        console.log("Arrangements:", this.arrangementList);
      },
      error: (_) => {
        console.log("Error!");
      }
    });
  }

  onSearch(arrangements: Arrangement[]): void {
    this.arrangementList = arrangements;
  }
}
