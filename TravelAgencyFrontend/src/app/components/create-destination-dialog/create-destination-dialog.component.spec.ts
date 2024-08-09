import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateDestinationDialogComponent } from './create-destination-dialog.component';

describe('CreateDestinationDialogComponent', () => {
  let component: CreateDestinationDialogComponent;
  let fixture: ComponentFixture<CreateDestinationDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CreateDestinationDialogComponent]
    });
    fixture = TestBed.createComponent(CreateDestinationDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
