import { AddUserDTO } from "./addUserDTO";
import { FacilityData } from "./facilityData";

export interface SanitizedAddUserDTO extends AddUserDTO {
    [key: string]: string | FacilityData[];
}