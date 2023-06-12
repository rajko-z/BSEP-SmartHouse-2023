import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ReportDataDTO } from 'src/app/model/reportDataDTO';

@Component({
  selector: 'app-report-dialog',
  templateUrl: './report-dialog.component.html',
  styleUrls: ['./report-dialog.component.scss']
})
export class ReportDialogComponent {

  tableColumns = ['message', 'messageType', 'timestamp', 'deviceStatus'];

  deviceMessages: any;
  sumInfoMessages: any;
  sumWarningMessages: any;
  sumErrorMessages: any;

  constructor(public dialogRef: MatDialogRef<ReportDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: any){
    
    this.deviceMessages = data.deviceMessages;
    this.sumInfoMessages = data.sumInfoMessages;
    this.sumWarningMessages = data.sumWarningMessages;
    this.sumErrorMessages = data.sumErrorMessages;
  }

  onConfirm(){
    this.dialogRef.close();
  }
}
