import {Component, OnInit} from '@angular/core';
import {CSRRequestService} from "../../../services/csr_requests/csrrequest.service";
import {SimpleCSRRequest} from "../../../model/csrRequest";

@Component({
  selector: 'app-admin-csrrequests-page',
  templateUrl: './admin-csrrequests-page.component.html',
  styleUrls: ['./admin-csrrequests-page.component.scss']
})
export class AdminCSRRequestsPageComponent implements OnInit{

  displayedColumns = ['email', 'timestamp', 'firstName', 'lastName'];

  csrRequests: SimpleCSRRequest[];

  constructor(
    private csrRequestsService: CSRRequestService
  ) {}

  ngOnInit(): void {
    this.loadData();
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
