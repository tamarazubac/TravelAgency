import { CommonModule } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { MaterialModule } from 'src/app/common/material/material.module';
import { Rate } from 'src/app/models/rate';
import { DeleteDialogComponent } from '../delete-dialog/delete-dialog.component';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { RateService } from 'src/app/services/rate/rate.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Role } from 'src/app/models/role';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';

@Component({
  selector: 'app-rate-card',
  templateUrl: './rate-card.component.html',
  styleUrls: ['./rate-card.component.css'],
  standalone:true,
  imports:[MaterialModule,CommonModule]
})
export class RateCardComponent implements OnInit{
  @Input() rate:Rate;

  constructor(private dialog:MatDialog,
              private rateService:RateService,
              private snackBar:MatSnackBar,
              private authService:AuthenticationService
  ){}
  ngOnInit():void{

    this.authService.userState.subscribe((result) => {
      this.roles=[]
      this.rolesObjects=[]
      if(result && result != null){
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


  stars: number[] = [1, 2, 3, 4, 5];


  roles:String[]=[];
  rolesObjects:Role[]=[];

  openDeleteDialog(itemId: number|undefined, itemName: string): void {
    const dialogRef = this.dialog.open(DeleteDialogComponent, {
      data: { itemType: 'Rate', itemId, itemName }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && itemId) {
        this.delete(itemId);
      }
    });
  }

  delete(id:number):void{

    this.rateService.delete(id).subscribe({
      next: () => {
        console.log(`Rate with id ${id} deleted successfully`);
        this.snackBar.open('Rate deleted successfully!', 'Close', {
          duration: 3000,
          panelClass: ['success-snackbar']
        });
      },
      error: (err) => {
        console.error('Failed to delete user', err);
      }
    });

  }

}
