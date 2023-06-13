import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {CommonModule, DatePipe} from '@angular/common';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {ReactiveFormsModule} from "@angular/forms";
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {AngularMaterialModule} from "./modules/shared/angular-material.module";
import {RouterModule} from '@angular/router';
import {MatTableModule} from '@angular/material/table';
import {TokenInterceptor} from "./services/interceptor/TokenInterceptor";
import { AdminUserListPageComponent } from './pages/admin/admin-user-list-page/admin-user-list-page.component';
import { ChangeRoleDialogComponent } from './components/admin/change-role-dialog/change-role-dialog.component';
import { ViewUserProfilePageComponent } from './pages/shared/view-user-profile-page/view-user-profile-page.component';
import { FacilityDetailsPageComponent } from './pages/shared/facility-details-page/facility-details-page.component';
import { ReportDialogComponent } from './components/admin/report-dialog/report-dialog.component';


@NgModule({
  declarations: [
    AppComponent,
    ReportDialogComponent,
  ],
  imports: [
    AngularMaterialModule,
    AppRoutingModule,
    CommonModule,
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    ReactiveFormsModule,
    DatePipe,
    RouterModule,
    MatTableModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    },
    {provide: MAT_DIALOG_DATA, useValue: {}},
    DatePipe
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
