import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PublisherCompanyReadComponent } from './components/publisher-company/publisher-company-read/publisher-company-read.component';

const routes: Routes = [{
  path: "publisher-company",
  component: PublisherCompanyReadComponent
}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
