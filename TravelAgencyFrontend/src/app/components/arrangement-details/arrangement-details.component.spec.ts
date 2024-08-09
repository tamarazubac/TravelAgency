import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArrangementDetailsComponent } from './arrangement-details.component';

describe('ArrangementDetailsComponent', () => {
  let component: ArrangementDetailsComponent;
  let fixture: ComponentFixture<ArrangementDetailsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ArrangementDetailsComponent]
    });
    fixture = TestBed.createComponent(ArrangementDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
