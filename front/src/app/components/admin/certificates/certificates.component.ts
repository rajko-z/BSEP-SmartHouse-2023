import { Component } from '@angular/core';

@Component({
  selector: 'app-certificates',
  templateUrl: './certificates.component.html',
  styleUrls: ['./certificates.component.scss']
})
export class CertificatesComponent {
  displayedColumns = ['id',  'name', 'surname', 'email', 'city', 'phone', 'accepted', 'show new data'];
}
