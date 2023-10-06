import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { TemplateModule } from './template/template.module';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { PublisherCompanyModule } from './views/publisher-company/publisher-company.module';


@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    TemplateModule,
    PublisherCompanyModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
