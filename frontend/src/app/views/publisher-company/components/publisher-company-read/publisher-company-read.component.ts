import { Component, OnInit } from '@angular/core';

import { MatSnackBar } from '@angular/material/snack-bar';

import { PublisherCompany } from '../../publisher-company.model';
import { PublisherCompanyService } from '../../publisher-company.service';
import { ShowMessagesService } from '../../show-messages.service';


@Component({
  selector: 'views-publisher-company-read',
  templateUrl: './publisher-company-read.component.html',
  styleUrls: ['./publisher-company-read.component.css']
})
export class PublisherCompanyReadComponent implements OnInit {
  
  publisherCompanies: PublisherCompany[];
  displayedColumns: string[] = ['id', 'name', 'actions']

  constructor(
    private publisherCompanyService: PublisherCompanyService,
    private showMessagesService: ShowMessagesService
  ) { 
    this.publisherCompanies = []
  }

  ngOnInit() {
    this.publisherCompanyService.findAll()
      .subscribe({
        next: data => {
          this.publisherCompanies = data;
        },
        error: err => this.showMessagesService.showMessageFailed("Problemas no servidor, tente novamente mais tarde")
      })

    this.publisherCompanyService.createPublisherCompany({} as PublisherCompany)
      .subscribe({
        complete: () => this.showMessagesService.showMessageSuccess("Editora de livros adicionada"),
        error: err => this.showMessagesService.showMessageFailed("Problemas no servidor, tente novamente mais tarde")
      })
  }
}
