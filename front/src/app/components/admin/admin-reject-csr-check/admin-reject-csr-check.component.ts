import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {CSRRequestService} from "../../../services/csr_requests/csrrequest.service";
import {RejectCSR} from "../../../model/csrRequest";
import {ToastrService} from "ngx-toastr";
import {Router} from "@angular/router";

@Component({
  selector: 'app-admin-reject-csr-check',
  templateUrl: './admin-reject-csr-check.component.html',
  styleUrls: ['./admin-reject-csr-check.component.scss']
})
export class AdminRejectCsrCheckComponent implements OnInit{


  reason: string;

  loading: boolean = false;

  constructor(
    private dialogRef: MatDialogRef<AdminRejectCsrCheckComponent>,
    @Inject(MAT_DIALOG_DATA) public email: string,
    private csrRequestService: CSRRequestService,
    private toast: ToastrService,
    private router: Router
  ) {}

  ngOnInit(): void {

  }

  ok() {
    let body: RejectCSR = new RejectCSR();
    body.email = this.email;
    if (this.reason !== null) {
      body.reason = this.reason;
    }

    this.loading = true;
    this.csrRequestService.rejectCSRRequest(body)
      .subscribe({
          next: (_) => {
            this.loading = false;
            this.toast.success("Successfully deleted request");
            this.dialogRef.close();
            this.router.navigate(['/admin/csr_requests']);
          },
          error: (error) => {
            this.loading = false;
            this.toast.error(error.error.message);
          }
        }
      );
  }

  cancel() {
    this.dialogRef.close();
  }
}
