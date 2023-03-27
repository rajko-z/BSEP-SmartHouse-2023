import {CommonModule, JsonPipe} from '@angular/common';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AngularMaterialModule} from './angular-material.module';
import {RouterLinkWithHref} from "@angular/router";
import {HomePageComponent} from "../../pages/home-page/home-page.component";

const declaredModules = [
  HomePageComponent
];

@NgModule({
  declarations: declaredModules,
  imports: [
    AngularMaterialModule,
    CommonModule,
    JsonPipe,
    ReactiveFormsModule,
    FormsModule,
    RouterLinkWithHref
  ],
  exports: declaredModules,
})
export class SharedComponentsModule {
}
