import { Component, OnInit } from '@angular/core';
import { PublisherCompany } from '../../publisher-company.model';
import { Router } from '@angular/router';
import { PublisherCompanyService } from '../../publisher-company.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ShowMessagesService } from '../../show-messages.service';


@Component({
  selector: 'app-publisher-company-create',
  templateUrl: './publisher-company-create.component.html',
  styleUrls: ['./publisher-company-create.component.css']
})
export class PublisherCompanyCreateComponent implements OnInit  {
  publisherCompany: PublisherCompany  = {
    name: ""
  }

  constructor(
    private readonly router: Router,
    private readonly publisherCompanyService: PublisherCompanyService,
    private showMessagesService: ShowMessagesService
  ) {}

  ngOnInit(): void {
    this.initPublisherCompanyModel();
  }

  createPublisherCompany(): void {
    this.publisherCompanyService.createPublisherCompany(this.publisherCompany)
      .subscribe({
        complete: () => this.showMessagesService.showMessageSuccess("Editora de livros adicionada"),
        error: err => this.showMessagesService.showMessageFailed("Problemas no servidor, tente novamente mais tarde")
      })
    this.initPublisherCompanyModel();
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
    this.publisherCompany = {
      name: ""
    }
  }
}
