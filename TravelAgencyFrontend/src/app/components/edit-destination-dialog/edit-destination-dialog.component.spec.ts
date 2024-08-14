import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditDestinationDialogComponent } from './edit-destination-dialog.component';

describe('EditDestinationDialogComponent', () => {
  let component: EditDestinationDialogComponent;
  let fixture: ComponentFixture<EditDestinationDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EditDestinationDialogComponent]
    });
    fixture = TestBed.createComponent(EditDestinationDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
