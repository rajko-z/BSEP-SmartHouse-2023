import { Injectable } from '@angular/core';
import {CustomHttpService} from "../custom-http/custom-http.service";
import {environment} from "../../../environments/environment";
import {CSRRequestData, RejectCSR, SimpleCSRRequest} from "../../model/csrRequest";
import {TextResponse} from "../../model/textResponse";

@Injectable({
  providedIn: 'root'
})
export class CSRRequestService {

  constructor(private httpService: CustomHttpService) { }

  getAllSimpleCSRRequestsData() {
    return this.httpService.getT<SimpleCSRRequest[]>(environment.backUrl + '/csrrequests');
  }

  getCSRRequestDataForEmail(email:string) {
    return this.httpService.getT<CSRRequestData>(environment.backUrl + '/csrrequests/' + email);
  }

  rejectCSRRequest(rejectCSR: RejectCSR) {
    return this.httpService.deleteT<TextResponse>(environment.backUrl + '/csrrequests/reject', rejectCSR);
  }
}
