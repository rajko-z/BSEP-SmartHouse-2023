import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {OwnerHomePageComponent} from "../../pages/owner/owner-home-page/owner-home-page.component";

const routes: Routes = [
  {
    path: '',
    component: OwnerHomePageComponent
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class OwnerRoutingModule {}
