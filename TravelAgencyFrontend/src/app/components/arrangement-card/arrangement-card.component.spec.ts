import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArrangementCardComponent } from './arrangement-card.component';

describe('ArrangementCardComponent', () => {
  let component: ArrangementCardComponent;
  let fixture: ComponentFixture<ArrangementCardComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ArrangementCardComponent]
    });
    fixture = TestBed.createComponent(ArrangementCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
