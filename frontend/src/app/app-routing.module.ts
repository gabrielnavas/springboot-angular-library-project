import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { PublisherCompanyComponent } from './publisher-company/publisher-company.component';
import { PublisherCompanyCreateComponent } from './publisher-company/components/publisher-company-create/publisher-company-create.component';
import { PublisherCompanyUpdateComponent } from './publisher-company/components/publisher-company-update/publisher-company-update.component';
import { PublisherCompanyRemoveComponent } from './publisher-company/components/publisher-company-remove/publisher-company-remove.component';

import { ClassificationBookComponent } from './classification-book/classification-book.component';
import { ClassificationBookCreateComponent } from './classification-book/components/classification-book-create/classification-book-create.component';


const routes: Routes = [{
  path: "publisher-company",
  component: PublisherCompanyComponent
}, {
  path: "publisher-company/create",
  component: PublisherCompanyCreateComponent
}, {
  path: "publisher-company/update/:id",
  component: PublisherCompanyUpdateComponent
}, {
  path: "publisher-company/remove/:id",
  component: PublisherCompanyRemoveComponent
}, {
  path: "classification-book",
  component: ClassificationBookComponent
}, {
  path: "classification-book/create",
  component: ClassificationBookCreateComponent
}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
