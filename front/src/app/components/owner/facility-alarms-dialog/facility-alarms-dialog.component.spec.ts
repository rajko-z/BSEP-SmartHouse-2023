import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FacilityAlarmsDialogComponent } from './facility-alarms-dialog.component';

describe('FacilityAlarmsDialogComponent', () => {
  let component: FacilityAlarmsDialogComponent;
  let fixture: ComponentFixture<FacilityAlarmsDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FacilityAlarmsDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FacilityAlarmsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
