import { Component } from '@angular/core';
import { CertificateData } from 'src/app/model/certificate';
import { CertificateService } from 'src/app/services/certificate/certificate.service';

@Component({
  selector: 'app-admin-certificates-page',
  templateUrl: './admin-certificates-page.component.html',
  styleUrls: ['./admin-certificates-page.component.scss']
})
export class AdminCertificatesPageComponent {
  displayedColumns = ['alias', 'algorithm', 'keySize', 'creationDate', 'expiryDate', 'verifyButton', 'cancelButton'];
  certificates: CertificateData[];

  constructor(private certificateService: CertificateService){
  }

  ngOnInit(): void {
    this.loadAllCertificates();
  }

  loadAllCertificates(){
    this.certificateService.getAllCertificates()
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
