import {DeviceType} from "./DeviceAlarmTrigger";

export interface DeviceInfo {
  deviceType: DeviceType,
  hasState: boolean,
  invalidStates: string[]
}

export class DeviceDTO{
    id: number
    deviceType: string
    messagesFilePath: string
}
