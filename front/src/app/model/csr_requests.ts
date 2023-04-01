export class SimpleCSRRequest {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  timestamp: string;
  filePath?: string;

  constructor(id: number, email: string, firstName: string, lastName: string, timestamp: string, filePath: string) {
    this.id = id;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.timestamp = timestamp;
    this.filePath = filePath;
  }
}
