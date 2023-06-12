import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DeviceMessage } from 'src/app/model/deviceMessage';
import { environment } from 'src/environments/environment';
import {DeviceAlarmTrigger} from "../../model/DeviceAlarmTrigger";
import {DeviceInfo} from "../../model/deviceDTO";

@Injectable({
  providedIn: 'root'
})
export class DeviceService {

  constructor(private http: HttpClient) { }

  getDeviceMessages(deviceMessagesPaths: string[]): Observable<DeviceMessage[]> {
    let httpParams = new HttpParams();
    deviceMessagesPaths.forEach((path) => {
      httpParams = httpParams.append('deviceMessagesPaths', path);
    });
    return this.http.get<DeviceMessage[]>(environment.backUrl + '/devices/', { params: httpParams });
  }

  getAllDevicesInfo() {
    return this.http.get<DeviceInfo[]>(environment.backUrl + '/devices/infos');
  }

}
