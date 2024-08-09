import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateArrangementComponent } from './create-arrangement.component';

describe('CreateArrangementComponent', () => {
  let component: CreateArrangementComponent;
  let fixture: ComponentFixture<CreateArrangementComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CreateArrangementComponent]
    });
    fixture = TestBed.createComponent(CreateArrangementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
