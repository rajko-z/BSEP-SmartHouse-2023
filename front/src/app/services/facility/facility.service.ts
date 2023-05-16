import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FacilityDetailsData } from 'src/app/model/facilityDetailsData';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class FacilityService {

  constructor(private http: HttpClient) {}

  getFacilityByName(facilityName: string): Observable<FacilityDetailsData> {
    let httpParams = new HttpParams().append("facilityName", facilityName);
    return this.http.get<FacilityDetailsData>(environment.backUrl + "/facilities/" + facilityName, {params:httpParams});
  }
}
