import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatNativeDateModule } from '@angular/material/core';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { LayoutModule } from 'src/app/common/layout/layout.module';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatListModule } from '@angular/material/list';

import { Arrangement } from 'src/app/models/arrangement';
import { ArrangementService } from 'src/app/services/arrangement/arrangement.service';
import { Destination } from 'src/app/models/destination';
import { DestinationService } from 'src/app/services/destination/destination.service';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/services/user/user.service';
import { MatDatepickerModule} from '@angular/material/datepicker';

import { MatFormFieldModule } from '@angular/material/form-field';
import { Role } from 'src/app/models/role';
import { JwtHelperService } from '@auth0/angular-jwt';
import { MaterialModule } from 'src/app/common/material/material.module';


@Component({
  selector: 'app-update-arrangement-dialog',
  templateUrl: './update-arrangement-dialog.component.html',
  styleUrls: ['./update-arrangement-dialog.component.css'],
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MaterialModule,
    CommonModule,
    LayoutModule
  ]
})
export class UpdateArrangementDialogComponent implements OnInit {
  arrangementForm: FormGroup;
  destinations: Destination[] = [];
  users: User[] = [];

  edit:boolean=true;  //disabled

  admin:boolean;

  constructor(
    private fb: FormBuilder,
    private snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<UpdateArrangementDialogComponent>,
    private arrangementService: ArrangementService,
    private destinationService: DestinationService,
    private userService: UserService,
    @Inject(MAT_DIALOG_DATA) public data: { element: Arrangement }
  ) {
    this.arrangementForm = this.fb.group({
      description: [data.element.description || ''],
      free_seats: [data.element.free_seats || 0],
      price_per_person: [data.element.price_per_person || 0],
      date_from: [data.element.date_from || ''],
      date_to: [data.element.date_to || ''],
      destination: [data.element.destination?.city_name || ''],
      owner: [data.element.owner?.id || '']
    });
  }

  ngOnInit(): void {
    this.loadDestinations();
    this.loadUsers();

    if (this.data.element.owner) {
      this.arrangementForm.patchValue({
        owner: this.data.element.owner.id
      });
    }


    if (this.data.element.destination) {
      this.arrangementForm.patchValue({
        destination: this.data.element.destination.id
      });
    }

    const accessToken: any = localStorage.getItem('user');
    const helper = new JwtHelperService();
    const decodedToken = helper.decodeToken(accessToken);

    if (decodedToken) {
      this.userService.getByUsername(decodedToken.sub).subscribe(
        (user: User) => {

            const roleNames = user.roles.map(role => role.roleName);
            this.admin = roleNames.includes('ADMIN');

            if (this.admin) {
              this.arrangementForm.get('owner')?.enable();
            } else {
              this.arrangementForm.get('owner')?.disable();
            }

        },
        (error) => {
          console.error('Error fetching user details:', error);
        }
      );
    } else {
      console.error('Error decoding JWT token');
    }
  }

  loadDestinations(): void {
    this.destinationService.getAll().subscribe((data: Destination[]) => {
      this.destinations = data;
      console.log("Destinations: ", this.destinations);
    });

  }

  loadUsers(): void {
    this.userService.getUsersByRole('ADMIN').subscribe((users: User[]) => {
      this.users = users;
      console.log('Users (ADMIN): ', this.users);
    });

    this.userService.getUsersByRole('SALESMAN').subscribe((users: User[]) => {
      this.users = this.users.concat(users);
      console.log('Users (SALESMAN): ', this.users);
    });
  }

  getRolesAsString(roles: Role[]): string {
    return ` - ${roles.map(role => role.roleName).join(', ')}`;
  }
  onSubmit(): void {
    if (this.arrangementForm.valid) {
      const formValue = this.arrangementForm.value;

    let selectedOwner: User | undefined;
    if (this.arrangementForm.get('owner')?.enabled) {
      selectedOwner = this.users.find(user => user.id === formValue.owner);
    } else {
      selectedOwner = this.data.element.owner;
    }

      const updatedArrangement: Arrangement = {
        id: this.data.element.id,
        date_from: new Date(formValue.date_from),
        date_to: new Date(formValue.date_to),
        description: formValue.description,
        free_seats: formValue.free_seats,
        price_per_person: formValue.price_per_person,
        destination: this.destinations.find(dest => dest.id === formValue.destination),
        owner: selectedOwner
      };

      console.log('Updated Arrangement:', updatedArrangement);

      if(this.data.element.id){
      this.arrangementService.update(this.data.element.id,updatedArrangement).subscribe(
        (response: Arrangement) => {
          console.log('Arrangement updated successfully:', response);
          this.dialogRef.close(response);
        },
        (error) => {
          console.error('Error updating arrangement:', error);
          this.snackBar.open('Failed to update arrangement', 'Close', { duration: 3000 });
        }
      );
    }
    }
  }

  onCancel(){
    this.dialogRef.close();
  }
}
