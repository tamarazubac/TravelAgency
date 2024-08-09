import { STRING_TYPE } from '@angular/compiler';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { JwtHelperService } from '@auth0/angular-jwt';
import { CreateDestinationDialogComponent } from 'src/app/components/create-destination-dialog/create-destination-dialog.component';
import { Role } from 'src/app/models/role';
import { User } from 'src/app/models/user';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { ReportService } from 'src/app/services/reports/report.service';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit{

  roles:String[]=[];
  rolesObjects:Role[]=[];

  userId:number|undefined;

  constructor(private authService:AuthenticationService,
    private dialog:MatDialog,
    private userService:UserService,
  private reportService : ReportService){

  }

  ngOnInit():void{

    this.getUser();

    this.authService.userState.subscribe((result) => {
      this.roles=[]
      this.rolesObjects=[]
      if(result &&
        result != null){
        this.rolesObjects=result.roles;
        this.roles = this.rolesObjects.map(role => role.roleName);

        if(this.roles.includes('UNAUTHENTICATED')){
          this.roles = this.roles.filter(role => role !== 'UNAUTHENTICATED');
        }

        console.log("Roles 1 : ",this.roles)

      }else{
        this.rolesObjects.push({ roleName:"UNAUTHENTICATED" });
        this.roles=[];
        this.roles = this.rolesObjects.map(role => role.roleName);
        console.log("Roles 2 : ",this.roles)
      }
    })



  }

  logout(): void {
    this.authService.logout();
    this.roles=[];
  }

  getUser(){
    const accessToken: any = localStorage.getItem('user');
    const helper = new JwtHelperService();
    const decodedToken = helper.decodeToken(accessToken);

    if (decodedToken) {
      this.userService.getByUsername(decodedToken.sub).subscribe(
        (user: User) => {

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


  addDestination(): void {
    const dialogRef = this.dialog.open(CreateDestinationDialogComponent, {
      width: '800px',
      data: {},
    });


    // dialogRef.afterClosed().subscribe(result => {

    // });


  }

  downloadReport(): void {
    this.reportService.downloadUserReport().subscribe((response: Blob) => {

      const url = window.URL.createObjectURL(response);  //url for blob

      // link for download
      const link = document.createElement('a');
      link.href = url;
      link.download = 'userReport.pdf';
      link.click();


      window.URL.revokeObjectURL(url);
    }, error => {
      console.error('Error downloading the report', error);
    });
  }



}
