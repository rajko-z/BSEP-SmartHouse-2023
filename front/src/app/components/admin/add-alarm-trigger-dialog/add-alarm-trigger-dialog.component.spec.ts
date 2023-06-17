import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddAlarmTriggerDialogComponent } from './add-alarm-trigger-dialog.component';

describe('AddAlarmTriggerDialogComponent', () => {
  let component: AddAlarmTriggerDialogComponent;
  let fixture: ComponentFixture<AddAlarmTriggerDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddAlarmTriggerDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddAlarmTriggerDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
