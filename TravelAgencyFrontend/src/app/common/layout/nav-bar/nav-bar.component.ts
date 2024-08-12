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
import { MaterialModule } from '../../material/material.module';
import { MatMenuModule } from '@angular/material/menu';
import { DateRangeDialogComponent } from 'src/app/components/date-range-dialog/date-range-dialog.component';
import { Router } from '@angular/router';

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
    private reportService : ReportService,
    private router: Router){}


  ngOnInit():void{

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
  }

  logout(): void {
    this.authService.logout();
    this.roles=[];
    this.router.navigate(['/home']);
  }


  downloadReportDestination(): void {
    this.reportService.downloadReportDestination().subscribe((response: Blob) => {

      const url = window.URL.createObjectURL(response);  //url for blob

      // link for download
      const link = document.createElement('a');
      link.href = url;
      link.download = 'DestinationReport.pdf';
      link.click();


      window.URL.revokeObjectURL(url);
    }, error => {
      console.error('Error downloading the report', error);
    });
  }

  downloadBalanceReport(): void {

    const dialogRef = this.dialog.open(DateRangeDialogComponent, {
      width: '300px',
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog is closed');
    });
  }

}
