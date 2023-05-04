import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { FacilityData } from 'src/app/model/facilityData';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-admin-view-user-profile-page',
  templateUrl: './admin-view-user-profile-page.component.html',
  styleUrls: ['./admin-view-user-profile-page.component.scss']
})
export class AdminViewUserProfilePageComponent implements OnInit{

  facilitiesForm: FormGroup;

  userEmail:string = "";
  user:any = "";

  facilities: FacilityData[] = [
    new FacilityData('', 'House')
  ]

  facilityTypes: string[] = ['House', 'Apartment', 'Cottage'];

  constructor(private route: ActivatedRoute, private userService:UserService, private toastrService:ToastrService){
    this.userEmail = route.snapshot.paramMap.get('email') as string;
    console.log(this.userEmail);
  }
  ngOnInit(){
    this.facilitiesForm = new FormGroup({
      name0: new FormControl('', [Validators.required]),
      facilityType0: new FormControl('', [Validators.required]),
    })
    this.getUser();

  }

  getUser(){
    this.userService.getUserByEmail(this.userEmail).subscribe(
        {
          next:(res)=>{
            console.log(res);
            this.user = res;
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

    this.facilities;

    // this.userService.addUser(userData).subscribe(
    //   {
    //     next:(res)=>{
    //       this.toastrService.success("Request successfully send.");
    //       console.log("Uspesno")
    //       this.router.navigateByUrl("anon/login");

    //     },
    //     error:(err)=>{
    //       this.toastrService.warning("Something went wrong, please try again!");
    //     }
    //   }
    // )
  }

  public fillFacilitiesData()
  {
    for (let i = 0; i < this.facilities.length; i++) {
      this.facilities[i].setName(this.facilitiesForm.value[`name${i}`]);
    }
  }

}
