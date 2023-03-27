import {NgModule} from "@angular/core";
import {AdminRoutingModule} from "./admin-routing.module";
import {SharedComponentsModule} from "../shared/shared-components.module";
import {AdminCSRRequestsPageComponent} from "../../pages/admin/admin-csrrequests-page/admin-csrrequests-page.component";
import {
  AdminCertificatesPageComponent
} from "../../pages/admin/admin-certificates-page/admin-certificates-page.component";
import {AdminDashboardComponent} from "../../components/admin/admin-dashboard/admin-dashboard.component";
import {AngularMaterialModule} from "../shared/angular-material.module";
import {NgForOf} from "@angular/common";

const declaredModules = [
  AdminCSRRequestsPageComponent,
  AdminCertificatesPageComponent,
  AdminDashboardComponent
];

@NgModule({
  declarations: declaredModules,
  imports: [
    AngularMaterialModule,
    AdminRoutingModule,
    SharedComponentsModule,
    NgForOf
  ],
  exports: declaredModules
})
export class AdminModule {}
