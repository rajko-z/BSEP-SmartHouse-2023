export interface AuthCredentials{
    email:string,
    password:string
}

export interface AuthCredentialsWith2FACode {
  email: string,
  password: string,
  mfaCode: string;
}
