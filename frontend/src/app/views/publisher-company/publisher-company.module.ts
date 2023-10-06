import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MatButtonModule } from "@angular/material/button"

import { PublisherCompanyReadComponent } from './components/publisher-company-read/publisher-company-read.component';
import { PublisherCompanyComponent } from './publisher-company.component';
import { PublisherCompanyCreateComponent } from './components/publisher-company-create/publisher-company-create.component';



@NgModule({
  declarations: [
    PublisherCompanyReadComponent,
    PublisherCompanyComponent,
    PublisherCompanyCreateComponent,
  ],
  imports: [
    CommonModule,
    MatButtonModule
  ]
})
export class PublisherCompanyModule { }
