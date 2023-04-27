  import { Component } from '@angular/core';
  import {FormControl, FormGroup, Validators, AbstractControl, ValidatorFn, ValidationErrors, FormArray} from '@angular/forms';
  import { Router } from '@angular/router';
  import { ToastrService } from 'ngx-toastr';
import { FacilityData } from 'src/app/model/facilityData';
  import { UserService } from 'src/app/services/user/user.service';


  @Component({
    selector: 'app-admin-add-user-page',
    templateUrl: './admin-add-user-page.component.html',
    styleUrls: ['./admin-add-user-page.component.scss']
  })
  export class AdminAddUserPageComponent {

    userDataForm: FormGroup;

    facilities: FacilityData[] = [
      new FacilityData('', 'House')
    ]

    facilityTypes: string[] = ['House', 'Apartment', 'Cottage'];

    constructor(private userService:UserService, private toastrService:ToastrService, private router:Router) {}

    ngOnInit(){
      this.userDataForm = new FormGroup({
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
      return this.userDataForm.get("firstName");
    }
    get lastName(){
      return this.userDataForm.get("lastName");
    }
    get email(){
      return this.userDataForm.get("email");
    }
    get password(){
      return this.userDataForm.get("password");
    }
    get confirmPassword(){
      return this.userDataForm.get("confirmPassword");
    }

    addFacility() {
      this.facilities.push(new FacilityData('', 'House'));
      const facility = new FormGroup({
        name: new FormControl('', [Validators.required])
      });
    } 

    public onSubmit(): void {
      console.log(this.userDataForm);
    }
  }
