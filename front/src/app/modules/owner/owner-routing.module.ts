import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {OwnerHomePageComponent} from "../../pages/owner/owner-home-page/owner-home-page.component";
import { FacilityDetailsPageComponent } from "src/app/pages/shared/facility-details-page/facility-details-page.component";
import { ViewUserProfilePageComponent } from "src/app/pages/shared/view-user-profile-page/view-user-profile-page.component";

const routes: Routes = [
  {
    path: '',
    component: ViewUserProfilePageComponent
  },
  {
    path: 'user',
    component: ViewUserProfilePageComponent
  },
  {
    path: 'facility-details/:facilityName',
    component: FacilityDetailsPageComponent
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class OwnerRoutingModule {}
