import { Component, Inject, OnInit, EventEmitter, Output } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-revoke-dialog',
  templateUrl: './revoke-dialog.component.html',
  styleUrls: ['./revoke-dialog.component.scss']
})
export class RevokeDialogComponent {
  confirmButtonText = "Confirm";
  selectedOption = '';
  otherReason = '';

  constructor(
    @Inject(MAT_DIALOG_DATA) public reasonForRevoking: string,
    private dialogRef: MatDialogRef<RevokeDialogComponent>, private toastr: ToastrService) {
    this.dialogRef.disableClose = true;
  }

  onConfirmClick(): void {
    if((!this.selectedOption || this.selectedOption === 'Other') && !this.otherReason)
    {
      this.toastr.error("You give a reason for revoking certificate!");
    }

    else{
      this.reasonForRevoking = this.selectedOption;
      if(this.selectedOption === 'Other')
      {
        this.reasonForRevoking = this.otherReason;
      }
      this.dialogRef.close(this.reasonForRevoking);
    }
  }
}
