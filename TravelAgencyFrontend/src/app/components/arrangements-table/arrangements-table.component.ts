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

@Component({
  selector: 'app-arrangements-table',
  templateUrl: './arrangements-table.component.html',
  styleUrls: ['./arrangements-table.component.css'],
  standalone: true,
  imports: [RouterModule,MatDatepickerModule,MatChipsModule,MatPaginatorModule, MatIconModule,MatTableModule, MatInputModule, MatFormFieldModule, MatButtonModule, MatListModule, CommonModule, LayoutModule]
})
export class ArrangementsTableComponent implements OnInit{

  arrangements: Arrangement[]
  dataSource:MatTableDataSource<Arrangement>;
  displayedColumns: string[] = ['DateFrom','DateTo','FreeSeats','PricePerPerson','Destination','actions-edit','actions-reservations'];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  rolesObjects:Role[]=[];
  roles:string[]=[]

  constructor(private arrangementService:ArrangementService,
    private dialog:MatDialog,
    private router:ActivatedRoute,
  private authService:AuthenticationService){

  }

  ngOnInit(): void {
    this.getAllArrangements();


    this.authService.userState.subscribe((result) => {
      this.roles=[]
      this.rolesObjects=[]
      if(result &&
        result != null){
        this.rolesObjects=result.roles;
        this.roles = this.rolesObjects.map(role => role.roleName);

        if(this.roles.includes('UNAUTHENTICATED')){
          this.roles = this.roles.filter(role => role !== 'UNAUTHENTICATED');
        }

        console.log("Roles 1 : ",this.roles)

      }else{
        this.rolesObjects.push({ roleName:"UNAUTHENTICATED" });
        this.roles=[];
        this.roles = this.rolesObjects.map(role => role.roleName);
        console.log("Roles 2 : ",this.roles)
      }
    })

  }

  getAllArrangements(): void {
    this.arrangementService.getAll().subscribe({
      next: (data: Arrangement[]) => {
        // Filter out arrangements with undefined destination
        this.arrangements = data.filter(arrangement => arrangement.destination !== undefined);
        this.dataSource = new MatTableDataSource<Arrangement>(this.arrangements);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;

        console.log(this.arrangements)
      },
      error: (err) => {
        console.error('Error fetching arrangements:', err);
      }
    });
  }

  edit(arrangement:Arrangement){
    const dialogRef = this.dialog.open(UpdateArrangementDialogComponent, {
      width: '800px',
      maxHeight:'800px',
      data: {element:JSON.parse(JSON.stringify(arrangement)) }
    });


    dialogRef.afterClosed().subscribe(result => {
      this.getAllArrangements();
    });
  }

  viewReservations(id:number){


  }


  // delete(id: number): void {
  //   this.arrangementService.delete(id).subscribe({
  //     next: () => {
  //       console.log('Arrangement deleted successfully');
  //       this.getAllArrangements();
  //     },
  //     error: (err) => {
  //       console.error('Error deleting arrangement:', err);
  //     }
  //   });
  // }

}
