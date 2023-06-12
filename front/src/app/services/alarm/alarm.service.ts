import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {DeviceAlarmTrigger} from "../../model/DeviceAlarmTrigger";

@Injectable({
  providedIn: 'root'
})
export class AlarmService {

  constructor(private http: HttpClient) { }

  getAllDeviceAlarmTriggers() {
    return this.http.get<DeviceAlarmTrigger[]>(environment.backUrl + '/device-alarm-triggers');
  }
}
