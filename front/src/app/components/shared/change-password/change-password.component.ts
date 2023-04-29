import {Component} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {UserService} from "../../../services/user/user.service";
import {NewPassword} from "../../../model/newPassword";
import {AuthService} from "../../../services/auth/auth.service";
import {ToastrService} from "ngx-toastr";
import {PASSWORD_REGEX} from "../../../services/utils/RegexUtil";
import {Router} from "@angular/router";
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent {

  hideCurrPassword: boolean = true;
  hideNewPassword: boolean = true;
  hideRepeatPassword: boolean = true;

  passwordForm = new FormGroup({
    currPassword: new FormControl('', [Validators.required]),
    newPassword: new FormControl('', [Validators.required, Validators.pattern(PASSWORD_REGEX)]),
    repeatedPassword: new FormControl('', [Validators.required, Validators.pattern(PASSWORD_REGEX)])
  });

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private toastService: ToastrService,
    private router: Router,
    public dialogRef: MatDialogRef<ChangePasswordComponent>
  ) {}

  validate(): boolean {
    let currPass = this.passwordForm.controls.currPassword.value;
    let newPass = this.passwordForm.controls.newPassword.value;
    let repPass = this.passwordForm.controls.repeatedPassword.value;

    if (currPass === newPass) {
      this.passwordForm.controls['newPassword'].setErrors({'passwordSame': true})
      return false;
    }
    if (newPass !== repPass) {
      this.passwordForm.controls['repeatedPassword'].setErrors({'noMatch': true})
      return false;
    }
    return true;
  }

  onSubmit() {
    if (!this.validate()) {
      return;
    }

    const payload: NewPassword = new NewPassword();
    payload.currentPassword = this.passwordForm.controls.currPassword.value;
    payload.newPassword = this.passwordForm.controls.newPassword.value;
    payload.email = this.authService.getCurrentUserEmail();
    console.log(payload)
    this.userService.changePassword(payload)
      .subscribe({
          next: (response) => {
            this.toastService.success("Password is changed, please log in with new password");
            this.authService.logout();
            this.router.navigateByUrl("/anon/login");
            this.dialogRef.close();
          },
          error: (error) => {
            this.toastService.error(error.error.message);
          }
        }
      );
  }
}
