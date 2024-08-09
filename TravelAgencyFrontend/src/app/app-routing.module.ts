import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterFormComponent } from 'src/app/components/register/register-form/register-form.component';
import { LoginFormComponent } from './components/login/login-form/login-form.component';
import { UsersTableComponent } from './components/users-table/users-table.component';
import { HomeComponent } from './components/home/home.component';
import { CreateArrangementComponent } from './components/create-arrangement/create-arrangement.component';
import { ArrangementsTableComponent } from './components/arrangements-table/arrangements-table.component';
import { ArrangementDetailsComponent } from './components/arrangement-details/arrangement-details.component';
import { DestinationsTableComponent } from './components/destinations-table/destinations-table.component';
import { ReservationTableComponent } from './components/reservation-table/reservation-table.component';

const routes: Routes = [
  {component: RegisterFormComponent, path:"register"},
  {component:LoginFormComponent, path:"login"},
  {component:UsersTableComponent,path:"allUsers"},
  {component:HomeComponent,path:"home"},
  {component:CreateArrangementComponent,path:"allArrangements/addArrangement"},
  {component:ArrangementsTableComponent,path:"allArrangements"},
  {component:ArrangementDetailsComponent,path:"arrangementsDetails/:id"},
  {component:DestinationsTableComponent,path:"allDestinations"},
  {component:ReservationTableComponent,path:"allReservations/:id/:type"}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
