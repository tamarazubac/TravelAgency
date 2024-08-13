import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialModule } from '../../common/material/material.module';
import {ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {RouterModule} from "@angular/router";
import { LoginFormComponent } from './login-form/login-form.component';
import { Router } from '@angular/router';
import { FormGroup } from '@angular/forms';

@NgModule({
  declarations: [
    LoginFormComponent,
    LogInModule,
    Router

  ],
  imports: [
    LoginFormComponent,
    LogInModule,
    CommonModule,
    MaterialModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule,
    FormGroup
  ],
  exports: [
    LoginFormComponent,
    LogInModule,
    Router

  ]
})
export class LogInModule { }
