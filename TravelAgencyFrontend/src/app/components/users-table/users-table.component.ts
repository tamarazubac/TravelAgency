import { MaterialModule } from 'src/app/common/material/material.module';
import { LayoutModule } from '@angular/cdk/layout';
import { CommonModule } from '@angular/common';
import { AfterViewInit,ViewChild } from '@angular/core';
import {MatPaginator, MatPaginatorModule} from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import {MatTableDataSource, MatTableModule} from '@angular/material/table';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/services/user/user.service';
import { EditRolesDialogComponent } from '../edit-roles-dialog/edit-roles-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { Component, Inject, OnInit } from '@angular/core';
import { DeleteDialogComponent } from '../delete-dialog/delete-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-users-table',
  templateUrl: './users-table.component.html',
  styleUrls: ['./users-table.component.css'],
  standalone: true,
  imports: [MaterialModule, CommonModule, LayoutModule]

})
export class UsersTableComponent {

  users: User[]
  dataSource:MatTableDataSource<User>;
  displayedColumns: string[] = ['Name', 'Username','Roles','Phone number','actions-edit','actions-delete'];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private service: UserService,private route: ActivatedRoute,private router: Router,private dialog:MatDialog,private snackBar:MatSnackBar) {

  }

  ngOnInit(): void {
   this.getAllUsers();
  }

  getAllUsers(){
    this.service.getAll().subscribe({
      next: (data: User[]) => {
        this.users = data
        this.dataSource = new MatTableDataSource<User>(this.users);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },

    })
  }

  getRolesAsString(roles: { roleName: string }[]): string {
    return roles.map(role => role.roleName).join(', ');
  }


  editRoles(originalElement: User): void {
    const dialogRef = this.dialog.open(EditRolesDialogComponent, {
      width: '800px',
      data: {element:JSON.parse(JSON.stringify(originalElement)) }, //Deep copy
    });


    dialogRef.afterClosed().subscribe(result => {
      this.getAllUsers();
    });

  }


  openDeleteDialog(itemId: number, itemName: string): void {
    const dialogRef = this.dialog.open(DeleteDialogComponent, {
      data: { itemType: 'User', itemId, itemName }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.delete(itemId);
      }
    });
  }

  delete(id:number):void{

    this.service.deleteUserById(id).subscribe({
      next: () => {
        console.log(`User with id ${id} deleted successfully`);
        this.snackBar.open('User deleted successfully!', 'Close', {
          duration: 3000,
          panelClass: ['success-snackbar']
        });
        this.getAllUsers();
      },
      error: (err) => {
        console.error('Failed to delete user', err);
      }
    });

  }


}
