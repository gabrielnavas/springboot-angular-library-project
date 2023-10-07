import { Component, OnInit } from '@angular/core';

import { MatSnackBar } from '@angular/material/snack-bar';

import { PublisherCompany } from '../publisher-company.model';
import { PublisherCompanyService } from '../../publisher-company.service';

type TypeMessage = 'message-success' | 'message-failed'

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
    private readonly snackBar: MatSnackBar
  ) { 
    this.publisherCompanies = []
  }

  ngOnInit() {
    this.publisherCompanyService.findAll()
      .subscribe({
        next: data => {
          this.publisherCompanies = data;
        },
        error: err => this.showMessage("tente novamente mais tarde", "message-failed")
      })

    this.publisherCompanyService.createPublisherCompany({} as PublisherCompany)
      .subscribe({
        complete: () => console.log("Editora de livros adicionada", "message-success"),
        error: err => console.log("tente novamente mais tarde", "message-failed")
      })
  }

  private showMessage(message: string, typeMessage: TypeMessage): void {
    this.snackBar.open(message, 'x', {
      duration: 3000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
      panelClass: [typeMessage],
    })
  }
}
