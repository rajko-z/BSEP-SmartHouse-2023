import { Router } from '@angular/router';
import { AuthService } from './../../../services/auth/auth.service';
import {Component, Inject, OnInit} from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import {FormControl, Validators} from "@angular/forms";
import {DOCUMENT} from "@angular/common";

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
    console.log(inputElement.value);
  }

}
