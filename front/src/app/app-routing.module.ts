import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminDashboardComponent } from './admin/admin-dashboard/admin-dashboard.component';
import { CsrRequestsComponent } from './admin/csr-requests/csr-requests.component';
import { UserDashboardComponent } from './user/user-dashboard/user-dashboard.component';

const routes: Routes = [
  {
    path: 'user',
    component: UserDashboardComponent,
    outlet: 'UserRouter',
  },
  {
    path: 'admin',
    component: AdminDashboardComponent
  },
  {
    path: 'csrrequests',
    component: CsrRequestsComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
