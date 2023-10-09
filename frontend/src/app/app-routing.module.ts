import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { PublisherCompanyComponent } from './views/publisher-company/publisher-company.component';
import { PublisherCompanyCreateComponent } from './views/publisher-company/components/publisher-company-create/publisher-company-create.component';
import { PublisherCompanyUpdateComponent } from './views/publisher-company/components/publisher-company-update/publisher-company-update.component';

const routes: Routes = [{
  path: "publisher-company",
  component: PublisherCompanyComponent
}, {
  path: "publisher-company/create",
  component: PublisherCompanyCreateComponent
}, {
  path: "publisher-company/update/:id",
  component: PublisherCompanyUpdateComponent
}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
