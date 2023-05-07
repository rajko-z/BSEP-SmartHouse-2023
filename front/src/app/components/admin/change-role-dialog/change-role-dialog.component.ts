import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-change-role-dialog',
  templateUrl: './change-role-dialog.component.html',
  styleUrls: ['./change-role-dialog.component.scss']
})
export class ChangeRoleDialogComponent {

  selectedRole:any = "";

  constructor(public dialogRef: MatDialogRef<ChangeRoleDialogComponent>, @Inject(MAT_DIALOG_DATA) public data:any,) {
    this.selectedRole = data.role;
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}
