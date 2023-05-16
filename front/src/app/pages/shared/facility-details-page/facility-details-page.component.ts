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
import { Client, over, Message as StompMessage } from 'stompjs';

@Component({
  selector: 'app-facility-details-page',
  templateUrl: './facility-details-page.component.html',
  styleUrls: ['./facility-details-page.component.scss']
})
export class FacilityDetailsPageComponent {
  private stompClient : Client;
  facilityName: string
  facilityData: FacilityDetailsData;
  deviceMessagesPaths: string[];
  deviceMessages: DeviceMessage[];
  tableColumns = ['message', 'messageType', 'timestamp', 'deviceStatus'];

  constructor(private route: ActivatedRoute, private location: Location, private facilityService: FacilityService, private toastrService: ToastrService, private deviceService: DeviceService) {}

  ngOnInit() {
    this.facilityName = this.route.snapshot.paramMap.get('facilityName') as string;
    this.getFacilityByName();
  }

  initializeWebSocket(){
    let Sock = new SockJS(environment.backUrl + "/ws");
    this.stompClient = over(Sock);
    this.stompClient.connect({}, this.onConnected, () => {});
  }

  onConnected = () => {
    this.stompClient.subscribe(`/${this.facilityName}/get-device-messages`, (message) => this.onDeviceMessageReceived(message));
  }

  onDeviceMessageReceived(payload: StompMessage)
  {
    let payloadData = JSON.parse(payload.body);
    let deviceMessage: DeviceMessage;
    deviceMessage = payloadData;
    this.deviceMessages.push(deviceMessage);
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
          console.log(this.deviceMessages);
          this.convertDateFormat();
          this.initializeWebSocket();
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
      console.log(this.deviceMessages[i].timestamp[0])
      let year = this.deviceMessages[i].timestamp[0];
      let month = this.deviceMessages[i].timestamp[1].toString().padStart(2, '0');
      let day = this.deviceMessages[i].timestamp[2].toString().padStart(2, '0');
      let hours = this.deviceMessages[i].timestamp[3].toString().padStart(2, '0');
      let minutes = this.deviceMessages[i].timestamp[4].toString().padStart(2, '0');
      let seconds = this.deviceMessages[i].timestamp[5] ? this.deviceMessages[i].timestamp[5].toString().padStart(2, '0') : "00";
      this.deviceMessages[i].formattedTimestamp = day + "/" + month + "/" + year + " " + hours + ":" + minutes + ":" + seconds;
    }
  }
}
