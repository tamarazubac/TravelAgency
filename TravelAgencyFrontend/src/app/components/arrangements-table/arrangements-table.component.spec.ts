import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArrangementsTableComponent } from './arrangements-table.component';

describe('ArrangementsTableComponent', () => {
  let component: ArrangementsTableComponent;
  let fixture: ComponentFixture<ArrangementsTableComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ArrangementsTableComponent]
    });
    fixture = TestBed.createComponent(ArrangementsTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
