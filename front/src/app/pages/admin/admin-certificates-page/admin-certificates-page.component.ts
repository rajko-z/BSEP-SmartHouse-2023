import { Component } from '@angular/core';
import { CertificateData } from 'src/app/model/certificate';
import { CertificateService } from 'src/app/services/certificate/certificate.service';
import { environment } from 'src/environments/environment';
import * as SockJS from 'sockjs-client';
import { over, Client, Message as StompMessage} from 'stompjs';
import { ToastrService } from 'ngx-toastr';
import { MatDialog } from '@angular/material/dialog';
import { RevokeDialogComponent } from 'src/app/components/admin/revoke-dialog/revoke-dialog.component';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-admin-certificates-page',
  templateUrl: './admin-certificates-page.component.html',
  styleUrls: ['./admin-certificates-page.component.scss']
})
export class AdminCertificatesPageComponent {
  displayedColumns = ['alias', 'algorithm', 'keySize', 'creationDate', 'expiryDate', 'isValid', 'revokeButton'];
  certificates: CertificateData[];
  private stompClient : Client;
  reasonForRevoking$: Observable<string>;
  revokedCertificatesSerialNumbers: String[] = [""];

  constructor(private certificateService: CertificateService, private toastrService: ToastrService, private revokingDialog: MatDialog){
  }

  ngOnInit(): void {
    this.loadAllCertificates();
    this.getRevokedCertificatesSerialNumbers();
    let Sock = new SockJS(environment.backUrl + "/ws");
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
    this.stompClient.subscribe("/verify-certificate-response", (data:any) => this.onVerifyResponseReceived(data));
  }

  onVerifyResponseReceived(payload: StompMessage)
  {
    let payloadData = JSON.parse(payload.body);
    if (payloadData.messageType === 'success')
    {
      this.toastrService.success('Certificate is valid!');
    }

    else
    {
      this.toastrService.error('Certificate is not valid!');
    }

  }

  getRevokedCertificatesSerialNumbers()
  {
    this.certificateService.getRevokedCertificatesSerialNumbers()
    .subscribe({
      next: (data) => {
        console.log(data);
        this.revokedCertificatesSerialNumbers = data;
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  revokeCertificate(certificateSerialNumber: number, reasonForRevoking: string)
  {
    this.certificateService.revokeCertificate(certificateSerialNumber, reasonForRevoking)
    .subscribe({
      next: (data) => {
        this.loadAllCertificates();
        this.getRevokedCertificatesSerialNumbers();
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  openRevokingDialog(serialNumber: number) {
    const dialogRef = this.revokingDialog.open(RevokeDialogComponent);
    dialogRef.afterClosed().subscribe((reasonForRevoking) => {
      this.revokeCertificate(serialNumber, reasonForRevoking);
    });
  }
}
