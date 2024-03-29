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
import {
  AdminRejectCsrCheckComponent
} from "../../components/admin/admin-reject-csr-check/admin-reject-csr-check.component";
import {AdminAddUserPageComponent} from "../../pages/admin/admin-add-user-page/admin-add-user-page.component";
import { ChangeRoleDialogComponent } from "src/app/components/admin/change-role-dialog/change-role-dialog.component";
import { AdminUserListPageComponent } from "src/app/pages/admin/admin-user-list-page/admin-user-list-page.component";
import {AdminLogPageComponent} from "../../pages/admin/admin-log-page/admin-log-page.component";
import {
  AdminDeviceAlarmTriggersPageComponent
} from "../../pages/admin/admin-device-alarm-triggers-page/admin-device-alarm-triggers-page.component";
import {
  AddAlarmTriggerDialogComponent
} from "../../components/admin/add-alarm-trigger-dialog/add-alarm-trigger-dialog.component";
import {
  AdminActivatedDeviceAlarmsPageComponent
} from "../../pages/admin/admin-activated-device-alarms-page/admin-activated-device-alarms-page.component";

const declaredModules = [
  AdminCSRRequestsPageComponent,
  AdminCertificatesPageComponent,
  AdminDashboardComponent,
  AdminCsrrequestPageComponent,
  RevokeDialogComponent,
  AdminRejectCsrCheckComponent,
  AdminAddUserPageComponent,
  AdminUserListPageComponent,
  ChangeRoleDialogComponent,
  AdminLogPageComponent,
  AdminDeviceAlarmTriggersPageComponent,
  AddAlarmTriggerDialogComponent,
  AdminActivatedDeviceAlarmsPageComponent
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
