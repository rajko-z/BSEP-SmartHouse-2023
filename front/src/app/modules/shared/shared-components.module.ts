import {CommonModule, JsonPipe} from '@angular/common';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AngularMaterialModule} from './angular-material.module';
import {RouterLinkWithHref} from "@angular/router";
import {HomePageComponent} from "../../pages/home-page/home-page.component";
import {ChangePasswordComponent} from "../../components/shared/change-password/change-password.component";
import { ViewUserProfilePageComponent } from 'src/app/pages/shared/view-user-profile-page/view-user-profile-page.component';
import { FacilityDetailsPageComponent } from 'src/app/pages/shared/facility-details-page/facility-details-page.component';

const declaredModules = [
  HomePageComponent,
  ChangePasswordComponent,
  ViewUserProfilePageComponent,
  FacilityDetailsPageComponent,
];

@NgModule({
  declarations: declaredModules,
  imports: [
    AngularMaterialModule,
    CommonModule,
    JsonPipe,
    ReactiveFormsModule,
    FormsModule,
    RouterLinkWithHref,
  ],
  exports: declaredModules,
})
export class SharedComponentsModule {
}
