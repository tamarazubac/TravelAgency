import { ChangeDetectorRef, Component } from '@angular/core';
import {FormControl, Validators, FormsModule, ReactiveFormsModule, FormGroup, FormBuilder, AbstractControl, ValidationErrors} from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/services/user/user.service';
import { MaterialModule } from 'src/app/common/material/material.module';

@Component({
  selector: 'app-register-form',
  templateUrl: './register-form.component.html',
  styleUrls: ['./register-form.component.css'],
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule,MaterialModule,CommonModule],
})
export class RegisterFormComponent {

  createRegisterForm = new FormGroup({
    name: new FormControl('', Validators.required),
    surname: new FormControl('', Validators.required),
    phoneNumber: new FormControl('', [Validators.required, Validators.pattern(/^\d{10}$/)]),
    username:  new FormControl('', [Validators.required]),
    password: new FormControl('', Validators.required),
    confirmPassword: new FormControl('', Validators.required),
    email:  new FormControl('', [Validators.required, Validators.email]),

  }, { validators: this.passwordMatchValidator });

  hide = true;

  constructor(private cdr: ChangeDetectorRef, private router: Router,private userService:UserService) {
  }
  navigateToHome() {
    this.router.navigate(['home']);
  }

  register() {

    if (this.createRegisterForm.valid) {
      const formValues = this.createRegisterForm.value;

      const newUser: User = {
        id: undefined,
        first_name: formValues.name || '',
        last_name: formValues.surname || '',
        username: formValues.username || '',
        email: formValues.email || '',
        phone: formValues.phoneNumber || '',
        password: formValues.password || '',
        roles: []
      };
      console.log(newUser);


      this.userService.create(newUser).subscribe(
        (response) => {
          console.log('User created successfully:', response);
        },
        (error) => {
          console.error('Error creating user:', error);
        }
      );

    } else {
      console.error('Form is invalid');
    }

  }

  passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
    const password = control.get('password')?.value;
    const confirmPassword = control.get('confirmPassword')?.value;

    return password === confirmPassword ? null : { passwordMismatch: true };
  }

  get isFormValid(): boolean {
    return this.createRegisterForm.valid;
}
}
