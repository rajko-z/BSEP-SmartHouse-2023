import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { FacilityDetailsData } from 'src/app/model/facilityDetialsData';
import { FacilityService } from 'src/app/services/facility/facility.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-facility-details-page',
  templateUrl: './facility-details-page.component.html',
  styleUrls: ['./facility-details-page.component.scss']
})
export class FacilityDetailsPageComponent {
  facilityData: FacilityDetailsData;

  constructor(private route: ActivatedRoute, private location: Location, private facilityService: FacilityService, private toastrService: ToastrService) {}

  ngOnInit() {
    this.facilityData.name = this.route.snapshot.paramMap.get('facilityName') as string;
    this.getFacilityByName();
  }

  getFacilityByName(){
    this.facilityService.getFacilityByName(this.facilityData.name).subscribe(
      {
        next:(res)=>{
          this.facilityData = res;
        },
        error:(err)=>{
          this.toastrService.warning("Something went wrong, please try again!");
        }
      }
    );
  }

  goBack(): void {
    this.location.back();
  }
}
