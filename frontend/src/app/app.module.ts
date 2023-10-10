import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppComponent } from './app.component';
import { TemplatesModule } from './templates/templates.module';

import { PublisherCompanyModule } from './publisher-company/publisher-company.module';


@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    TemplatesModule,
    PublisherCompanyModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
