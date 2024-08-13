import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DateRangeDialogComponent } from './date-range-dialog.component';

describe('DateRangeDialogComponent', () => {
  let component: DateRangeDialogComponent;
  let fixture: ComponentFixture<DateRangeDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DateRangeDialogComponent]
    });
    fixture = TestBed.createComponent(DateRangeDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
