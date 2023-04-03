
export class SubjectData {
  email: string;
  commonName: string;
  organizationUnit: string;
  organizationName: string;
  localityName: string;
  stateName: string;
  countryName: string;
}

export enum TemplateType {
  SSL_SERVER, SSL_CLIENT
}

export enum IntermediateCA {
  SMARTHOUSE2023FirstICA, SMARTHOUSE2023SecondICA
}

export class KeyUsage {
  certificateSigning: boolean;
  decipherOnly: boolean;
  keyAgreement: boolean;
  crlSign: boolean;
  digitalSignature: boolean;
  keyEncipherment: boolean;
  dataEncipherment: boolean;
  encipherOnly: boolean;
  nonRepudiation: boolean;
}

export class NewCertificate {
  subjectData: SubjectData;
  templateType: TemplateType;
  intermediateCA: IntermediateCA;
  keyUsage: KeyUsage;
  validUntil: String;
}
