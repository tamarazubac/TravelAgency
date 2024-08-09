import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditRolesDialogComponent } from './edit-roles-dialog.component';

describe('EditRolesDialogComponent', () => {
  let component: EditRolesDialogComponent;
  let fixture: ComponentFixture<EditRolesDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EditRolesDialogComponent]
    });
    fixture = TestBed.createComponent(EditRolesDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
