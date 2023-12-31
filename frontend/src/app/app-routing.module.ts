import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { PublisherCompanyCreateComponent } from './publisher-company/components/publisher-company-create/publisher-company-create.component';
import { PublisherCompanyUpdateComponent } from './publisher-company/components/publisher-company-update/publisher-company-update.component';
import { PublisherCompanyRemoveComponent } from './publisher-company/components/publisher-company-remove/publisher-company-remove.component';
import { PublisherCompanyReadComponent } from './publisher-company/components/publisher-company-read/publisher-company-read.component';

import { ClassificationBookCreateComponent } from './classification-book/components/classification-book-create/classification-book-create.component';
import { ClassificationBookUpdateComponent } from './classification-book/components/classification-book-update/classification-book-update.component';
import { ClassificationBookRemoveComponent } from './classification-book/components/classification-book-remove/classification-book-remove.component';
import { ClassificationBookReadComponent } from './classification-book/components/classification-book-read/classification-book-read.component';

import { AuthorBookReadComponent } from './author-book/components/author-book-read/author-book-read.component';
import { AuthorBookCreateComponent } from './author-book/components/author-book-create/author-book-create.component';
import { AuthorBookUpdateComponent } from './author-book/components/author-book-update/author-book-update.component';
import { AuthorBookRemoveComponent } from './author-book/components/author-book-remove/author-book-remove.component';
import { BookReadComponent } from './book/components/book-read/book-read.component';
import { BookCreateComponent } from './book/components/book-create/book-create.component';
import { BookUpdateComponent } from './book/components/book-update/book-update.component';
import { BookRemoveComponent } from './book/components/book-remove/book-remove.component';

const routes: Routes = [{
  path: "publisher-company",
  component: PublisherCompanyReadComponent
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
  component: ClassificationBookReadComponent
}, {
  path: "classification-book/create",
  component: ClassificationBookCreateComponent
}, {
  path: "classification-book/update/:id",
  component: ClassificationBookUpdateComponent
}, {
  path: "classification-book/remove/:id",
  component: ClassificationBookRemoveComponent
}, {
  path: "author-book",
  component: AuthorBookReadComponent
}, {
  path: "author-book/create",
  component: AuthorBookCreateComponent
}, {
  path: "author-book/update/:id",
  component: AuthorBookUpdateComponent
}, {
  path: "author-book/remove/:id",
  component: AuthorBookRemoveComponent
}, {
  path: "book",
  component: BookReadComponent
}, {
  path: "book/create",
  component: BookCreateComponent
}, {
  path: "book/update/:id",
  component: BookUpdateComponent
}, {
  path: "book/remove/:id",
  component: BookRemoveComponent
}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
