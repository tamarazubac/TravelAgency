import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { RegisterFormComponent } from './register-form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatError, MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { ActivatedRoute, Router } from '@angular/router';

import { tap } from 'rxjs/operators';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { MatSelectHarness } from '@angular/material/select/testing';
import { MatOptionHarness } from '@angular/material/core/testing';
import { MatSlideToggleHarness } from '@angular/material/slide-toggle/testing';


import { By } from '@angular/platform-browser';
import { ChangeDetectorRef, Component } from '@angular/core';
import {FormControl, Validators,FormGroup, FormBuilder, AbstractControl, ValidationErrors} from '@angular/forms';
import {MatSelectChange} from '@angular/material/select';

import {MatSlideToggleChange} from '@angular/material/slide-toggle';
import { MatOption, MatOptionSelectionChange, ThemePalette } from '@angular/material/core';
import { NgZone } from '@angular/core';
import { HarnessLoader } from '@angular/cdk/testing';



describe('RegisterFormComponent', () => {
  let component: RegisterFormComponent;
  let fixture: ComponentFixture<RegisterFormComponent>;
  

  let loader:HarnessLoader;
  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [],
      imports: [
        RegisterFormComponent,
        FormsModule,
        ReactiveFormsModule,
        MatIconModule,
        MatInputModule,
        MatFormFieldModule,
        MatButtonModule,
        MatSelectModule,
        MatSlideToggleModule,
        HttpClientModule,
        BrowserAnimationsModule,

        MatSelectModule
      ],
      providers: [
        { provide: ActivatedRoute, useValue: {} },
        { provide: Router, useValue: {} },
        MatSnackBar,
      ],
    });

    fixture = TestBed.createComponent(RegisterFormComponent);
    component = fixture.componentInstance;
    loader = TestbedHarnessEnvironment.loader(fixture);
    fixture.detectChanges();
  });

  it('Should create', () => {
    expect(component).toBeTruthy();
  })




});

