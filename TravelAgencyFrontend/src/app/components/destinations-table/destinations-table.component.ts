import { MaterialModule } from 'src/app/common/material/material.module';
import { LayoutModule } from '@angular/cdk/layout';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { Component, Inject, OnInit } from '@angular/core';
import { ArrangementService } from 'src/app/services/arrangement/arrangement.service';
import { Role } from 'src/app/models/role';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { Destination } from 'src/app/models/destination';
import { DestinationService } from 'src/app/services/destination/destination.service';
import { CreateDestinationDialogComponent } from '../create-destination-dialog/create-destination-dialog.component';
import { DestinationCardComponent } from "../destination-card/destination-card.component";
@Component({
  selector: 'app-destinations-table',
  templateUrl: './destinations-table.component.html',
  styleUrls: ['./destinations-table.component.css'],
  standalone:true,
  imports: [RouterModule, MaterialModule, CommonModule, LayoutModule, DestinationCardComponent]

})
export class DestinationsTableComponent implements OnInit{

  destinations:Destination[];


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

      }else{
        this.rolesObjects.push({ roleName:"UNAUTHENTICATED" });
        this.roles=[];
        this.roles = this.rolesObjects.map(role => role.roleName);
      }
    })
  }


  getAllDestinations(): void {
    this.destinationService.getAll().subscribe({
      next: (data: Destination[]) => {

        this.destinations=data;

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
