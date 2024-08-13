import { MaterialModule } from 'src/app/common/material/material.module';
import { LayoutModule } from '@angular/cdk/layout';
import { CommonModule } from '@angular/common';
import { AfterViewInit,ViewChild } from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { Component, Inject, OnInit } from '@angular/core';
import { Arrangement } from 'src/app/models/arrangement';
import { ArrangementService } from 'src/app/services/arrangement/arrangement.service';
import { UpdateArrangementDialogComponent } from '../update-arrangement-dialog/update-arrangement-dialog.component';
import { Role } from 'src/app/models/role';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { DeleteDialogComponent } from '../delete-dialog/delete-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-arrangements-table',
  templateUrl: './arrangements-table.component.html',
  styleUrls: ['./arrangements-table.component.css'],
  standalone: true,
  imports: [RouterModule,MaterialModule, CommonModule, LayoutModule]
})
export class ArrangementsTableComponent implements OnInit{

  arrangements: Arrangement[]
  dataSource:MatTableDataSource<Arrangement>;
  displayedColumns: string[] = ['DateFrom','DateTo','FreeSeats','PricePerPerson','Destination','actions-edit','actions-reservations','actions-delete'];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  rolesObjects:Role[]=[];
  roles:string[]=[]

  constructor(private arrangementService:ArrangementService,
    private dialog:MatDialog,
    private router:ActivatedRoute,
    private authService:AuthenticationService,
    private snackBar:MatSnackBar){
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
      }
      else{
        this.rolesObjects.push({ roleName:"UNAUTHENTICATED" });
        this.roles=[];
        this.roles = this.rolesObjects.map(role => role.roleName);
      }
    })

  }

  getAllArrangements(): void {
    this.arrangementService.getAll().subscribe({
      next: (data: Arrangement[]) => {

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


  openDeleteDialog(itemId: number, itemName: string): void {
    const dialogRef = this.dialog.open(DeleteDialogComponent, {
      data: { itemType: 'Arrangement', itemId, itemName }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.delete(itemId);
      }
    });
  }

  delete(id:number):void{

    this.arrangementService.delete(id).subscribe({
      next: () => {
        console.log(`Arrangement with id ${id} deleted successfully`);
        this.snackBar.open('Arrangement deleted successfully!', 'Close', {
          duration: 3000,
          panelClass: ['success-snackbar']
        });
      },
      error: (err) => {
        console.error('Failed to delete arrangement', err);
      }
    });

  }

}
