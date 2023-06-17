import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminDeviceAlarmTriggersPageComponent } from './admin-device-alarm-triggers-page.component';

describe('AdminDeviceAlarmTriggersPageComponent', () => {
  let component: AdminDeviceAlarmTriggersPageComponent;
  let fixture: ComponentFixture<AdminDeviceAlarmTriggersPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminDeviceAlarmTriggersPageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminDeviceAlarmTriggersPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
