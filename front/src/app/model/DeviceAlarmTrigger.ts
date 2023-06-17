export enum DeviceType {
  THERMOMETER= 'THERMOMETER',
  GATE= 'GATE',
  COOKER = 'COOKER',
  WATER_HEATER = 'WATER_HEATER',
  DOOR = 'DOOR',
  LIGHT = 'LIGHT',
  VACUUM_CLEANER = 'VACUUM_CLEANER',
  AIR_CONDITIONING = 'AIR_CONDITIONING',
  CAMERA = 'CAMERA',
  ALARM = 'ALARM'
}

export class DeviceAlarmTrigger {
  alarmName: string;
  deviceType: DeviceType;
  lowerThan: number;
  higherThan: number;
  invalidState: string;
  ruleName: string;
}
export interface NewAlarmDeviceTrigger {
  alarmName: string;
  selectedDevice: DeviceType;
  hasState: boolean;
  state: string;
  greater: boolean;
  value: number;
}
