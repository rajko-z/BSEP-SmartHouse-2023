import { Component } from '@angular/core';
import {FormControl, FormGroup, Validators, AbstractControl, ValidatorFn, ValidationErrors} from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { UserService } from 'src/app/services/user/user.service';


@Component({
  selector: 'app-admin-add-user-page',
  templateUrl: './admin-add-user-page.component.html',
  styleUrls: ['./admin-add-user-page.component.scss']
})
export class AdminAddUserPageComponent {

  form: FormGroup;

  constructor(private userService:UserService, private toastrService:ToastrService, private router:Router) {}

  ngOnInit(){
    this.form = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
      firstName: new FormControl('',[Validators.required]),
      lastName: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required, Validators.pattern("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20}")]),
      confirmPassword: new FormControl('', [Validators.required]),
    }, { validators: this.validatePassword })
  }

  public validatePassword: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
    const password = control.get('password');
    const confirmPassword = control.get('confirmPassword');
  
    if(password && confirmPassword && password.value !== confirmPassword.value){
      confirmPassword.setErrors({notEquivalent: true});
    }
    else if(password && confirmPassword && !confirmPassword.errors?.['required']){
      confirmPassword.setErrors(null);
    }
  
    return password && confirmPassword && password.value !== confirmPassword.value ? { notMatchingPasswords: true } : null;
  }

  get firstName(){
    return this.form.get("firstName");
  }
  get lastName(){
    return this.form.get("lastName");
  }
  get email(){
    return this.form.get("email");
  }
  get password(){
    return this.form.get("password");
  }
  get confirmPassword(){
    return this.form.get("confirmPassword");
  }

  public onSubmit(): void {
    console.log(this.form);
  }
}
