import {Component, OnInit} from '@angular/core';
import {CSRRequestService} from "../../../services/csr_requests/csrrequest.service";
import {SimpleCSRRequest} from "../../../model/csrRequest";
import { over, Client, Message as StompMessage} from 'stompjs';
import { environment } from 'src/environments/environment';
import * as SockJS from 'sockjs-client';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-admin-csrrequests-page',
  templateUrl: './admin-csrrequests-page.component.html',
  styleUrls: ['./admin-csrrequests-page.component.scss']
})
export class AdminCSRRequestsPageComponent implements OnInit{
  private stompClient : Client;

  displayedColumns = ['email', 'timestamp', 'firstName', 'lastName'];

  csrRequests: SimpleCSRRequest[];

  constructor(
    private csrRequestsService: CSRRequestService,
    private toastrService: ToastrService
  ) {}

  ngOnInit(): void {
    this.loadData();
    let Sock = new SockJS(environment.backUrl + "/ws");
    this.stompClient = over(Sock);
    this.stompClient.connect({}, this.onConnected, () => {});
  }

  onConnected = () => {
    this.stompClient.subscribe("/alarm", (data:any) => this.onAlarmReceived(data));
  }

  onAlarmReceived(payload: StompMessage)
  {
    console.log(payload);
    let payloadData = JSON.parse(payload.body);
    if (payloadData.logActionType === 'INVALID_CREDENTIALS')
    {
      this.toastrService.error(`INVALID CREDENTIALS! ${payloadData.message}`);
    }

    else if(payloadData.logStatus === 'ERROR'){
      this.toastrService.error(`ERROR! ${payloadData.message}`);
    }

    else if(payloadData.logActionType === 'CREATING_NEW_CSR_REQUEST'){
      this.toastrService.error(`REDUNDANT CSR REQUEST! ${payloadData.message}`);
    }
  }

  private loadData() {
    this.csrRequestsService.getAllSimpleCSRRequestsData()
      .subscribe({
          next: (response) => {
            this.csrRequests = response;
          }
        }
      );
  }


}
