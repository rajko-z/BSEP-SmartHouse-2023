import {Component} from '@angular/core';
import {Client, Message as StompMessage, over} from "stompjs";
import {MatTableDataSource} from "@angular/material/table";
import * as SockJS from "sockjs-client";
import {environment} from "../../../../environments/environment";
import {ActivatedDeviceAlarm} from "../../../model/activatedAlarms";
import {AlarmService} from "../../../services/alarm/alarm.service";

@Component({
  selector: 'app-admin-activated-device-alarms-page',
  templateUrl: './admin-activated-device-alarms-page.component.html',
  styleUrls: ['./admin-activated-device-alarms-page.component.scss']
})
export class AdminActivatedDeviceAlarmsPageComponent {

  displayedColumns = ['alarmName', 'timestamp', 'deviceType', 'message', 'facility'];

  private stompClient : Client;

  activatedDeviceAlarms: ActivatedDeviceAlarm[] = [];

  dataSource = new MatTableDataSource(this.activatedDeviceAlarms);

  constructor(
    private alarmService: AlarmService,
  ) {}

  ngOnInit(): void {
    this.loadData();
    let Sock = new SockJS(environment.backUrl + "/ws");
    this.stompClient = over(Sock);
    this.stompClient.connect({}, this.onConnected, () => {});
  }

  onConnected = () => {
    this.stompClient.subscribe("/activated-device-alarm", (data:any) => this.addNewActivatedDeviceAlarm(data));
  }

  addNewActivatedDeviceAlarm(payload: StompMessage) {
    let newActivatedDeviceAlarm = JSON.parse(payload.body) as ActivatedDeviceAlarm;
    this.activatedDeviceAlarms.unshift(newActivatedDeviceAlarm);
    this.dataSource = new MatTableDataSource(this.activatedDeviceAlarms);
  }

  private loadData() {
    this.alarmService.getAllActivatedDeviceAlarms()
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
