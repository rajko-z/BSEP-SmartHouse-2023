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

  facilities: FacilityData[] = [
    new FacilityData('', '', 'House', [])
  ]


  facilityTypes: string[] = ['House', 'Apartment', 'Cottage'];

  allUserEmails: string[];

  filteredUserEmails: Observable<string[]>;

  constructor(private route: ActivatedRoute, private userService:UserService, private toastrService:ToastrService){
    this.userEmail = route.snapshot.paramMap.get('email') as string;
  }
  ngOnInit(){
    this.selectedTenantEmails = new Map<number, string[]>();

    this.facilitiesForm = new FormGroup({
      name0: new FormControl('', [Validators.required]),
      address0: new FormControl('', [Validators.required]),
      facilityType0: new FormControl('', [Validators.required]),
    })
    
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

  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.allUserEmails.filter(email => email.toLowerCase().includes(filterValue));
  }

  getUser(){
    this.userService.getUserByEmail(this.userEmail).subscribe(
        {
          next:(res)=>{
            console.log(res);
            this.user = res;
            if(this.user.facilities)
              this.facilities = this.user.facilities;
            else
              this.facilities = [];
          },
          error:(err)=>{
            this.toastrService.warning("Something went wrong, please try again!");
          }
        }
      );
  }

  addFacility() {
    let nameAttribute = `name${this.facilities.length}`;
    let facilityTypeAttribute = `facilityType${this.facilities.length}`
    let addressAttribute = `address${this.facilities.length}`;

    this.facilities.push(new FacilityData('', '', 'House', []));
    const facility = new FormGroup({
      [nameAttribute]: new FormControl('', [Validators.required]),
      [facilityTypeAttribute]: new FormControl('', [Validators.required]),
      [addressAttribute]: new FormControl('', [Validators.required])
    });

    this.facilitiesForm.addControl(nameAttribute, facility.get(nameAttribute));
    this.facilitiesForm.addControl(facilityTypeAttribute, facility.get(facilityTypeAttribute));
    this.facilitiesForm.addControl(addressAttribute, facility.get(addressAttribute));
  }

  deleteFacility(index: number) : void {
    this.facilities.splice(index, 1);
    let facilityTypeAttribute = `facilityType${this.facilities.length}`;
    this.facilitiesForm.removeControl(facilityTypeAttribute);
    console.log(this.facilities);
  }

  public onSubmit(): void {
    this.fillFacilitiesData();

    console.log(this.facilities);

    this.userService.saveFacilities(this.facilities, this.userEmail).subscribe(
      {
        next:(res)=>{
          console.log(res);
          this.user = res;
          if(this.user.facilities)
            this.facilities = this.user.facilities;
          else
            this.facilities = [];
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
      this.facilities[i].setName(this.facilitiesForm.value[`name${i}`]);
      this.facilities[i].setAddress(this.facilitiesForm.value[`address${i}`]);
      const tenantsEmails = this.selectedTenantEmails.get(i) ?? []; // use empty array as default value
      this.facilities[i].setTenantsEmails(tenantsEmails);
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
