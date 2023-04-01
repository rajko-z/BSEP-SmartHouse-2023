import { CommonModule } from '@angular/common';
import { AngularMaterialModule } from './../shared/angular-material.module';
import {NgModule} from "@angular/core";
import {SharedComponentsModule} from "../shared/shared-components.module";
import {AnonymousRoutingModule} from "./anonymous-routing.module";
import {LoginComponent} from "../../components/anonymous/login/login.component";
import {RegisterComponent} from "../../components/anonymous/register/register.component";
import {AnonymousHomePageComponent} from "../../pages/anonymous/anonymous-home-page/anonymous-home-page.component";
import { FormsModule } from '@angular/forms';

const declaredModules = [
  LoginComponent,
  RegisterComponent,
  AnonymousHomePageComponent
];

@NgModule({
  declarations: declaredModules,
  imports: [
    AnonymousRoutingModule,
    SharedComponentsModule,
    AngularMaterialModule,
    FormsModule,
    CommonModule
  ],
  exports: declaredModules
})
export class AnonymousModule {}
