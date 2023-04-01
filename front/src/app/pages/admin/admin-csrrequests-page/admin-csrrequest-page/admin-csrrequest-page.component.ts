import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {CSRRequestData} from "../../../../model/csrRequest";
import {CSRRequestService} from "../../../../services/csr_requests/csrrequest.service";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MatDialog} from "@angular/material/dialog";
import {
  AdminRejectCsrCheckComponent
} from "../../../../components/admin/admin-reject-csr-check/admin-reject-csr-check.component";

@Component({
  selector: 'app-admin-csrrequest-page',
  templateUrl: './admin-csrrequest-page.component.html',
  styleUrls: ['./admin-csrrequest-page.component.scss']
})
export class AdminCsrrequestPageComponent implements OnInit {

  id: string | null;

  csrRequestData: CSRRequestData;

  errorActive: boolean = false;

  loading: boolean = false;

  subjectDataForm = new FormGroup({
    email: new FormControl({value: '', disabled: true}),
    commonName: new FormControl(''),
    organizationUnit: new FormControl(''),
    organizationName: new FormControl(''),
    localityName: new FormControl(''),
    stateName: new FormControl(''),
    country: new FormControl(''),
    pk: new FormControl({value: '', disabled: true}),
    signature: new FormControl({value: '', disabled: true}),
  });

  templateExtensionsForm = new FormGroup({
    authorityKeyIdentifier: new FormControl({value: '2.5.29.35', disabled: true}),
    extendedKeyUsage: new FormControl({value: '2.5.29.37', disabled: true}),
    subjectAlternativeName: new FormControl({value: '2.5.29.17', disabled: true}),
    subjectKeyIdentifier: new FormControl({value: '2.5.29.14', disabled: true}),
    templateType: new FormControl('1'),
    certificateSigning: new FormControl(false),
    decipherOnly: new FormControl(false),
    keyAgreement: new FormControl(false),
    crlSign: new FormControl(false),
    digitalSignature: new FormControl(true),
    keyEncipherment: new FormControl(true),
    dataEncipherment: new FormControl(false),
    encipherOnly: new FormControl(false),
    nonRepudiation: new FormControl(false),
  });

  signingForm = new FormGroup({
    certificateVersion: new FormControl({value:'3', disabled: true}),
    signatureAlgorithm: new FormControl({value:'SHA 256 with RSA', disabled: true}),
    intermediateType: new FormControl('1'),
    validUntil: new FormControl(new Date(), [Validators.required])
  });

  constructor(
    private route: ActivatedRoute,
    private csrRequestService: CSRRequestService,
    private matDialog: MatDialog)
  {
    this.id = route.snapshot.paramMap.get('id');
  }

  ngOnInit(): void {
    this.loadData();
  }

  private loadData(): void {
    if (this.id === null) {
      this.errorActive = true;
      return;
    }

    this.loading = true;

    this.csrRequestService.getCSRRequestDataForEmail(this.id)
      .subscribe({
          next: (csrRequestData) => {
            this.errorActive = false;
            this.csrRequestData = csrRequestData;
            this.fillForm();
            this.loading = false;
          },
          error: (error) => {
            this.errorActive = true;
            this.loading = false;
          }
        }
      );
  }

  private fillForm() {
    this.subjectDataForm.patchValue({
      email: this.id,
      commonName: this.csrRequestData?.commonName,
      organizationUnit: this.csrRequestData?.organizationUnit,
      organizationName: this.csrRequestData?.organizationName,
      localityName: this.csrRequestData?.localityName,
      stateName: this.csrRequestData?.stateName,
      country: this.csrRequestData?.countryName,
      pk: this.csrRequestData?.publicKey,
      signature: this.csrRequestData?.signature
    });
  }

  reject() {
    const dialogRef = this.matDialog.open(AdminRejectCsrCheckComponent, {
      data: this.id,
    });
  }

  issueCertificate() {

  }
}
