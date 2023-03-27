import { Component } from '@angular/core';

export interface UserData {
  firstName: string;
  lastName: string
}

const TABLE_DATA: UserData[] = [
  {firstName: "Djura", lastName: "Djuric"},
  {firstName: "Mika", lastName: "Djuric"},
  {firstName: "Pera", lastName: "Djuric"},
]

@Component({
  selector: 'app-admin-certificates-page',
  templateUrl: './admin-certificates-page.component.html',
  styleUrls: ['./admin-certificates-page.component.scss']
})
export class AdminCertificatesPageComponent {
  displayedColumns = ['firstName', 'lastName', 'verifyButton', 'cancelButton'];
  dataSource = TABLE_DATA;
}
