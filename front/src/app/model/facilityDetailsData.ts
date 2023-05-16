import { DeviceDTO } from "./deviceDTO"

export class FacilityDetailsData{
    name: string
    address: string
    facilityType: string
    owner: string
    tenantsEmails: string[]
    deviceDTOs: DeviceDTO[]
}