import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatIconModule } from '@angular/material/icon';
import { MaterialModule } from 'src/app/common/material/material.module';
import { LayoutModule } from '@angular/cdk/layout';
import { CommonModule } from '@angular/common';
import { AfterViewInit,ViewChild } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatChipsModule } from '@angular/material/chips';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import {MatPaginator, MatPaginatorModule} from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import {MatTableDataSource, MatTableModule} from '@angular/material/table';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/services/user/user.service';
import { EditRolesDialogComponent } from '../edit-roles-dialog/edit-roles-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { Component, Inject, OnInit } from '@angular/core';
import { Arrangement } from 'src/app/models/arrangement';
import { ArrangementService } from 'src/app/services/arrangement/arrangement.service';
import { UpdateArrangementDialogComponent } from '../update-arrangement-dialog/update-arrangement-dialog.component';
import { Role } from 'src/app/models/role';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';

import { Reservation } from 'src/app/models/reservation';
import { ReservationService } from 'src/app/services/reservation/reservation.service';
import { EditReservationComponent } from '../edit-reservation/edit-reservation.component';


@Component({
  selector: 'app-reservation-table',
  templateUrl: './reservation-table.component.html',
  styleUrls: ['./reservation-table.component.css'],
  standalone:true,
  imports: [MatIconModule,RouterModule,MatDatepickerModule,MatChipsModule,MatPaginatorModule, MatIconModule,MatTableModule, MatInputModule, MatFormFieldModule, MatButtonModule, MatListModule, CommonModule, LayoutModule]

})
export class ReservationTableComponent {

  id: number;
  type:string | null;

  constructor(private route: ActivatedRoute,
             private reservationService:ReservationService,
             private snackBar:MatSnackBar,
             private dialog:MatDialog
  ) { }

  reservations: Reservation[] = [];

  dataSource:MatTableDataSource<Reservation>;
  displayedColumns: string[] = ['FullPrice','NumberOfPeople','Destination','DateFrom','DateTo','User','actions-edit','actions-delete'];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;


  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.id = Number(params.get('id'));
      this.type = params.get('type');

      console.log(`Received id: ${this.id}, type: ${this.type}`);

      if (this.type === 'arrangement') {
        this.loadByArrangementId();
      } else if (this.type === 'user') {
        this.loadByUserId();
      } else {
        this.loadAll();
      }
    });
  }


  loadByArrangementId(): void {
    this.reservationService.getReservationsByArrangementId(this.id)
      .subscribe({
        next: (data: Reservation[]) => {
          this.reservations = data;
          console.log(this.reservations)
          this.dataSource = new MatTableDataSource<Reservation>(this.reservations);
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;

        },
        error: (err) => {
          console.error('Failed to load reservations', err);
        }
      });
  }

  loadByUserId(): void {
    this.reservationService.getReservationsByUserId(this.id)
      .subscribe({
        next: (data: Reservation[]) => {
          this.reservations = data;
          console.log(this.reservations)
          this.dataSource = new MatTableDataSource<Reservation>(this.reservations);
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;

        },
        error: (err) => {
          console.error('Failed to load reservations', err);
        }
      });
  }

  loadAll(): void {
    this.reservationService.getAll()
      .subscribe({
        next: (data: Reservation[]) => {
          this.reservations = data;
          console.log(this.reservations)
          this.dataSource = new MatTableDataSource<Reservation>(this.reservations);
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;

        },
        error: (err) => {
          console.error('Failed to load reservations', err);
        }
      });
  }


  edit(element:Reservation){

    const dialogRef = this.dialog.open(EditReservationComponent, {
      width: '800px',
      data: { reservation: element
       }
    });

    dialogRef.afterClosed().subscribe(result => {

        this.ngOnInit();

    });

  }
  delete(id: number) {
    this.reservationService.delete(id).subscribe(
      () => {
        this.reservations = this.reservations.filter(reservation => reservation.id !== id);
        this.snackBar.open('Reservation deleted successfully!', 'Close', {
          duration: 3000,
          panelClass: ['success-snackbar']
        });
        this.ngOnInit();
      },
      error => {
        console.error('Error deleting reservation:', error);
        this.snackBar.open('Failed to delete reservation.', 'Close', {
          duration: 3000,
          panelClass: ['error-snackbar']
        });
      }
    );
  }


}
