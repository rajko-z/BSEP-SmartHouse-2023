import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {CertificateData} from 'src/app/model/certificate';
import {environment} from 'src/environments/environment';
import {NewCertificate} from "../../model/newCertificate";
import {TextResponse} from "../../model/textResponse";

@Injectable({
  providedIn: 'root'
})
export class CertificateService {

  constructor(private http: HttpClient) { }

  getRevokedCertificatesSerialNumbers(): Observable<String[]> {
    return this.http.get<String[]>(environment.backUrl + "/certificates/get-revoked-certificates");
  }

  getAllCertificates(): Observable<CertificateData[]> {
    return this.http.get<CertificateData[]>(environment.backUrl + "/certificates/get-all-certificates");
  }

  verifyCertificate(certificateSerialNumber: string): Observable<string> {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("certificateSerialNumber", certificateSerialNumber);
    return this.http.post<string>(environment.backUrl + "/certificates/verify-certificate", null, {
      params: queryParams
    });
  }

  revokeCertificate(certificateSerialNumber: number, reasonForRevoking: string): Observable<string> {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("certificateSerialNumber", certificateSerialNumber);
    queryParams = queryParams.append("reasonForRevoking", reasonForRevoking);
    return this.http.post<string>(environment.backUrl + "/certificates/revoke-certificate", null, { params: queryParams });
  }

  issueCertificate(data: NewCertificate) {
    return this.http.put<TextResponse>(environment.backUrl + "/certificates/issue-certificate", data);
  }
}
