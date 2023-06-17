import {Component} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Location} from '@angular/common';
import {FacilityDetailsData} from 'src/app/model/facilityDetailsData';
import {FacilityService} from 'src/app/services/facility/facility.service';
import {ToastrService} from 'ngx-toastr';
import {DeviceMessage} from 'src/app/model/deviceMessage';
import {DeviceService} from 'src/app/services/device/device.service';
import {environment} from 'src/environments/environment';
import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';
import {MatTableDataSource} from '@angular/material/table';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {DateInterval} from 'src/app/model/dateInterval';
import {InputForReportDTO} from 'src/app/model/inputForReportDTO';
import {MatDialog} from '@angular/material/dialog';
import {ReportDialogComponent} from 'src/app/components/admin/report-dialog/report-dialog.component';
import {ReportDataDTO} from 'src/app/model/reportDataDTO';
import {
  FacilityAlarmsDialogComponent
} from "../../../components/owner/facility-alarms-dialog/facility-alarms-dialog.component";

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
  form: FormGroup;
  maxDate: Date = new Date();
  regExpr:any;

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private facilityService: FacilityService,
    private toastrService: ToastrService,
    private deviceService: DeviceService,
    private formBuilder: FormBuilder,
    private matDialog: MatDialog)
  {
    this.form = this.formBuilder.group({
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
    }, { validator: this.dateRangeValidator });
  }

  ngOnInit() {
    this.facilityName = this.route.snapshot.paramMap.get('facilityName') as string;
    this.getFacilityByName();
    this.initializeWebSocket();
  }

  dateRangeValidator(formGroup: FormGroup) {
    let startDate = formGroup.value['startDate'];
    let endDate = formGroup.value['endDate'];

    if (startDate && endDate && startDate > endDate) {
      return { dateRangeError: true };
    }

    return null;
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

  public onGenerateReport(): void {
    let dateInterval: DateInterval = this.form.value;
    let InputForReportDTO: InputForReportDTO = {
      dateInterval: dateInterval,
      deviceMessagePaths: this.deviceMessagesPaths
    };

    this.deviceService.getReportData(InputForReportDTO).subscribe(
      {
        next:(res: ReportDataDTO)=>{
          this.openReportDialog(res);
        },
        error:(err)=>{
          this.toastrService.warning("Something went wrong, please try again!");
        }
      }
    )
  }

  onDeviceMessageReceived(payload: any)
  {
    let payloadData = JSON.parse(payload.body);
    payloadData = this.convertDateFormatFromTimestamp(payloadData);
    let deviceMessage: DeviceMessage = payloadData;
    this.deviceMessages.push(deviceMessage);
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

  convertDateFormatFromTimestamp(deviceMessage:any){
      //2023-06-12T23:00:09.120998
      let date = deviceMessage['timestamp'].split("T")[0];
      let time = deviceMessage['timestamp'].split("T")[1];
      let day = date.split("-")[2];
      let month = date.split("-")[1];
      let year = date.split("-")[0];
      let hours = time.split(":")[0];
      let minutes = time.split(":")[1];
      let seconds = time.split(":")[2].split(".")[0];
      deviceMessage['formattedTimestamp'] = day + "/" + month + "/" + year + " " + hours + ":" + minutes + ":" + seconds;
      return deviceMessage;
  }


  openReportDialog(deviceMessages: ReportDataDTO) {
    const dialogRef = this.matDialog.open(ReportDialogComponent, {
      data: deviceMessages,
    });
  }

  applyFilter(event: Event) {
    try{
      const filterValue = (event.target as HTMLInputElement).value;
      const regex = new RegExp(filterValue);
      this.dataSource.filter = filterValue.trim().toLowerCase();
      this.dataSource.filterPredicate = (data: any, filter: string) => {
        return regex.test(data.message); // Replace 'columnName' with the actual column name you want to filter
      };

      // Manually trigger the filtering
      this.dataSource.filter = '';
      this.dataSource.filter = filterValue.trim().toLowerCase();
    }catch(error){
      return;
    }
  }

  openAlarmsDialog() {
    const dialogRef = this.matDialog.open(FacilityAlarmsDialogComponent, {
      data: this.facilityData.name
    });
  }
}
