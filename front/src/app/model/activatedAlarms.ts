import {DeviceAlarmTrigger} from "./DeviceAlarmTrigger";

export interface ActivatedDeviceAlarm {
  deviceAlarmTrigger: DeviceAlarmTrigger;
  timestamp: string;
  message: string;
  facilityName: string;
}
export interface AlarmNotificationForUser {
  alarm: ActivatedDeviceAlarm;
  userEmail: string;
}
