import { Component, OnInit } from '@angular/core';
import { PublisherCompany } from '../../publisher-company.model';
import { Router } from '@angular/router';
import { PublisherCompanyService } from '../../publisher-company.service';
import { MatSnackBar } from '@angular/material/snack-bar';

type TypeMessage = 'message-success' | 'message-failed'

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
    private readonly snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.initPublisherCompanyModel();
  }

  createPublisherCompany(): void {
    this.publisherCompanyService.createPublisherCompany(this.publisherCompany)
      .subscribe({
        complete: () => this.showMessage("Editora de livros adicionada", "message-success"),
        error: err => this.showMessage("tente novamente mais tarde", "message-failed")
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

  private showMessage(message: string, typeMessage: TypeMessage): void {
    this.snackBar.open(message, 'x', {
      duration: 3000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
      panelClass: [typeMessage],
    })
  }

  private initPublisherCompanyModel(): void {
    this.publisherCompany = {
      name: ""
    }
  }
}
