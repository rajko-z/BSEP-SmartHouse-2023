import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {AdminCSRRequestsPageComponent} from "../../pages/admin/admin-csrrequests-page/admin-csrrequests-page.component";
import {
  AdminCertificatesPageComponent
} from "../../pages/admin/admin-certificates-page/admin-certificates-page.component";
import {AdminDashboardComponent} from "../../components/admin/admin-dashboard/admin-dashboard.component";
import {
  AdminCsrrequestPageComponent
} from "../../pages/admin/admin-csrrequests-page/admin-csrrequest-page/admin-csrrequest-page.component";
import { AdminAddUserPageComponent } from "src/app/pages/admin/admin-add-user-page/admin-add-user-page.component";
import { AdminUserListPageComponent } from "src/app/pages/admin/admin-user-list-page/admin-user-list-page.component";
import { ViewUserProfilePageComponent } from "src/app/pages/shared/view-user-profile-page/view-user-profile-page.component";
import { FacilityDetailsPageComponent } from "src/app/pages/shared/facility-details-page/facility-details-page.component";
import {AdminLogPageComponent} from "../../pages/admin/admin-log-page/admin-log-page.component";
import {
  AdminDeviceAlarmTriggersPageComponent
} from "../../pages/admin/admin-device-alarm-triggers-page/admin-device-alarm-triggers-page.component";
import {
  AdminActivatedDeviceAlarmsPageComponent
} from "../../pages/admin/admin-activated-device-alarms-page/admin-activated-device-alarms-page.component";

const routes: Routes = [
  {
    path: '',
    component: AdminCSRRequestsPageComponent
  },
  {
    path:'csr_requests',
    component: AdminCSRRequestsPageComponent
  },
  {
    path:'csr_requests/:id',
    component: AdminCsrrequestPageComponent
  },
  {
    path: 'certificates',
    component: AdminCertificatesPageComponent
  },
  {
    path: 'add_user',
    component: AdminAddUserPageComponent
  },
  {
    path: 'all_users',
    component: AdminUserListPageComponent
  },
  {
    path: 'user/:email',
    component: ViewUserProfilePageComponent
  },
  {
    path: 'facility-details/:facilityName',
    component: FacilityDetailsPageComponent
  },
  {
    path: 'logs',
    component: AdminLogPageComponent
  },
  {
    path: 'device_alarm_triggers',
    component: AdminDeviceAlarmTriggersPageComponent
  },
  {
    path: 'device_activated_alarms',
    component: AdminActivatedDeviceAlarmsPageComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule {}
