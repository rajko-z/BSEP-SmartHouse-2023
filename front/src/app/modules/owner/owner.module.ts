import {NgModule} from "@angular/core";
import {SharedComponentsModule} from "../shared/shared-components.module";
import {OwnerRoutingModule} from "./owner-routing.module";
import {OwnerHomePageComponent} from "../../pages/owner/owner-home-page/owner-home-page.component";
import {
  FacilityAlarmsDialogComponent
} from "../../components/owner/facility-alarms-dialog/facility-alarms-dialog.component";
import {MatTableModule} from "@angular/material/table";

const declaredModules = [
  OwnerHomePageComponent,
  FacilityAlarmsDialogComponent
];

@NgModule({
  declarations: declaredModules,
  imports: [
    OwnerRoutingModule,
    SharedComponentsModule,
    MatTableModule
  ],
  exports: declaredModules
})
export class OwnerModule {}
