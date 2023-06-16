import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminActivatedDeviceAlarmsPageComponent } from './admin-activated-device-alarms-page.component';

describe('AdminActivatedDeviceAlarmsPageComponent', () => {
  let component: AdminActivatedDeviceAlarmsPageComponent;
  let fixture: ComponentFixture<AdminActivatedDeviceAlarmsPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminActivatedDeviceAlarmsPageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminActivatedDeviceAlarmsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
