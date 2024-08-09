import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RateCardComponent } from './rate-card.component';

describe('RateCardComponent', () => {
  let component: RateCardComponent;
  let fixture: ComponentFixture<RateCardComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RateCardComponent]
    });
    fixture = TestBed.createComponent(RateCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
