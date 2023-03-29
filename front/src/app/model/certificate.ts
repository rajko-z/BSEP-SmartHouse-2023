export interface CertificateData
{
    serialNumber: number
    alias: string
    algorithm: string
    keySize: number
    creationDate: Date
    expiryDate: Date
}