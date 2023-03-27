import {NgModule} from "@angular/core";
import {SharedComponentsModule} from "../shared/shared-components.module";
import {OwnerRoutingModule} from "./owner-routing.module";
import {OwnerHomePageComponent} from "../../pages/owner/owner-home-page/owner-home-page.component";

const declaredModules = [
  OwnerHomePageComponent
];

@NgModule({
  declarations: declaredModules,
  imports: [
    OwnerRoutingModule,
    SharedComponentsModule
  ],
  exports: declaredModules
})
export class OwnerModule {}
