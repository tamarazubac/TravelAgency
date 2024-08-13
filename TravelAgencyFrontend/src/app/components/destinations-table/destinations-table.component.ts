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
import { MatDialog } from '@angular/material/dialog';
import { Component, Inject, OnInit } from '@angular/core';
import { ArrangementService } from 'src/app/services/arrangement/arrangement.service';
import { Role } from 'src/app/models/role';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { Destination } from 'src/app/models/destination';
import { DestinationService } from 'src/app/services/destination/destination.service';
import { CreateDestinationDialogComponent } from '../create-destination-dialog/create-destination-dialog.component';
@Component({
  selector: 'app-destinations-table',
  templateUrl: './destinations-table.component.html',
  styleUrls: ['./destinations-table.component.css'],
  standalone:true,
  imports: [RouterModule, MaterialModule, CommonModule, LayoutModule]

})
export class DestinationsTableComponent implements OnInit{

  destinations:Destination[];
  dataSource:MatTableDataSource<Destination>;
  displayedColumns: string[] = ['City','Country'];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  rolesObjects:Role[]=[];
  roles:string[]=[]

  constructor(private arrangementService:ArrangementService,
    private destinationService:DestinationService,
    private dialog:MatDialog,
    private router:ActivatedRoute,
    private authService:AuthenticationService){

  }
  ngOnInit(): void {


    this.getAllDestinations();


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


  getAllDestinations(): void {
    this.destinationService.getAll().subscribe({
      next: (data: Destination[]) => {
        this.dataSource = new MatTableDataSource<Destination>(data);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;

        console.log(this.destinations)
      },
      error: (err) => {
        console.error('Error fetching destinations:', err);
      }
    });
  }

  addDestination(): void {
    const dialogRef = this.dialog.open(CreateDestinationDialogComponent, {
      width: '800px',
      data: {},
    });


    dialogRef.afterClosed().subscribe(result => {
      this.getAllDestinations();

    });


  }

}
