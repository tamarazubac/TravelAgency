import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { AuthResponse } from 'src/app/models/authResponse';
import { LogIn } from 'src/app/models/logIn';
import { environment } from 'src/app/common/env/env';
import {JwtHelperService} from "@auth0/angular-jwt";
import { User } from 'src/app/models/user';
import { UserService } from '../user/user.service';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {


  private headers = new HttpHeaders({
    'Content-Type': 'application/json',
    skip: 'true',
  });

  user$ = new BehaviorSubject<User | null>(null);
  userState = this.user$.asObservable();

  constructor(private http: HttpClient,private userService:UserService,private router:Router) {

    this.user$.next(this.getRole());
    this.initUser();

  }



  login(credentials: LogIn): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(environment.apiHost + 'logIn', credentials, {
      headers: this.headers,
    });
  }


  setUser(): void {
    const accessToken: any = localStorage.getItem('user');
    const helper = new JwtHelperService();

    const decodedToken = helper.decodeToken(accessToken);

    if (decodedToken) {
      console.log('Decoded token : ',decodedToken)
    } else {
      console.error('Error decoding JWT token');
    }

  }


  getRole(): any {
    if (this.isLoggedIn()) {
      const accessToken: any = localStorage.getItem('user');
      const helper = new JwtHelperService();
      const decodedToken = helper.decodeToken(accessToken);
      console.log("AUTHORITY -> ",helper.decodeToken(accessToken).authority );
      return helper.decodeToken(accessToken).authority;
    }

  }



  isLoggedIn(): boolean {
    return localStorage.getItem('user') != null;
  }


  private initUser(): void {
    const accessToken: any = localStorage.getItem('user');
    const helper = new JwtHelperService();
    const decodedToken = helper.decodeToken(accessToken);

    if (decodedToken) {
      this.userService.getByUsername(decodedToken.sub).subscribe(
        (user: User) => {
          if (user) {
            this.user$.next(user);
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


  logout(): void {
    this.http.get(environment.apiHost + 'logOut', {
     responseType: 'text',
   }).subscribe({
     next:()=>{
       localStorage.removeItem('user');
       this.user$.next(null);
     }
   });
   localStorage.removeItem('user');
   this.user$.next(null);
 }

}
