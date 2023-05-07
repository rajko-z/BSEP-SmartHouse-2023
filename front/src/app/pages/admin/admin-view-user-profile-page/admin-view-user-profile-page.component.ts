import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Observable, map, startWith } from 'rxjs';
import { FacilityData } from 'src/app/model/facilityData';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-admin-view-user-profile-page',
  templateUrl: './admin-view-user-profile-page.component.html',
  styleUrls: ['./admin-view-user-profile-page.component.scss']
})
export class AdminViewUserProfilePageComponent implements OnInit{

  facilitiesForm: FormGroup;
  tenantEmailsControl = new FormControl('');
  selectedTenantEmails: Map<number, string[]>;

  userEmail:string = "";
  user:any = "";

  facilities: FacilityData[] = []


  facilityTypes: string[] = ['House', 'Apartment', 'Cottage'];

  allUserEmails: string[];

  filteredUserEmails: Observable<string[]>;

  constructor(private route: ActivatedRoute, private userService:UserService, private toastrService:ToastrService){
    this.userEmail = route.snapshot.paramMap.get('email') as string;
  }


  ngOnInit(){
    this.selectedTenantEmails = new Map<number, string[]>();

    this.facilitiesForm = new FormGroup({});
    
    this.userService.getAllNonAdminEmails()
    .subscribe({
      next: (data) => {
        this.allUserEmails = data;
        this.filteredUserEmails = this.tenantEmailsControl.valueChanges.pipe(
          startWith(''),
          map(value => this._filter(value || '')),
        );
      },
      error: (err) => {
        console.log(err);
      },
    });
    this.getUser();
    this.getUserFacilities();
  }
 

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.allUserEmails.filter(email => email.toLowerCase().includes(filterValue));
  }

  getUser(){
    this.userService.getUserByEmail(this.userEmail).subscribe(
        {
          next:(res)=>{
            this.user = res;
          },
          error:(err)=>{
            this.toastrService.warning("Something went wrong, please try again!");
          }
        }
      );
  }

  getUserFacilities() {
    this.userService.getUserFacilities(this.userEmail).subscribe(
      {
        next:(res:FacilityData[])=>{
          this.facilities = [];
          if(res && res.length > 0){
            for (let index = 0; index < res.length; index++) {
              const element = res[index];
              let facilityData: FacilityData = new FacilityData(element.name, element.address, element.facilityType, element.tenantsEmails)
              this.facilities.push(facilityData);
              this.addFormControls();
            }
            this.setFormValues(res);
          }
        },
        error:(err)=>{
          this.toastrService.warning("Something went wrong, please try again!");
        }
      }
    );
  }


  private setFormValues(res: FacilityData[]) {
    for (let index = 0; index < res.length; index++) {
      const element = res[index];
      this.facilitiesForm.get("name" + index)?.setValue(element.name);
      this.facilitiesForm.get("name" + index)?.disable();
      this.facilitiesForm.get("address" + index)?.setValue(element.address);
      this.facilitiesForm.get("facilityType" + index)?.setValue(this.capitalizeFirstLetter(element.facilityType));
      this.selectedTenantEmails.set(index, element.tenantsEmails);
    }
  }

  private capitalizeFirstLetter(str: string): string {
    str = str.toLowerCase();
    return str.charAt(0).toUpperCase() + str.slice(1);
  }

  addFacility() {
    this.addFormControls();
    this.facilities.push(new FacilityData('', '', 'House', []));
  }

  addFormControls(){
    let nameNumber = 0;
    if(this.facilities.length > 0)
      nameNumber = this.facilities.length-1;

    this.facilitiesForm.addControl(`name${nameNumber}`, new FormControl('', [Validators.required]));
    this.facilitiesForm.addControl(`facilityType${nameNumber}`, new FormControl('', [Validators.required]));
    this.facilitiesForm.addControl(`address${nameNumber}`, new FormControl('', [Validators.required]));
  }

  deleteFacility(index: number) : void {
    this.facilities.splice(index, 1);
    
    let nameAttribute = `name${this.facilities.length}`;
    let facilityTypeAttribute = `facilityType${this.facilities.length}`
    let addressAttribute = `address${this.facilities.length}`;

    this.facilitiesForm.removeControl(nameAttribute);
    this.facilitiesForm.removeControl(facilityTypeAttribute);
    this.facilitiesForm.removeControl(addressAttribute);  
    this.selectedTenantEmails.delete(index);
  }

  public onSubmit(): void {
    this.fillFacilitiesData();

    this.userService.saveFacilities(this.facilities, this.userEmail).subscribe(
      {
        next:(res:any)=>{
          this.toastrService.success(res.text, "Success");
        },
        error:(err)=>{
          this.toastrService.warning("Something went wrong, please try again!");
        }
      }
    );
  }

  public fillFacilitiesData()
  {
    for (let i = 0; i < this.facilities.length; i++) {
      this.facilitiesForm.get(`name${i}`)?.enable();
      this.facilities[i].setName(this.facilitiesForm.value[`name${i}`]);
      this.facilities[i].setAddress(this.facilitiesForm.value[`address${i}`]);
      this.facilities[i].setFacilityType(this.facilitiesForm.value[`facilityType${i}`]);
      const tenantsEmails = this.selectedTenantEmails.get(i) ?? []; // use empty array as default value
      this.facilities[i].setTenantsEmails(tenantsEmails);
      this.facilitiesForm.get(`name${i}`)?.disable();
    }
  }

  public addTenant(facilityIndex: number, tenantEmail: string) {
    this.tenantEmailsControl.setValue('');	//emptying input field

    let newStateArray = this.selectedTenantEmails.get(facilityIndex) ?? [];
    if(!newStateArray.includes(tenantEmail))
    newStateArray.push(tenantEmail);
    this.selectedTenantEmails.set(facilityIndex, newStateArray);
  }
  
  public removeTenant(index: number, email: string) {
    const emailList = this.selectedTenantEmails.get(index);
    if (emailList) {
      const newEmailList = emailList.filter((value) => value !== email);
      this.selectedTenantEmails.set(index, newEmailList);
    }
  }

}
