import {Component, Inject} from '@angular/core';
import {Client, Message as StompMessage, over} from "stompjs";
import {ActivatedDeviceAlarm, AlarmNotificationForUser} from "../../../model/activatedAlarms";
import {MatTableDataSource} from "@angular/material/table";
import {AlarmService} from "../../../services/alarm/alarm.service";
import * as SockJS from "sockjs-client";
import {environment} from "../../../../environments/environment";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {AuthService} from "../../../services/auth/auth.service";

@Component({
  selector: 'app-facility-alarms-dialog',
  templateUrl: './facility-alarms-dialog.component.html',
  styleUrls: ['./facility-alarms-dialog.component.scss']
})
export class FacilityAlarmsDialogComponent {

  displayedColumns = ['alarmName', 'timestamp', 'deviceType', 'message', 'facility'];

  private stompClient : Client;

  activatedDeviceAlarms: ActivatedDeviceAlarm[] = [];

  dataSource = new MatTableDataSource(this.activatedDeviceAlarms);

  constructor(
    private alarmService: AlarmService,
    public dialogRef: MatDialogRef<FacilityAlarmsDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public facilityName: string,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadData();
    let Sock = new SockJS(environment.backUrl + "/ws");
    this.stompClient = over(Sock);
    this.stompClient.connect({}, this.onConnected, () => {});
  }

  onConnected = () => {
    this.stompClient.subscribe("/activated-device-alarm-for-user", (data:any) => this.showDeviceAlarmForUser(data));
  }

  showDeviceAlarmForUser(payload: StompMessage) {
    let notification = JSON.parse(payload.body) as AlarmNotificationForUser;
    let newActivatedDeviceAlarm: ActivatedDeviceAlarm = notification.alarm;

    let currentUser = this.authService.getCurrentUser();
    if (currentUser != null && currentUser.email === notification.userEmail) {
      this.activatedDeviceAlarms.unshift(newActivatedDeviceAlarm);
      this.dataSource = new MatTableDataSource(this.activatedDeviceAlarms);
    }
  }

  private loadData() {
    this.alarmService.getAllActivatedDeviceAlarmsByFacilityName(this.facilityName)
      .subscribe({
          next: (response) => {
            this.activatedDeviceAlarms = response;
            console.log(this.activatedDeviceAlarms)
            this.dataSource = new MatTableDataSource(this.activatedDeviceAlarms);
          }
        }
      );
  }
}
