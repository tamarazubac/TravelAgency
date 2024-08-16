import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Arrangement } from 'src/app/models/arrangement';
import { ArrangementService } from 'src/app/services/arrangement/arrangement.service';
import { Role } from 'src/app/models/role';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { UserService } from 'src/app/services/user/user.service';
import { JwtHelperService } from '@auth0/angular-jwt';
import { User } from 'src/app/models/user';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  arrangementList: Arrangement[] = [];


  roles:String[]=[];
  rolesObjects:Role[]=[];
  userId:number;

  noResultMessage:string;

  constructor(
    private http: HttpClient,
    private router: Router,
    private arrangementService: ArrangementService,
    private authService:AuthenticationService,
    private userService:UserService
  ) {}

  ngOnInit(): void {
    this.getUser();

    this.authService.userState.subscribe((result) => {
      this.roles = [];
      this.rolesObjects = [];

      if (result && result != null) {
        this.rolesObjects = result.roles;
        this.roles = this.rolesObjects.map(role => role.roleName);

        if (this.roles.includes('UNAUTHENTICATED')) {
          this.roles = this.roles.filter(role => role !== 'UNAUTHENTICATED');
        }
      } else {
        this.rolesObjects.push({ roleName: "UNAUTHENTICATED" });
        this.roles = [];
        this.roles = this.rolesObjects.map(role => role.roleName);
      }

      this.getArrangements();

    });
  }


  getArrangements(){
    this.arrangementService.getAll().subscribe({
      next: (data: Arrangement[]) => {
        this.arrangementList = data;

        if (this.roles.includes('UNAUTHENTICATED') || this.roles.includes('CUSTOMER') ) {
          const currentDate = new Date();
          this.arrangementList = this.arrangementList.filter(arrangement =>
            new Date(arrangement.date_to) > currentDate
          );
        }

        if (this.arrangementList.length === 0) {
          this.noResultMessage = "No arrangements available in the future.";
        }
      },
      error: (_) => {
        console.log("Error!");
      }
    });
  }

  onSearch(arrangements: Arrangement[]): void {
    this.arrangementList = arrangements;
    if (arrangements.length === 0) {
        this.noResultMessage = "There are no arrangements corresponding to the search criteria.";
    } else {
        this.noResultMessage = "";
    }
}

  getUser(){
    const accessToken: any = localStorage.getItem('user');
    const helper = new JwtHelperService();
    const decodedToken = helper.decodeToken(accessToken);

    if (decodedToken) {
      this.userService.getByUsername(decodedToken.sub).subscribe(
        (user: User) => {

          if(user.id)
            this.userId=user?.id;
            console.log("Usaoooo ",this.userId)

        },
        (error) => {
          console.error('Error fetching user details:', error);
        }
      );
    } else {
      console.error('Error decoding JWT token');
    }
  }
}
