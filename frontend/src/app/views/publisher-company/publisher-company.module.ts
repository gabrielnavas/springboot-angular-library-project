import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MatButtonModule } from "@angular/material/button"
import { MatCardModule } from "@angular/material/card"
import { MatFormFieldModule } from "@angular/material/form-field"
import { MatInputModule } from "@angular/material/input";
import { MatSnackBarModule } from "@angular/material/snack-bar";
import { MatTableModule } from '@angular/material/table';

import { FormsModule } from "@angular/forms"

import { HttpClientModule } from "@angular/common/http"

import { PublisherCompanyComponent } from './publisher-company.component';
import { PublisherCompanyCreateComponent } from './components/publisher-company-create/publisher-company-create.component';
import { PublisherCompanyReadComponent } from './components/publisher-company-read/publisher-company-read.component';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';



@NgModule({
  declarations: [
    PublisherCompanyReadComponent,
    PublisherCompanyComponent,
    PublisherCompanyCreateComponent,
  ],
  imports: [
    CommonModule,
    MatButtonModule,
    MatCardModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    HttpClientModule,
    MatSnackBarModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
  ]
})
export class PublisherCompanyModule { }
