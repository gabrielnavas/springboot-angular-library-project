import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from "@angular/forms"

import { MatButtonModule } from "@angular/material/button"
import { MatCardModule } from "@angular/material/card"
import { MatFormFieldModule } from "@angular/material/form-field"
import { MatInputModule } from "@angular/material/input";
import { MatSnackBarModule } from "@angular/material/snack-bar";
import { MatTableModule } from '@angular/material/table';
import { MatDialogModule } from '@angular/material/dialog';

import { HttpClientModule } from "@angular/common/http"

import { PublisherCompanyCreateComponent } from './components/publisher-company-create/publisher-company-create.component';
import { PublisherCompanyReadComponent } from './components/publisher-company-read/publisher-company-read.component';
import { PublisherCompanyUpdateComponent } from './components/publisher-company-update/publisher-company-update.component';
import { PublisherCompanyRemoveComponent } from './components/publisher-company-remove/publisher-company-remove.component';
import { PublisherCompanyFindDialogComponent } from './components/publisher-company-find-dialog/publisher-company-find-dialog.component';

import { UtilsModule } from '../utils/utils.module';


@NgModule({
  declarations: [
    PublisherCompanyReadComponent,
    PublisherCompanyCreateComponent,
    PublisherCompanyUpdateComponent,
    PublisherCompanyRemoveComponent,
    PublisherCompanyFindDialogComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    HttpClientModule,
    MatSnackBarModule,
    MatTableModule,
    MatDialogModule,
    UtilsModule
  ]
})
export class PublisherCompanyModule { }
