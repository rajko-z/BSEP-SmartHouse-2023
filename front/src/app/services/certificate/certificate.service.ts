import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {CertificateData} from 'src/app/model/certificate';
import {environment} from 'src/environments/environment';
import {CustomHttpService} from "../custom-http/custom-http.service";
import {NewCertificate} from "../../model/newCertificate";
import {TextResponse} from "../../model/textResponse";

@Injectable({
  providedIn: 'root'
})
export class CertificateService {

  constructor(private customHttp: CustomHttpService, private http: HttpClient) { }

  getRevokedCertificatesSerialNumbers(): Observable<String[]> {
    return this.customHttp.getT<String[]>(environment.backUrl + "/certificates/get-revoked-certificates");
  }

  getAllCertificates(): Observable<CertificateData[]> {
    return this.customHttp.getT<CertificateData[]>(environment.backUrl + "/certificates/get-all-certificates");
  }

  verifyCertificate(certificateSerialNumber: string): Observable<string> {
    let headers = this.customHttp.createHeader();
    let queryParams = new HttpParams();
    queryParams = queryParams.append("certificateSerialNumber", certificateSerialNumber);
    return this.http.post<string>(environment.backUrl + "/certificates/verify-certificate", null, {
      params: queryParams,
      headers: headers
    });
  }

  revokeCertificate(certificateSerialNumber: string, reasonForRevoking: string): Observable<string> {
    let headers = this.customHttp.createHeader();
    let queryParams = new HttpParams();
    queryParams = queryParams.append("certificateSerialNumber", certificateSerialNumber);
    queryParams = queryParams.append("reasonForRevoking", reasonForRevoking);
    queryParams = queryParams.append("format", "json");
    return this.http.post<string>(environment.backUrl + "/certificates/revoke-certificate", null, { params: queryParams, headers: headers });
  }

  issueCertificate(data: NewCertificate) {
    return this.customHttp.putT<TextResponse>(environment.backUrl + "/certificates/issue-certificate", data);
  }
}
