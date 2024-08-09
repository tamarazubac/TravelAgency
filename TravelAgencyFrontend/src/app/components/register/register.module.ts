import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialModule } from 'src/app/common/material/material.module';
import {ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {RouterModule} from "@angular/router";
import { RegisterFormComponent} from './register-form/register-form.component';
import { Router } from '@angular/router';


@NgModule({
  declarations: [
    // HomeComponent,
    RegisterFormComponent,
    RegisterModule,
    Router,
    RegisterFormComponent

  ],
  imports: [
    RegisterFormComponent,
    RegisterModule,
    CommonModule,
    MaterialModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule
  ],
  exports: [
    RegisterFormComponent,
    RegisterModule,
    Router

  ]
})
export class RegisterModule { }
