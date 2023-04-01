import { RegisterComponent } from './../../components/anonymous/register/register.component';
import { LoginComponent } from './../../components/anonymous/login/login.component';
import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {AnonymousHomePageComponent} from "../../pages/anonymous/anonymous-home-page/anonymous-home-page.component";

const routes: Routes = [
  {
    path: '',
    component: AnonymousHomePageComponent
  },{
    path: 'login',
    component: LoginComponent
  },{
    path: 'register',
    component: RegisterComponent
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AnonymousRoutingModule {}
