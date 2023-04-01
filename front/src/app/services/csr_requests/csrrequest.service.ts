import { Injectable } from '@angular/core';
import {CustomHttpService} from "../custom-http/custom-http.service";
import {SimpleCSRRequest} from "../../model/csr_requests";
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class CSRRequestService {

  constructor(private httpService: CustomHttpService) { }

  getAllSimpleCSRRequestsData() {
    return this.httpService.getT<SimpleCSRRequest[]>(environment.backUrl + 'csrrequests');
  }

}
