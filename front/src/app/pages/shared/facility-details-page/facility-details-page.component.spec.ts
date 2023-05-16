import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FacilityDetailsPageComponent } from './facility-details-page.component';

describe('FacilityDetailsPageComponent', () => {
  let component: FacilityDetailsPageComponent;
  let fixture: ComponentFixture<FacilityDetailsPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FacilityDetailsPageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FacilityDetailsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
