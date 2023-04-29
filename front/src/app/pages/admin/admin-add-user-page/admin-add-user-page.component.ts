import { Component } from '@angular/core';
import {FormControl, FormGroup, Validators, AbstractControl, ValidatorFn, ValidationErrors, FormArray} from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AddUserDTO } from 'src/app/model/addUserDTO';
import { FacilityData } from 'src/app/model/facilityData';
import { UserService } from 'src/app/services/user/user.service';
import {PASSWORD_REGEX} from "../../../services/utils/RegexUtil";


@Component({
  selector: 'app-admin-add-user-page',
  templateUrl: './admin-add-user-page.component.html',
  styleUrls: ['./admin-add-user-page.component.scss']
})
export class AdminAddUserPageComponent {

  userDataForm: FormGroup;
  facilitiesForm: FormGroup;

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
      password: new FormControl('', [Validators.required, Validators.pattern(PASSWORD_REGEX)]),
      confirmPassword: new FormControl('', [Validators.required]),
    }, { validators: this.validatePassword })

    this.facilitiesForm = new FormGroup({
      name0: new FormControl('', [Validators.required]),
      facilityType0: new FormControl('', [Validators.required]),
    })
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
    let nameAttribute = `name${this.facilities.length}`;
    let facilityTypeAttribute = `facilityType${this.facilities.length}`
    this.facilities.push(new FacilityData('', 'House'));
    const facility = new FormGroup({
      [nameAttribute]: new FormControl('', [Validators.required]),
      [facilityTypeAttribute]: new FormControl('', [Validators.required]),
    });
    this.facilitiesForm.addControl(nameAttribute, facility.get(nameAttribute));
    this.facilitiesForm.addControl(facilityTypeAttribute, facility.get(facilityTypeAttribute));
  }

  deleteFacility(index: number) : void {
    this.facilities.splice(index, 1);
  }

  public onSubmit(): void {
    this.fillFacilitiesData();

    let userData: AddUserDTO = this.userDataForm.value;
    userData.facilities = this.facilities;

    this.userService.addUser(userData).subscribe(
      {
        next:(res)=>{
          this.toastrService.success("Request successfully send.");
          console.log("Uspesno")
          this.router.navigateByUrl("anon/login");

        },
        error:(err)=>{
          this.toastrService.warning("Something went wrong, please try again!");
        }
      }
    )
  }

  public fillFacilitiesData()
  {
    for (let i = 0; i < this.facilities.length; i++) {
      this.facilities[i].setName(this.facilitiesForm.value[`name${i}`]);
    }
  }
}
