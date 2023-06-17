import { DeviceDTO } from "./deviceDTO"

export class FacilityDetailsData{
    name: string
    address: string
    facilityType: string
    owner: string
    ownerEmail: string
    tenantsEmails: string[]
    deviceDTOs: DeviceDTO[]
}
