import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Arrangement } from 'src/app/models/arrangement';
import { ArrangementService } from 'src/app/services/arrangement/arrangement.service';
import { ReactiveFormsModule} from '@angular/forms';
import { MatFormField } from '@angular/material/form-field';
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

  constructor(
    private http: HttpClient,
    private router: Router,
    private arrangementService: ArrangementService,
    private authService:AuthenticationService,
    private userService:UserService
  ) {}

  ngOnInit(): void {
    const reloaded = localStorage.getItem('reloaded');

    this.getUser();

    this.authService.userState.subscribe((result) => {
      this.roles=[]
      this.rolesObjects=[]
      if(result && result != null){
        this.rolesObjects=result.roles;
        this.roles = this.rolesObjects.map(role => role.roleName);

        if(this.roles.includes('UNAUTHENTICATED')){
          this.roles = this.roles.filter(role => role !== 'UNAUTHENTICATED');
        }

      }
      else{
        this.rolesObjects.push({ roleName:"UNAUTHENTICATED" });
        this.roles=[];
        this.roles = this.rolesObjects.map(role => role.roleName);
      }
    })

    // if (!reloaded) {
    //   localStorage.setItem('reloaded', 'true');
    //   location.reload();
    // } else {
    //   localStorage.removeItem('reloaded');
    // }

    this.arrangementService.getAll().subscribe({
      next: (data: Arrangement[]) => {
        this.arrangementList = data;
        // console.log("Arrangements:", this.arrangementList);
      },
      error: (_) => {
        console.log("Error!");
      }
    });
  }

  onSearch(arrangements: Arrangement[]): void {
    this.arrangementList = arrangements;
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
