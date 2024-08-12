import { Injectable, OnInit } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Observable } from 'rxjs';
import { Role } from 'src/app/models/role';
import { User } from 'src/app/models/user';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { UserService } from 'src/app/services/user/user.service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate{

  constructor(
    private router: Router,
    private authService: AuthenticationService,
    private userService:UserService
  ) {}


  rolesObjects:Role[]=[];
  roles:String[]=[];

  async canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Promise<boolean | UrlTree> {
    console.log('Checking authentication...');

    try {
      await this.getUser();

      if (!this.authService.isLoggedIn()) {
        console.log('User not logged in. Redirecting to login...');
        this.router.navigate(['login']);
        return false;
      }

      const expectedRoles = route.data['roles'] as Array<string>;

      console.log('Expected Roles:', expectedRoles);
      console.log('User Roles:', this.roles);

      if (expectedRoles && !expectedRoles.some(role => this.roles.includes(role))) {
        console.log('User does not have required role. Redirecting to home...');
        this.router.navigate(['home']);
        return false;
      }

      console.log('User authorized.');
      return true;

    } catch (error) {
      console.error('Error during authentication:', error);
      this.router.navigate(['home']);
      return false;
    }
  }


  getUser(): Promise<void> {
    return new Promise((resolve, reject) => {
      const accessToken: any = localStorage.getItem('user');
      const helper = new JwtHelperService();
      const decodedToken = helper.decodeToken(accessToken);

      if (decodedToken) {
        this.userService.getByUsername(decodedToken.sub).subscribe(
          (user: User) => {
            this.rolesObjects = user.roles;
            this.roles = this.rolesObjects.map(role => role.roleName);
            console.log("Roles:", this.roles);
            resolve();
          },
          (error) => {
            console.error('Error fetching user details:', error);
            reject(error);
          }
        );
      } else {
        console.error('Error decoding JWT token');
        reject(new Error('Error decoding JWT token'));
      }
    });
  }


}
