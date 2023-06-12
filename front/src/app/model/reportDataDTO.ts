import { DeviceMessage } from "./deviceMessage";

export class ReportDataDTO{
    deviceMessages: DeviceMessage[]
    sumInfoMessages: number
    sumWarningMessages: number
    sumErrorMessages: number
}