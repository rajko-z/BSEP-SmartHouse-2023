export enum Role{
  ROLE_ADMIN = 'ROLE_ADMIN',
  ROLE_OWNER = 'ROLE_OWNER',
  ROLE_TENANT = 'ROLE_TENANT'
}

export class User{
  email: string;
  firstName: string;
  lastName: string;
  deleted?: boolean;
  role?: Role;

  constructor(
    email: string,
    firstName: string,
    lastName: string,
    deleted: boolean,
    role: Role)
  {
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.deleted = deleted;
    this.role = role;
  }
}

export class UserWithToken {
  token : string
  user: User

  constructor(token : string, user: User) {
    this.token = token;
    this.user = user;
  }
}
