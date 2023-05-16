import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { FacilityDetailsData } from 'src/app/model/facilityDetailsData';
import { FacilityService } from 'src/app/services/facility/facility.service';
import { ToastrService } from 'ngx-toastr';
import { DeviceMessage } from 'src/app/model/deviceMessage';
import { DeviceService } from 'src/app/services/device/device.service';

@Component({
  selector: 'app-facility-details-page',
  templateUrl: './facility-details-page.component.html',
  styleUrls: ['./facility-details-page.component.scss']
})
export class FacilityDetailsPageComponent {
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
}
