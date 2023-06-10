import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { FacilityDetailsData } from 'src/app/model/facilityDetailsData';
import { FacilityService } from 'src/app/services/facility/facility.service';
import { ToastrService } from 'ngx-toastr';
import { DeviceMessage } from 'src/app/model/deviceMessage';
import { DeviceService } from 'src/app/services/device/device.service';
import { environment } from 'src/environments/environment';
import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';
import { Client, over, Message as StompMessage } from 'stompjs';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-facility-details-page',
  templateUrl: './facility-details-page.component.html',
  styleUrls: ['./facility-details-page.component.scss']
})
export class FacilityDetailsPageComponent {
  private stompClient : any;
  searchString = "";
  facilityName: string
  facilityData: FacilityDetailsData;
  deviceMessagesPaths: string[];
  deviceMessages: DeviceMessage[] = [];
  dataSource = new MatTableDataSource(this.deviceMessages);
  tableColumns = ['message', 'messageType', 'timestamp', 'deviceStatus'];
  regExpr:any;

  constructor(private route: ActivatedRoute, private location: Location, private facilityService: FacilityService, private toastrService: ToastrService, private deviceService: DeviceService) {}

  ngOnInit() {
    this.facilityName = this.route.snapshot.paramMap.get('facilityName') as string;
    this.getFacilityByName();
    this.initializeWebSocket();
  }

  initializeWebSocket(){
    let Sock = new SockJS(environment.backUrl + "/ws");
    this.stompClient = Stomp.over(Sock);

    let that = this;

    this.stompClient.connect({}, that.onConnected, that.onError); 
  }

  onConnected = () => {
    this.stompClient.subscribe(`/device-messages/new-message/${this.facilityName}`, (message:any) => this.onDeviceMessageReceived(message));
  }

  onError = () => {
    console.log("Socket error.");    
  }

  onDeviceMessageReceived(payload: any)
  {
    let payloadData = JSON.parse(payload.body);
    let deviceMessage: DeviceMessage;
    deviceMessage = payloadData;
    this.deviceMessages.push(deviceMessage);
    this.convertDateFormat();
    this.dataSource = new MatTableDataSource(this.deviceMessages);

  }

  getFacilityByName(){
    this.facilityService.getFacilityByName(this.facilityName).subscribe(
      {
        next:(res)=>{
          this.facilityData = res;
          this.capitalizeDeviceTypes();
          this.deviceMessagesPaths = this.fillDeviceMessagesPath();
          this.getDeviceMessages();
        },
        error:(err)=>{
          this.toastrService.warning("Something went wrong, please try again!");
        }
      }
    );
  }

  getDeviceMessages(){
    this.deviceService.getDeviceMessages(this.deviceMessagesPaths).subscribe(
      {
        next:(res)=>{
          this.deviceMessages = res;
          this.convertDateFormat();
          this.dataSource = new MatTableDataSource(this.deviceMessages);
        },
        error:(err)=>{
          this.toastrService.warning("Something went wrong, please try again!");
        }
      }
    );
  }

  fillDeviceMessagesPath(): string[]{
    let deviceMessagesPaths: string[] = [];
    for(let deviceDTO of this.facilityData.deviceDTOs){
      deviceMessagesPaths.push(deviceDTO.messagesFilePath);
    }
    return deviceMessagesPaths;
  }

  goBack(): void {
    this.location.back();
  }

  capitalizeDeviceTypes(){
    for(let deviceDTO of this.facilityData.deviceDTOs){
      deviceDTO.deviceType = this.capitalizeWord(deviceDTO.deviceType);
      deviceDTO.deviceType = deviceDTO.deviceType.replace(/_/g, ' ');
    }
    this.facilityData.facilityType = this.capitalizeWord(this.facilityData.facilityType);
  }

  capitalizeWord(word: string | undefined): string {
    if(!word)
    {
      return '';
    }
    return word.charAt(0) + word.slice(1).toLowerCase();
  }

  convertDateFormat(){
    for(let i = 0; i < this.deviceMessages.length; i++)
    {
      let year = this.deviceMessages[i].timestamp[0];
      let month = this.deviceMessages[i].timestamp[1].toString().padStart(2, '0');
      let day = this.deviceMessages[i].timestamp[2].toString().padStart(2, '0');
      let hours = this.deviceMessages[i].timestamp[3].toString().padStart(2, '0');
      let minutes = this.deviceMessages[i].timestamp[4].toString().padStart(2, '0');
      let seconds = this.deviceMessages[i].timestamp[5] ? this.deviceMessages[i].timestamp[5].toString().padStart(2, '0') : "00";
      this.deviceMessages[i].formattedTimestamp = day + "/" + month + "/" + year + " " + hours + ":" + minutes + ":" + seconds;
    }
  }

  applyFilter(event: Event) {
    try{
    // this.dataSource.filter = filterValue.trim().toLowerCase();
    const filterValue = (event.target as HTMLInputElement).value;
    console.log(filterValue);
    const regex = new RegExp(filterValue);
    // const regex = new RegExp('\\d');
    console.log(regex);
    this.dataSource.filter = filterValue.trim().toLowerCase();
    this.dataSource.filterPredicate = (data: any, filter: string) => {
      return regex.test(data.message); // Replace 'columnName' with the actual column name you want to filter
    };
    }catch(error){
      return;
    }
    

  }
}
