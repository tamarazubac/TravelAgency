import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Role } from 'src/app/models/role';
import { User } from 'src/app/models/user';
import { RoleService } from 'src/app/services/role/role.service';
import { UserService } from 'src/app/services/user/user.service';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatListModule } from '@angular/material/list';
import { CommonModule } from '@angular/common';
import { LayoutModule } from 'src/app/common/layout/layout.module';
import { MatSelectModule } from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from 'src/app/common/material/material.module';


@Component({
  selector: 'app-edit-roles-dialog',
  templateUrl: './edit-roles-dialog.component.html',
  styleUrls: ['./edit-roles-dialog.component.css'],
  standalone: true,
  imports: [ReactiveFormsModule,MaterialModule, CommonModule, LayoutModule]

})
export class EditRolesDialogComponent implements OnInit {
  rolesForm: FormGroup;
  element: User;
  currentRoles: string[] = [];
  availableRoles: Role[] = [];

  constructor(
    private fb: FormBuilder,
    private snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<EditRolesDialogComponent>,
    private roleService: RoleService,
    private userService:UserService,
    @Inject(MAT_DIALOG_DATA) public data: { element: User }
  ) {

    this.rolesForm = this.fb.group({
      // newRole: ['']
      newRoles: [[]]
    });

  }

  ngOnInit(): void {
    this.element = this.data.element;
    this.currentRoles = this.element.roles.map(role => role.roleName);
    this.loadAvailableRoles();
    this.rolesForm.patchValue({ newRoles: this.currentRoles });

  }

  loadAvailableRoles(): void {
    this.roleService.getAll().subscribe({
      next: (data: Role[]) => {
        this.availableRoles = data;
      },
      error: (err) => {
        console.error('Failed to load available roles', err);
      }
    });
  }

  closeDialog(): void {
    this.dialogRef.close();
  }

  saveRoles(){
    this.currentRoles=[]
    const selectedRoles = this.rolesForm.get('newRoles')?.value;
    if (selectedRoles && selectedRoles.length > 0) {
      console.log(selectedRoles)

      selectedRoles.forEach((role: string) => {
        if (role && this.element.id) {

          this.userService.addRoleToUser(this.element.id, role).subscribe({
            next: (user) => {
              console.log(`Role ${role} added successfully to user ${user.username}`);
              this.snackBar.open(`Role ${role} added successfully`, 'Close', { duration: 2000 });
              if (!this.currentRoles.includes(role)) {
                this.currentRoles.push(role);
              }
            },
            error: (err) => {
              console.error('Failed to add role', err);
              this.snackBar.open('Failed to add role', 'Close', { duration: 2000 });
            }
          });
        }
      });
    } else {
      this.snackBar.open('No roles selected', 'Close', { duration: 2000 });
    }
  }


  // addRole(){

  //   const selectedRole = this.rolesForm.get('newRole')?.value;
  //   if (selectedRole) {
  //     console.log('Selected Role:', selectedRole);

  //     // this.snackBar.open(`Selected Role: ${selectedRole}`, 'Close', { duration: 2000 });

  //     this.userService.addRoleToUser(this.element.id, selectedRole).subscribe({
  //       next: (user) => {
  //         console.log(`Role ${selectedRole} added successfully to user ${user.username}`);
  //         this.snackBar.open(`Role ${selectedRole} added successfully to user ${user.username}`, 'Close', { duration: 2000 });
  //         this.currentRoles.push(selectedRole);
  //       },
  //       error: (err) => {
  //         console.error('Failed to add role', err);
  //         this.snackBar.open('Failed to add role', 'Close', { duration: 2000 });
  //       }
  //     });




  //   } else {
  //     console.log('No role selected !');
  //     this.snackBar.open('No role selected', 'Close', { duration: 2000 });
  //   }
  // }


  // removeRole() {
  //   const selectedRole = this.rolesForm.get('newRole')?.value;
  //   if (selectedRole) {
  //     console.log('Selected Role:', selectedRole);

  //     this.userService.removeRoleFromUser(this.element.id, selectedRole).subscribe({
  //       next: (user) => {
  //         console.log(`Role ${selectedRole} removed successfully ${user.username}`);
  //         this.snackBar.open(`Role ${selectedRole} removed successfully ${user.username}`, 'Close', { duration: 2000 });

  //         const index = this.currentRoles.indexOf(selectedRole);
  //         if (index > -1) {
  //           this.currentRoles.splice(index, 1);  // Remove the role from the array
  //         }
  //       },
  //       error: (err) => {
  //         console.error('Failed to remove role', err);
  //         this.snackBar.open('Failed to remove role', 'Close', { duration: 2000 });
  //       }
  //     });
  //   } else {
  //     console.log('No role selected !');
  //     this.snackBar.open('No role selected', 'Close', { duration: 2000 });
  //   }
  // }



}
