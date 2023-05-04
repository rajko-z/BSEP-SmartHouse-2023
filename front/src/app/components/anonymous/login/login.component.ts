import {Router} from '@angular/router';
import {AuthService} from './../../../services/auth/auth.service';
import {Component, Inject, OnInit} from '@angular/core';
import {ToastrService} from 'ngx-toastr';
import {DOCUMENT} from "@angular/common";
import {AuthCredentials, AuthCredentialsWith2FACode} from "../../../model/auth";
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit{
  public loginValid = true;
  public is2FAVisible = false;
  public codeEntered = false;

  public loading = false;

  loginDataForm: FormGroup;

  constructor(
    private authService:AuthService,
    private toastService: ToastrService,
    private router:Router,
    @Inject(DOCUMENT) document: Document){
  }

  ngOnInit(): void {
    this.loginDataForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required]),
    })
  }

  public onFirstLoginStep(): void {
    this.loginValid = true;
    this.loading = true;

    let authCredentials: AuthCredentials = this.loginDataForm.value;

    this.authService.loginFirstStep(authCredentials).subscribe({
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
      password: this.loginDataForm.value['password'],
      email: this.loginDataForm.value['email'] as string,
      mfaCode: code
    };
    console.log(payload);
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
