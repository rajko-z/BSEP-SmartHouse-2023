export interface CSRRequest{
    email:string,
    firstName:string,
    lastName:string,

    filePath:string,

}

export class CSRRequestData {
  commonName: string;
  organizationUnit: string;
  organizationName: string;
  localityName: string;
  stateName: string;
  countryName: string;
  publicKey: string;
  signature: string;
  validSignature: boolean;
}

export class RejectCSR {
  email: string;
  reason?: string;
}
