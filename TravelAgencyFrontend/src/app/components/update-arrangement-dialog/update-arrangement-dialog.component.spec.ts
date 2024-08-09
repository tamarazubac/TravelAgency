import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateArrangementDialogComponent } from './update-arrangement-dialog.component';

describe('UpdateArrangementDialogComponent', () => {
  let component: UpdateArrangementDialogComponent;
  let fixture: ComponentFixture<UpdateArrangementDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UpdateArrangementDialogComponent]
    });
    fixture = TestBed.createComponent(UpdateArrangementDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
