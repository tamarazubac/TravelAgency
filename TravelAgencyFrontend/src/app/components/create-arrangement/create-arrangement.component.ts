import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Destination } from 'src/app/models/destination';
import { User } from 'src/app/models/user';
import { Component, Inject, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserService } from 'src/app/services/user/user.service';
import { CommonModule } from '@angular/common';
import { LayoutModule } from 'src/app/common/layout/layout.module';
import { ReactiveFormsModule } from '@angular/forms';
import { DestinationService } from 'src/app/services/destination/destination.service';
import { Arrangement } from 'src/app/models/arrangement';
import { ArrangementService } from 'src/app/services/arrangement/arrangement.service';
import { MaterialModule } from 'src/app/common/material/material.module';


@Component({
  selector: 'app-create-arrangement',
  templateUrl: './create-arrangement.component.html',
  styleUrls: ['./create-arrangement.component.css'],
  standalone: true,
  imports: [ReactiveFormsModule, MaterialModule, CommonModule, LayoutModule]
})
export class CreateArrangementComponent {
  arrangementForm: FormGroup;
  destinations: Destination[] = [];
  users: User[] = [];

  constructor(private fb: FormBuilder,
    private destinationService:DestinationService,
    private userService:UserService,
    private arrangementService:ArrangementService,
    private snackBar:MatSnackBar
  ) { }

  ngOnInit(): void {
    this.arrangementForm = this.fb.group({
      description: ['', Validators.required],
      free_seats: ['', [Validators.required, Validators.min(1)]],
      price_per_person: ['', [Validators.required, Validators.min(0)]],
      date_from: ['', Validators.required],
      date_to: ['', Validators.required],
      destination: ['', Validators.required],
      owner: ['', Validators.required]
    });
    this.loadDestinations();
    this.getUsersByRole('ADMIN');
    this.getUsersByRole('SALESMAN');
  }

  loadDestinations(): void {
    this.destinationService.getAll().subscribe((data: Destination[]) => {
      this.destinations = data;
    });
  }

  onSubmit(): void {
    if (this.arrangementForm.valid) {
      const formValue = this.arrangementForm.value;
      const newArrangement: Arrangement = {
        id: undefined,
        date_from: new Date(formValue.date_from),
        date_to: new Date(formValue.date_to),
        description: formValue.description,
        free_seats: formValue.free_seats,
        price_per_person: formValue.price_per_person,
        destination: this.destinations.find(dest => dest.id === formValue.destination),
        owner: formValue.owner
      };

      this.arrangementService.create(newArrangement).subscribe(
        (response: Arrangement) => {
          console.log('Arrangement created successfully:', response);
          this.snackBar.open('Arrangement created successfully!', 'Close', {
            duration: 4000,
            horizontalPosition: 'center'
          });
        },
        (error) => {
          console.error('Error creating arrangement:', error);
        }
      );
    }
  }

  getRolesAsString(user: User): string {
    return user.roles.map(role => role.roleName).join(', ');
  }

  private isUserDuplicate(existingUsers: User[], newUser: User): boolean {
    return existingUsers.some(user => user.id === newUser.id);
  }


  getUsersByRole(roleName: string): void {
    this.userService.getUsersByRole(roleName).subscribe(
      (users: User[]) => {
        console.log(roleName + ": " + users);

        const uniqueUsers = users.filter(user =>
          !this.isUserDuplicate(this.users, user)
        );

        this.users = this.users.concat(uniqueUsers);
      },
      (error) => {
        console.error('Error fetching users by role', error);
      }
    );
  }

}
