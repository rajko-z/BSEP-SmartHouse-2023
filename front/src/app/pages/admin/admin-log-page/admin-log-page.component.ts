import {Component} from '@angular/core';
import {Log} from "../../../model/logs";
import {LogServiceService} from "../../../services/log/log-service.service";
import {Client, Message as StompMessage, over} from "stompjs";
import * as SockJS from "sockjs-client";
import {environment} from "../../../../environments/environment";
import {MatTableDataSource} from "@angular/material/table";
import {MatSlideToggleChange} from "@angular/material/slide-toggle";

@Component({
  selector: 'app-admin-log-page',
  templateUrl: './admin-log-page.component.html',
  styleUrls: ['./admin-log-page.component.scss']
})
export class AdminLogPageComponent  {
  displayedColumns = ['status', 'timestamp', 'trace', 'action', 'message'];

  private stompClient : Client;

  logs: Log[] = [];

  dataSource = new MatTableDataSource(this.logs);

  searchString = "";

  regexActive = false;

  constructor(
    private logService: LogServiceService,
  ) {}

  ngOnInit(): void {
    this.loadData();
    let Sock = new SockJS(environment.backUrl + "/ws");
    this.stompClient = over(Sock);
    this.stompClient.connect({}, this.onConnected, () => {});
  }

  onConnected = () => {
    this.stompClient.subscribe("/new-log", (data:any) => this.addNewLog(data));
  }

  addNewLog(payload: StompMessage) {
    let newLog = JSON.parse(payload.body) as Log;
    this.logs.push(newLog);
    this.dataSource = new MatTableDataSource(this.logs);
  }

  private loadData() {
    this.logService.getAllLogs()
      .subscribe({
          next: (response) => {
            this.logs = response;
            this.dataSource = new MatTableDataSource(this.logs);
          }
        }
      );
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  applyFilterWithRegex(event: Event) {
    try {
      const filterValue = (event.target as HTMLInputElement).value;
      const regex = new RegExp(filterValue);

      this.dataSource.filter = filterValue.trim().toLowerCase();
      this.dataSource.filterPredicate = (data: any, filter: string) => {
        return regex.test(data.message);
      };

      this.dataSource.filter = '';
      this.dataSource.filter = filterValue.trim().toLowerCase();
    } catch (error) {
      return;
    }
  }

  onRegexActiveChange($event: MatSlideToggleChange) {
    this.regexActive = $event.checked;
    this.searchString = "";
    this.dataSource.filter = this.searchString.trim().toLowerCase();
  }
}
