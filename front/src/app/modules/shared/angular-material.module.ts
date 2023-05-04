import {NgModule} from '@angular/core';
import {MatButtonModule} from '@angular/material/button';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {MatListModule} from '@angular/material/list';
import {MatStepperModule} from '@angular/material/stepper';
import {MatRadioModule} from '@angular/material/radio';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatProgressBarModule} from '@angular/material/progress-bar';
import {MatTableModule} from "@angular/material/table";
import {MatDialogModule} from "@angular/material/dialog"
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {MatExpansionModule} from "@angular/material/expansion";
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatSortModule} from '@angular/material/sort';
import {MatPaginatorModule} from '@angular/material/paginator';
import {MatButtonToggleModule} from '@angular/material/button-toggle';
import {MatCardModule} from '@angular/material/card';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatNativeDateModule} from '@angular/material/core';
import {MatSelectModule} from "@angular/material/select";
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatMenuModule} from "@angular/material/menu";
import { ToastrModule } from 'ngx-toastr';
import {FileUploadModule} from 'primeng/fileupload';
import { FormsModule } from '@angular/forms';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';


const materialModules:any = [
  MatInputModule,
  MatFormFieldModule,
  MatIconModule,
  MatButtonModule,
  MatSnackBarModule,
  MatListModule,
  MatStepperModule,
  MatRadioModule,
  MatCheckboxModule,
  MatProgressBarModule,
  MatTableModule,
  MatDialogModule,
  MatAutocompleteModule,
  MatProgressSpinnerModule,
  MatExpansionModule,
  MatTooltipModule,
  MatTooltipModule,
  MatSortModule,
  MatPaginatorModule,
  MatButtonToggleModule,
  MatCardModule,
  MatDatepickerModule,
  MatNativeDateModule,
  MatSelectModule,
  MatSidenavModule,
  MatToolbarModule,
  MatMenuModule,
  FormsModule,
  MatSlideToggleModule,
  ToastrModule.forRoot({
    positionClass :'toast-bottom-center'
  }),
  FileUploadModule
];

@NgModule({
  imports: [...materialModules],
  exports: materialModules,
  providers: [MatDatepickerModule]
})
export class AngularMaterialModule {}
