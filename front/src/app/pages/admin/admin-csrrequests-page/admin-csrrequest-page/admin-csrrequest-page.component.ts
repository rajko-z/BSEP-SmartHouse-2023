import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {CSRRequestData} from "../../../../model/csrRequest";
import {CSRRequestService} from "../../../../services/csr_requests/csrrequest.service";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MatDialog} from "@angular/material/dialog";
import {
  AdminRejectCsrCheckComponent
} from "../../../../components/admin/admin-reject-csr-check/admin-reject-csr-check.component";
import {
  COMMON_NAME_REGEX,
  COMMON_NAME_TOOLTIP,
  COUNTRY_NAME_REGEX,
  COUNTRY_NAME_TOOLTIP,
  LOCALITY_NAME_REGEX,
  LOCALITY_NAME_TOOLTIP,
  ORGANIZATION_NAME_REGEX,
  ORGANIZATION_NAME_TOOLTIP,
  ORGANIZATION_UNIT_REGEX,
  ORGANIZATION_UNIT_TOOLTIP,
  STATE_NAME_REGEX,
  STATE_NAME_TOOLTIP
} from "../../../../services/utils/RegexUtil";
import {ToastrService} from "ngx-toastr";
import * as moment from "moment";
import {IntermediateCA, KeyUsage, NewCertificate, SubjectData, TemplateType} from "../../../../model/newCertificate";
import {DateUtilsService} from "../../../../services/utils/date/date-utils.service";
import {CertificateService} from "../../../../services/certificate/certificate.service";

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

  sendingCert: boolean = false;

  maxLength: number = 64;

  minDate: Date;
  maxDate: Date;

  subjectDataForm = new FormGroup({
    email: new FormControl({value: '', disabled: true}),
    commonName: new FormControl('', [Validators.pattern(COMMON_NAME_REGEX), Validators.maxLength(this.maxLength)]),
    organizationUnit: new FormControl('',[Validators.pattern(ORGANIZATION_UNIT_REGEX), Validators.maxLength(this.maxLength)]),
    organizationName: new FormControl('', [Validators.pattern(ORGANIZATION_NAME_REGEX), Validators.maxLength(this.maxLength)]),
    localityName: new FormControl('', [Validators.pattern(LOCALITY_NAME_REGEX), Validators.maxLength(this.maxLength)]),
    stateName: new FormControl('', [Validators.pattern(STATE_NAME_REGEX), Validators.maxLength(this.maxLength)]),
    country: new FormControl('', [Validators.pattern(COUNTRY_NAME_REGEX), Validators.maxLength(this.maxLength)]),
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
    private matDialog: MatDialog,
    private toastService: ToastrService,
    private certificatesService: CertificateService,
    private dateUtils: DateUtilsService,
    private router: Router)
  {
    this.id = route.snapshot.paramMap.get('id');

    const now = moment();
    this.minDate = now.add(3, 'months').toDate();
    this.maxDate = now.add(13, 'months').toDate();
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
    if (this.inputsNotValid()) {
      return;
    }
    const certificateData: NewCertificate = this.createRequestBody();

    this.sendingCert = true;

    this.certificatesService.issueCertificate(certificateData)
      .subscribe({
          next: (_) => {
            this.toastService.success("Successfully issued certificate");
            this.sendingCert = false;
            this.router.navigate(['/admin/csr_requests']);
          },
          error: (error) => {
            console.log(error);
            this.toastService.error(error.error.message);
            this.sendingCert = false;
          }
        }
      );
  }

  inputsNotValid(): boolean {
    if (this.subjectDataForm.controls.commonName.invalid) {
      this.toastService.error("Common Name invalid.");
      return true;
    }
    if (this.subjectDataForm.controls.organizationUnit.invalid) {
      this.toastService.error("Organization Unit invalid.");
      return true;
    }
    if (this.subjectDataForm.controls.organizationName.invalid) {
      this.toastService.error("Organization Name invalid.");
      return true;
    }
    if (this.subjectDataForm.controls.localityName.invalid) {
      this.toastService.error("Locality invalid.");
      return true;
    }
    if (this.subjectDataForm.controls.stateName.invalid) {
      this.toastService.error("State invalid.");
      return true;
    }
    if (this.subjectDataForm.controls.country.invalid) {
      this.toastService.error("Country invalid.");
      return true;
    }
    if (this.signingForm.controls.validUntil.invalid) {
      this.toastService.error("Please choose validity period for certificate (minimum 3 months - maximum 13)");
      return true;
    }
    return false;
  }

  createRequestBody(): NewCertificate {
    let newCertificateData: NewCertificate = new NewCertificate();
    newCertificateData.subjectData = this.createSubjectData();
    newCertificateData.keyUsage = this.createKeyUsageData();
    newCertificateData.validUntil = this.createFormattedDate();
    newCertificateData.templateType = this.getSelectedTemplateType();
    newCertificateData.intermediateCA = this.getSelectedIntermediateICA();
    return newCertificateData;
  }

  getSelectedTemplateType(): TemplateType {
    if (this.templateExtensionsForm.controls.templateType.value === '1') {
      return TemplateType.SSL_SERVER;
    } else {
      return TemplateType.SSL_CLIENT;
    }
  }

  getSelectedIntermediateICA(): IntermediateCA {
    if (this.signingForm.controls.intermediateType.value === '1') {
      return IntermediateCA.SMARTHOUSE2023FirstICA;
    } else {
      return IntermediateCA.SMARTHOUSE2023SecondICA;
    }
  }

  createFormattedDate(): string {
    let date: Date = this.signingForm.controls.validUntil.value as Date;
    return this.dateUtils.formatDate(date);
  }

  createKeyUsageData(): KeyUsage {
    let keyUsage = new KeyUsage();
    keyUsage.decipherOnly = this.templateExtensionsForm.controls.decipherOnly.value as boolean;
    keyUsage.dataEncipherment = this.templateExtensionsForm.controls.dataEncipherment.value as boolean;
    keyUsage.keyAgreement = this.templateExtensionsForm.controls.keyAgreement.value as boolean;
    keyUsage.crlSign = this.templateExtensionsForm.controls.crlSign.value as boolean;
    keyUsage.digitalSignature = this.templateExtensionsForm.controls.digitalSignature.value as boolean;
    keyUsage.keyEncipherment = this.templateExtensionsForm.controls.keyEncipherment.value as boolean;
    keyUsage.dataEncipherment = this.templateExtensionsForm.controls.dataEncipherment.value as boolean;
    keyUsage.encipherOnly = this.templateExtensionsForm.controls.encipherOnly.value as boolean;
    keyUsage.nonRepudiation = this.templateExtensionsForm.controls.nonRepudiation.value as boolean;
    return keyUsage;
  }

  createSubjectData(): SubjectData {
    let subjectData = new SubjectData();
    subjectData.email = this.id as string;
    subjectData.commonName = this.subjectDataForm.controls.commonName.value as string;
    subjectData.organizationUnit = this.subjectDataForm.controls.organizationUnit.value as string;
    subjectData.organizationName = this.subjectDataForm.controls.organizationName.value as string;
    subjectData.localityName = this.subjectDataForm.controls.localityName.value as string;
    subjectData.stateName = this.subjectDataForm.controls.stateName.value as string;
    subjectData.countryName = this.subjectDataForm.controls.country.value as string;
    return subjectData;
  }

  getCommonNameTooltip(): string {
    return COMMON_NAME_TOOLTIP;
  }

  getOrganizationUnitTooltip(): string {
    return ORGANIZATION_UNIT_TOOLTIP;
  }

  getOrganizationNameTooltip(): string {
    return ORGANIZATION_NAME_TOOLTIP;
  }

  getLocalityNameTooltip(): string {
    return LOCALITY_NAME_TOOLTIP;
  }

  getStateNameTooltip(): string {
    return STATE_NAME_TOOLTIP;
  }

  getCountryTooltip(): string {
    return COUNTRY_NAME_TOOLTIP;
  }
}
