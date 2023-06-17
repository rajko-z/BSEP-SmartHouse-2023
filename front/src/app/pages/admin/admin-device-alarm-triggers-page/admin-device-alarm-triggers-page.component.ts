import {Component} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {DeviceAlarmTrigger} from "../../../model/DeviceAlarmTrigger";
import {AlarmService} from "../../../services/alarm/alarm.service";
import {ChangePasswordComponent} from "../../../components/shared/change-password/change-password.component";
import {MatDialog} from "@angular/material/dialog";
import {
  AddAlarmTriggerDialogComponent
} from "../../../components/admin/add-alarm-trigger-dialog/add-alarm-trigger-dialog.component";

@Component({
  selector: 'app-admin-device-alarm-triggers-page',
  templateUrl: './admin-device-alarm-triggers-page.component.html',
  styleUrls: ['./admin-device-alarm-triggers-page.component.scss']
})
export class AdminDeviceAlarmTriggersPageComponent {

  displayedColumns = ['alarmName', 'deviceType', 'ruleName'];

  deviceAlarmTriggers: DeviceAlarmTrigger[] = [];

  dataSource = new MatTableDataSource(this.deviceAlarmTriggers);

  constructor(
    private deviceAlarmTriggersService: AlarmService,
    private matDialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  private loadData() {
    this.deviceAlarmTriggersService.getAllDeviceAlarmTriggers()
      .subscribe({
          next: (response) => {
            this.deviceAlarmTriggers = response;
            this.dataSource = new MatTableDataSource(this.deviceAlarmTriggers);
          }
        }
      );
  }

  addAlarmTrigger() {
    let dialogRef = this.matDialog.open(AddAlarmTriggerDialogComponent);
    dialogRef.afterClosed().subscribe(() => { this.loadData(); })
  }
}
