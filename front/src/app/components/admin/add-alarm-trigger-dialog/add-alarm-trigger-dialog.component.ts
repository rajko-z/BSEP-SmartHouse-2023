import {Component} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {ToastrService} from "ngx-toastr";
import {Router} from "@angular/router";
import {MatDialogRef} from "@angular/material/dialog";
import {AlarmService} from "../../../services/alarm/alarm.service";
import {DeviceInfo} from "../../../model/deviceDTO";
import {DeviceService} from "../../../services/device/device.service";
import {MatSelectChange} from "@angular/material/select";
import {NewAlarmDeviceTrigger} from "../../../model/DeviceAlarmTrigger";

@Component({
  selector: 'app-add-alarm-trigger-dialog',
  templateUrl: './add-alarm-trigger-dialog.component.html',
  styleUrls: ['./add-alarm-trigger-dialog.component.scss']
})
export class AddAlarmTriggerDialogComponent {

  deviceInfos: DeviceInfo[];

  devicesLoaded: boolean = false;

  selectedDevice: DeviceInfo | null = null;

  triggerForm = new FormGroup({
    alarmName: new FormControl('', [Validators.required]),
    selectedDevice: new FormControl<DeviceInfo | null>(null, [Validators.required]),
    invalidState: new FormControl('', [Validators.required]),
    sign: new FormControl('G', [Validators.required]),
    numberValue: new FormControl('0', [Validators.required, Validators.min(0), Validators.max(1000)])
  });

  constructor(
    private router: Router,
    private deviceAlarmTriggersService: AlarmService,
    private deviceService: DeviceService,
    private toastService: ToastrService,
    public dialogRef: MatDialogRef<AddAlarmTriggerDialogComponent>
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  private loadData() {
    this.deviceService.getAllDevicesInfo()
      .subscribe({
          next: (response) => {
            this.deviceInfos = response;
            console.log(response);
            this.devicesLoaded = true;
          }
        }
      );
  }

  onDeviceChange(event: MatSelectChange) {
    this.selectedDevice = event.value as DeviceInfo;
  }

  onSubmit() {
    if (this.triggerForm.controls.alarmName.invalid) {
      alert("Invalid alarm name");
      return;
    }

    if (this.selectedDevice == null) {
      alert("Please select device");
      return;
    }

    let selectedState: string | null = this.triggerForm.controls.invalidState.value;
    if (this.selectedDevice.hasState && (selectedState == null || !this.selectedDevice.invalidStates.includes(selectedState))) {
      alert("Please select valid state for device");
      return;
    }

    let value: number = 0;
    if (this.triggerForm.controls.numberValue.value !== null) {
      value = parseFloat(this.triggerForm.controls.numberValue.value);
    }

    if (!this.selectedDevice.hasState && (value == null || value > 1000 || value < 0)) {
      alert("Please select valid value");
      return;
    }

    let isGreater: boolean = this.triggerForm.controls.sign.value === 'G';

    let payload: NewAlarmDeviceTrigger = {
      alarmName: this.triggerForm.controls.alarmName.value as string,
      selectedDevice: this.selectedDevice.deviceType,
      hasState: this.selectedDevice.hasState,
      state: selectedState as string,
      greater: isGreater,
      value: value
    };

    console.log(typeof payload.value);
    console.log(payload);

    this.deviceAlarmTriggersService.addTrigger(payload)
      .subscribe({
          next: (response) => {
            this.toastService.success("New trigger is added");
            this.dialogRef.close();
          },
          error: (error) => {
            console.log(error)
            this.toastService.error(error.error.message);
          }
        }
      );
  }
}
