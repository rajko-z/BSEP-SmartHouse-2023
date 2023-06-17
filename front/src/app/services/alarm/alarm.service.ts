import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {DeviceAlarmTrigger, NewAlarmDeviceTrigger} from "../../model/DeviceAlarmTrigger";
import {ActivatedDeviceAlarm} from "../../model/activatedAlarms";

@Injectable({
  providedIn: 'root'
})
export class AlarmService {

  constructor(private http: HttpClient) { }

  getAllDeviceAlarmTriggers() {
    return this.http.get<DeviceAlarmTrigger[]>(environment.backUrl + '/device-alarm-triggers');
  }

  addTrigger(payload: NewAlarmDeviceTrigger) {
    return this.http.post(environment.backUrl + '/device-alarm-triggers', payload);
  }

  getAllActivatedDeviceAlarms() {
    return this.http.get<ActivatedDeviceAlarm[]>(environment.backUrl + '/activated-alarms');
  }

  getAllActivatedDeviceAlarmsByFacilityName(facility: string) {
    return this.http.get<ActivatedDeviceAlarm[]>(environment.backUrl + "/activated-alarms/" + facility)
  }
}
