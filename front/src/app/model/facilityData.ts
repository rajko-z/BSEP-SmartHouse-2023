export class FacilityData {
    name: string
    facilityType: string

    constructor(name: string, facilityType: string)
    {
        this.name = name;
        this.facilityType = facilityType;
    }

    public getName()
    {
        return this.name;
    }

    public getFacilityType()
    {
        return this.facilityType;
    }

    public setName(name: string)
    {
        this.name = name;
    }

    public setFacilityType(facilityType: string)
    {
        this.facilityType = facilityType;
    }
}
  