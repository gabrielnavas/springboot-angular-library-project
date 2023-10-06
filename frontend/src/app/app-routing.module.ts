import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { PublisherCompanyComponent } from './views/publisher-company/publisher-company.component';
import { PublisherCompanyCreateComponent } from './views/publisher-company/components/publisher-company-create/publisher-company-create.component';

const routes: Routes = [{
  path: "publisher-company",
  component: PublisherCompanyComponent
}, {
  path: "publisher-company/create",
  component: PublisherCompanyCreateComponent
}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
