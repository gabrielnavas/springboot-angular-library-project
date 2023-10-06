import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MatButtonModule } from "@angular/material/button"

import { PublisherCompanyReadComponent } from './components/publisher-company-read/publisher-company-read.component';
import { PublisherCompanyComponent } from './publisher-company.component';



@NgModule({
  declarations: [
    PublisherCompanyReadComponent,
    PublisherCompanyComponent,
  ],
  imports: [
    CommonModule,
    MatButtonModule
  ]
})
export class PublisherCompanyModule { }
