import { FacilityData } from "./facilityData"

export class AddUserDTO {
    email: string
    firstName: string
    lastName: string
    password: string
    confirmPassword: string
    facilities: FacilityData[];
}