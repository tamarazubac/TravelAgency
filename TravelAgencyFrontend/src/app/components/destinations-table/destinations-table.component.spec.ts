import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DestinationsTableComponent } from './destinations-table.component';

describe('DestinationsTableComponent', () => {
  let component: DestinationsTableComponent;
  let fixture: ComponentFixture<DestinationsTableComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DestinationsTableComponent]
    });
    fixture = TestBed.createComponent(DestinationsTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
