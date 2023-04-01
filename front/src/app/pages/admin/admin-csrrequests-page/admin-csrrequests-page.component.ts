import {Component, OnInit} from '@angular/core';
import {SimpleCSRRequest} from "../../../model/csr_requests";
import {CSRRequestService} from "../../../services/csr_requests/csrrequest.service";

@Component({
  selector: 'app-admin-csrrequests-page',
  templateUrl: './admin-csrrequests-page.component.html',
  styleUrls: ['./admin-csrrequests-page.component.scss']
})
export class AdminCSRRequestsPageComponent implements OnInit{

  displayedColumns = ['id', 'timestamp', 'email', 'firstName', 'lastName'];

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
