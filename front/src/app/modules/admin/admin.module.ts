import {NgModule} from "@angular/core";
import {AdminRoutingModule} from "./admin-routing.module";
import {SharedComponentsModule} from "../shared/shared-components.module";
import {AdminCSRRequestsPageComponent} from "../../pages/admin/admin-csrrequests-page/admin-csrrequests-page.component";
import {
  AdminCertificatesPageComponent
} from "../../pages/admin/admin-certificates-page/admin-certificates-page.component";
import {AdminDashboardComponent} from "../../components/admin/admin-dashboard/admin-dashboard.component";
import {AngularMaterialModule} from "../shared/angular-material.module";
import {CommonModule, DatePipe, NgForOf} from "@angular/common";
import {
  AdminCsrrequestPageComponent
} from "../../pages/admin/admin-csrrequests-page/admin-csrrequest-page/admin-csrrequest-page.component";
import {RevokeDialogComponent} from "../../components/admin/revoke-dialog/revoke-dialog.component";
import {ReactiveFormsModule} from "@angular/forms";

const declaredModules = [
  AdminCSRRequestsPageComponent,
  AdminCertificatesPageComponent,
  AdminDashboardComponent,
  AdminCsrrequestPageComponent,
  RevokeDialogComponent,
];

@NgModule({
  declarations: declaredModules,
  imports: [
    AngularMaterialModule,
    AdminRoutingModule,
    SharedComponentsModule,
    NgForOf,
    CommonModule,
    DatePipe,
    ReactiveFormsModule
  ],
  exports: declaredModules
})
export class AdminModule {}
