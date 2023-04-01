import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {AdminCSRRequestsPageComponent} from "../../pages/admin/admin-csrrequests-page/admin-csrrequests-page.component";
import {
  AdminCertificatesPageComponent
} from "../../pages/admin/admin-certificates-page/admin-certificates-page.component";
import {AdminDashboardComponent} from "../../components/admin/admin-dashboard/admin-dashboard.component";

const routes: Routes = [
  {
    path: '',
    component: AdminDashboardComponent
  },
  {
    path:'csr_requests',
    component: AdminCSRRequestsPageComponent
  },
  {
    path: 'certificates',
    component: AdminCertificatesPageComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule {}
