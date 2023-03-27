import {NgModule} from "@angular/core";
import {SharedComponentsModule} from "../shared/shared-components.module";
import {AnonymousRoutingModule} from "./anonymous-routing.module";
import {LoginComponent} from "../../components/anonymous/login/login.component";
import {RegisterComponent} from "../../components/anonymous/register/register.component";
import {AnonymousHomePageComponent} from "../../pages/anonymous/anonymous-home-page/anonymous-home-page.component";

const declaredModules = [
  LoginComponent,
  RegisterComponent,
  AnonymousHomePageComponent
];

@NgModule({
  declarations: declaredModules,
  imports: [
    AnonymousRoutingModule,
    SharedComponentsModule
  ],
  exports: declaredModules
})
export class AnonymousModule {}
