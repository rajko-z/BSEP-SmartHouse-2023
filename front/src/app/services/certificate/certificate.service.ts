import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CertificateData } from 'src/app/model/certificate';
import { RemovedCertificate } from 'src/app/model/removedCertificate';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CertificateService {

  constructor(private http: HttpClient) { }

  getRemovedCertificates(): Observable<RemovedCertificate[]>
  {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("format", "json");
    return this.http.get<RemovedCertificate[]>(environment.backUrl + "certificates/get-removed-certificates", { params: queryParams });
  }

  getAllCertificates(): Observable<CertificateData[]>
  {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("format", "json");
    return this.http.get<CertificateData[]>(environment.backUrl + "certificates/get-all-certificates", { params: queryParams });
  }

  verifyCertificate(certificateSerialNumber: number): Observable<String>
  {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("certificateSerialNumber", certificateSerialNumber);
    queryParams = queryParams.append("format", "json");
    return this.http.post<String>(environment.backUrl + "certificates/verify-certificate", { params: queryParams });
  }
}
