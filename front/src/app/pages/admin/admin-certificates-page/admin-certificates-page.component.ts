import { Component } from '@angular/core';
import { CertificateData } from 'src/app/model/certificate';
import { CertificateService } from 'src/app/services/certificate/certificate.service';
import { environment } from 'src/environments/environment';
import * as SockJS from 'sockjs-client';
import { over, Client, Message as StompMessage} from 'stompjs';

@Component({
  selector: 'app-admin-certificates-page',
  templateUrl: './admin-certificates-page.component.html',
  styleUrls: ['./admin-certificates-page.component.scss']
})
export class AdminCertificatesPageComponent {
  displayedColumns = ['alias', 'algorithm', 'keySize', 'creationDate', 'expiryDate', 'verifyButton', 'cancelButton'];
  certificates: CertificateData[];
  private stompClient : Client;

  constructor(private certificateService: CertificateService){
  }

  ngOnInit(): void {
    this.loadAllCertificates();
    let Sock = new SockJS(environment.backUrl + "ws");
    this.stompClient = over(Sock);
    this.stompClient.connect({}, this.onConnected, () => {});
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

  verifyCertificate(certificateSerialNumber: number)
  {
    this.certificateService.verifyCertificate(certificateSerialNumber.toString())
    .subscribe({
      next: (data) => {
        console.log(data); 
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  onConnected = () => {
    this.stompClient.subscribe("/verify-certificate-response", (data) => this.onVerifyResponseReceived(data));
  }

  onVerifyResponseReceived(payload: StompMessage)
  {
    console.log(payload);
  }
}
