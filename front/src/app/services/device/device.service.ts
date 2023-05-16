import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DeviceMessage } from 'src/app/model/deviceMessage';
import { environment } from 'src/environments/environment';

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

}
