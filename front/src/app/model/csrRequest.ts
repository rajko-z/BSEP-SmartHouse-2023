export interface CSRRequest{
    email:string,
    firstName:string,
    lastName:string
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

export class SimpleCSRRequest {
  email: string;
  firstName: string;
  lastName: string;
  timestamp: string;
}
