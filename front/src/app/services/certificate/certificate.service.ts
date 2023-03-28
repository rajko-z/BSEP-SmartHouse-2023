import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Certificate } from 'src/app/model/certificate';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CertificateService {

  constructor(private http: HttpClient) { }

  getRemovedCertificates(): Observable<Certificate[]>
  {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("format", "json");
    return this.http.get<Certificate[]>(environment.backUrl + "/certificates/get-removed-certificates", { params: queryParams });
  }
}
