import {Router} from '@angular/router';
import {AuthService} from './../../../services/auth/auth.service';
import {Component, Inject, OnInit} from '@angular/core';
import {ToastrService} from 'ngx-toastr';
import {DOCUMENT} from "@angular/common";
import {AuthCredentialsWith2FACode} from "../../../model/auth";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit{
  public loginValid = true;
  public email = '';
  public password = '';
  public is2FAVisible = false;
  public code = '';
  public codeEntered = false;

  public loading = false;


  constructor(
    private authService:AuthService,
    private toastService: ToastrService,
    private router:Router,
    @Inject(DOCUMENT) document: Document){
  }

  ngOnInit(): void {}

  public onFirstLoginStep(): void {
    this.loginValid = true;
    this.loading = true;

    this.authService.loginFirstStep({password:this.password, email:this.email}).subscribe({
      next:(res)=>{
        this.loading = false;
        this.is2FAVisible = true;
      },
      error:(err)=>{
        this.loading = false;
        this.toastService.warning("Credentials are not valid!");
        this.loginValid = false;
      }
    });
  }

  public onCodeInputChange(): void {
    let inputElement = document.getElementById('mfa-partitioned') as HTMLInputElement;
    inputElement.value = inputElement.value.replace(/[^0-9.]/g, '').replace(/(\..*)\./g, '$1');
    this.codeEntered = inputElement.value.length == 6;
  }

  public onSecondLoginStep(): void {
    let inputElement = document.getElementById('mfa-partitioned') as HTMLInputElement;
    let code = inputElement.value;

    let payload: AuthCredentialsWith2FACode = {
      password: this.password,
      email: this.email,
      mfaCode: code
    };

    this.loading = true;

    this.authService.loginFinalStep(payload).subscribe({
      next:(res)=>{
        this.loading = false;

        this.authService.setCurrentUser(res);
        this.authService.loggedUser = true;
        let role: string | undefined = this.authService.getCurrentUser()?.role;

        switch(role){
          case 'ROLE_ADMIN':
            this.router.navigateByUrl("admin/");
            break;
          case 'ROLE_OWNER':
            this.router.navigateByUrl("owner/");
            break;
          default:
            this.router.navigateByUrl('anon/login');
        }
      },
      error:(err)=>{
        this.loading = false;
        inputElement.value = '';
        this.is2FAVisible = false;
        this.toastService.error("2FA code is invalid or it has expired.");
      }
    });
  }

}
