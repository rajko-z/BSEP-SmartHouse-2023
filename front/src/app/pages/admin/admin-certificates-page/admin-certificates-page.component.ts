import { Component } from '@angular/core';
import { Certificate } from 'src/app/model/certificate';
import { CertificateService } from 'src/app/services/certificate/certificate.service';


@Component({
  selector: 'app-admin-certificates-page',
  templateUrl: './admin-certificates-page.component.html',
  styleUrls: ['./admin-certificates-page.component.scss']
})
export class AdminCertificatesPageComponent {
  displayedColumns = ['firstName', 'lastName', 'verifyButton', 'cancelButton'];
  certificates: Certificate[];

  constructor(private certificateService: CertificateService){
  }

  ngOnInit(): void {
    this.loadRemovedCertificates();
  }

  loadRemovedCertificates(){
    this.certificateService.getRemovedCertificates()
    .subscribe({
      next: (data) => {
        console.log(data);
        this.certificates = data;
      },
      error: (err) => {
        console.log(err);
      },
    });
  }
}
