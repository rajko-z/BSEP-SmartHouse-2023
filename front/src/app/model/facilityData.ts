export class FacilityData {
    name: string
    address: string
    facilityType: string
    tenantsEmails: string[]

    constructor(name: string, address: string, facilityType: string, tenantsEmails: string[])
    {
        this.name = name;
        this.facilityType = facilityType;
        this.address = address;
        this.tenantsEmails = tenantsEmails;
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

    public getAddress()
    {
        return this.address;
    }

    public setAddress(address: string)
    {
        this.address = address;
    }
}
  