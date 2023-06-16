import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {DeviceMessage} from 'src/app/model/deviceMessage';
import {InputForReportDTO} from 'src/app/model/inputForReportDTO';
import {ReportDataDTO} from 'src/app/model/reportDataDTO';
import {environment} from 'src/environments/environment';
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

  getReportData(inputForReportDTO: InputForReportDTO): Observable<ReportDataDTO>{
    let startDateStr = inputForReportDTO.dateInterval.startDate.toString();
    let endDateStr = inputForReportDTO.dateInterval.endDate.toString();

    let httpParams = new HttpParams()
      .append('startDate', startDateStr)
      .append('endDate', endDateStr);

      inputForReportDTO.deviceMessagePaths.forEach((path) => {
      httpParams = httpParams.append('deviceMessagesPaths', path);
    });

    return this.http.get<ReportDataDTO>(environment.backUrl + '/devices/get-report-data', { params: httpParams });
  }
}
