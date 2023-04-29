import {Injectable} from '@angular/core';
import {environment} from "../../../environments/environment";
import {CSRRequestData, RejectCSR, SimpleCSRRequest} from "../../model/csrRequest";
import {TextResponse} from "../../model/textResponse";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class CSRRequestService {

  constructor(private http: HttpClient) { }

  getAllSimpleCSRRequestsData() {
    return this.http.get<SimpleCSRRequest[]>(environment.backUrl + '/csrrequests');
  }

  getCSRRequestDataForEmail(email:string) {
    return this.http.get<CSRRequestData>(environment.backUrl + '/csrrequests/' + email);
  }

  rejectCSRRequest(rejectCSR: RejectCSR) {
    return this.http.delete<TextResponse>(environment.backUrl + '/csrrequests/reject', {body: rejectCSR});
  }
}
