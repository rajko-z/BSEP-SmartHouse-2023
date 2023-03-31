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

  getRevokedCertificatesSerialNumbers(): Observable<String[]>
  {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("format", "json");
    return this.http.get<String[]>(environment.backUrl + "/certificates/get-revoked-certificates", { params: queryParams });
  }

  getAllCertificates(): Observable<CertificateData[]>
  {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("format", "json");
    return this.http.get<CertificateData[]>(environment.backUrl + "/certificates/get-all-certificates", { params: queryParams });
  }

  verifyCertificate(certificateSerialNumber: string): Observable<string> {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("certificateSerialNumber", certificateSerialNumber);
    queryParams = queryParams.append("format", "json");
    return this.http.post<string>(environment.backUrl + "/certificates/verify-certificate", null, { params: queryParams });
  }

  revokeCertificate(certificateSerialNumber: string, reasonForRevoking: string): Observable<string> {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("certificateSerialNumber", certificateSerialNumber);
    queryParams = queryParams.append("reasonForRevoking", reasonForRevoking);
    queryParams = queryParams.append("format", "json");
    return this.http.post<string>(environment.backUrl + "/certificates/revoke-certificate", null, { params: queryParams });
  }
}
