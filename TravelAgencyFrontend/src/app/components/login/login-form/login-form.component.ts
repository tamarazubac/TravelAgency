import { Component, ElementRef, ViewChild } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { Router } from '@angular/router';
import { FormControl, FormGroup } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar'; // Import MatSnackBar
import { LogIn } from 'src/app/models/logIn';
import { AuthenticationService } from '../../../services/authentication/authentication.service';
import { AuthResponse } from 'src/app/models/authResponse';
import { UserService } from 'src/app/services/user/user.service';
import { MaterialModule } from 'src/app/common/material/material.module';
import { SocketApiService } from 'src/app/services/sockets/socket-api.service';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css'],
  standalone: true,
  imports: [MaterialModule, ReactiveFormsModule]
})
export class LoginFormComponent {

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
    private userService: UserService,
    private snackBar: MatSnackBar,
    private socketApiService:SocketApiService
  ) {}

  hide = true;
  @ViewChild('usernameInput') usernameInput!: ElementRef;
  @ViewChild('passwordInput') passwordInput!: ElementRef;

  loginForm = new FormGroup({
    username: new FormControl(''),
    password: new FormControl('')
  });

  login(): void {
    if (this.loginForm.valid) {
      const loginCredentials: LogIn = {
        username: this.loginForm.value.username || "",
        password: this.loginForm.value.password || ""
      };

      this.authenticationService.login(loginCredentials).subscribe({
        next: (response: AuthResponse) => {
          localStorage.setItem('user', response.accessToken);  // saving token in local storage

          this.authenticationService.initUser();
          this.authenticationService.setUser();



          this.socketApiService.openSocket(loginCredentials.username);

          this.router.navigate(['home']);
        },
        error: (err) => {
          console.error('Login failed', err);
          this.snackBar.open('Login failed. Please check your credentials.', 'Close', { duration: 3000 });
        }
      });

    }
  }

  register() {
    this.router.navigate(['register']);
  }
}

