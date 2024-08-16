import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MaterialModule } from 'src/app/common/material/material.module';
import { User } from 'src/app/models/user';
import { ThemePalette } from '@angular/material/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from 'src/app/services/user/user.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Role } from 'src/app/models/role';
import { RoleService } from 'src/app/services/role/role.service';

@Component({
  selector: 'app-edit-account',
  templateUrl: './edit-account.component.html',
  styleUrls: ['./edit-account.component.css'],
  standalone: true,
  imports: [CommonModule, MaterialModule,ReactiveFormsModule],
})
export class EditAccountComponent implements OnInit {
  user: User;
  color: ThemePalette = 'primary';

  availableRoles: Role[] = [];
  currentRoles:string[]=[]

  editAccountDataForm = new FormGroup({
    name: new FormControl('', Validators.required),
    surname: new FormControl('', Validators.required),
    phoneNumber: new FormControl('', [Validators.required, Validators.pattern(/^\d{10}$/)]),
    email: new FormControl('', [Validators.required, Validators.email]),
    username: new FormControl({ value: '', disabled: true }, Validators.required),
    password: new FormControl('', Validators.required),
    confirmPassword: new FormControl('', Validators.required),
    newRoles:new FormControl()
  }, { validators: this.passwordMatchValidator });

  hide = true;

  constructor(private route: ActivatedRoute,
              private userService: UserService,
              private router: Router,
              private snackBar: MatSnackBar,
            private roleService:RoleService) {}

  ngOnInit() {

    this.loadAvailableRoles();
    const accessToken: any = localStorage.getItem('user');
    const helper = new JwtHelperService();
    const decodedToken = helper.decodeToken(accessToken);

    if (decodedToken) {
      this.userService.getByUsername(decodedToken.sub).subscribe(
        (user: User) => {
          if (user) {
            this.user = user;
            this.currentRoles = this.user.roles.map(role => role.roleName);

            const admin = this.currentRoles.includes('ADMIN');

            if (admin) {
              this.editAccountDataForm.get('newRoles')?.enable();
            } else {
              this.editAccountDataForm.get('newRoles')?.disable();
            }

            this.editAccountDataForm.patchValue({
              name: user.first_name,
              surname: user.last_name,
              phoneNumber: user.phone,
              email: user.email,
              username: user.username,
              password: '',
              confirmPassword: '',
              newRoles: this.currentRoles

            });
          }
        },
        error => {
          console.error('Error fetching user:', error);
        }
      );
    } else {
      console.error('Error decoding JWT token');
    }
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


  saveChanges() {

    let updatedRoles: Role[] = [];

    // Check if the user has the 'ADMIN' role
    if (this.currentRoles.includes('ADMIN')) {
      const selectedRoleNames = this.editAccountDataForm.get('newRoles')?.value || [];
      updatedRoles = this.availableRoles.filter(role => selectedRoleNames.includes(role.roleName));
    } else {
      updatedRoles = this.user.roles;
    }

    const changedUser: User = {
      id: this.user.id,
      first_name: this.editAccountDataForm.value.name ?? '',
      last_name: this.editAccountDataForm.value.surname ?? '',
      phone: this.editAccountDataForm.value.phoneNumber ?? '',
      password: this.editAccountDataForm.value.password ?? '',
      email: this.editAccountDataForm.value.email ?? '',
      username: this.user.username,
      roles: updatedRoles
    };

    console.log(updatedRoles)
    this.saveRoles();
    this.userService.update(this.user.id, changedUser).subscribe(
      (response: User) => {
        this.snackBar.open('User successfully updated!', 'OK', { duration: 3000 });
      },
      (error) => {
        this.snackBar.open('Error updating user!', 'OK', { duration: 3000 });
        console.error('Error updating user:', error);
      }
    );

    this.saveRoles();
  }

  openSnackBar(message: string) {
    this.snackBar.open(message, 'OK', {
      duration: 3000,
    });
  }

  passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
    const password = control.get('password')?.value;
    const confirmPassword = control.get('confirmPassword')?.value;

    return password === confirmPassword ? null : { passwordMismatch: true };
  }

  get isFormValid(): boolean {
    return this.editAccountDataForm.valid;
  }

  saveRoles(){
    this.currentRoles=[]
    const selectedRoles = this.editAccountDataForm.get('newRoles')?.value;
    if (selectedRoles && selectedRoles.length > 0) {
      console.log(selectedRoles)

      selectedRoles.forEach((role: string) => {
        if (role && this.user.id) {

          this.userService.addRoleToUser(this.user.id, role).subscribe({
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

}
