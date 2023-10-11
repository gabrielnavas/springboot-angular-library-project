import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { PublisherCompany } from '../../publisher-company.model';
import { PublisherCompanyService } from '../../publisher-company.service';
import { ShowMessagesService } from '../../../utils/show-messages.service';
import { HttpErrorResponse } from '@angular/common/http';


@Component({
  selector: 'app-publisher-company-create',
  templateUrl: './publisher-company-create.component.html',
  styleUrls: ['./publisher-company-create.component.css']
})
export class PublisherCompanyCreateComponent {
  publisherCompany: PublisherCompany = new PublisherCompany();

  constructor(
    private readonly router: Router,
    private readonly publisherCompanyService: PublisherCompanyService,
    private showMessagesService: ShowMessagesService
  ) { }

  createPublisherCompany(): void {
    const error = this.publisherCompany.validate();
    if (error != null) {
      this.showMessagesService.showMessage(error.message);
      return;
    }

    this.publisherCompanyService.createPublisherCompany(this.publisherCompany)
      .subscribe({
        complete: () => {
          this.showMessagesService.showMessage("Editora de livros adicionada")
          this.initPublisherCompanyModel();
        },
        error: (err: HttpErrorResponse) => {
          if(err.status === 0) {
            this.showMessagesService.showMessage("Problemas no servidor, tente novamente mais tarde")
          } else if(err.status === 400) {
            const alreadyExistsWithName = err.error.message === 
              `publishing company already exists with attribute name with value ${this.publisherCompany.name}`
            if(alreadyExistsWithName) {
              this.showMessagesService.showMessage("Editora de livro já existe com esse nome. \nTente com outro nome.")
            }
          }
        }
      })
  }

  cancel(): void {
    this.router.navigateByUrl("publisher-company")
    this.initPublisherCompanyModel();
  }

  keyPress(event: KeyboardEvent) {
    if(event.key === "Enter") {
      this.createPublisherCompany();
    }
  }

  private initPublisherCompanyModel(): void {
    this.publisherCompany = new PublisherCompany();
  }
}
