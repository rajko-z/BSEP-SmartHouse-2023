import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {CommonModule, DatePipe} from '@angular/common';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientModule} from "@angular/common/http";
import {ReactiveFormsModule} from "@angular/forms";
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {AngularMaterialModule} from "./modules/shared/angular-material.module";
import { RouterModule } from '@angular/router';
import {MatTableModule} from '@angular/material/table';



@NgModule({
  declarations: [
    AppComponent
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
    {provide: MAT_DIALOG_DATA, useValue: {}},
    DatePipe
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
