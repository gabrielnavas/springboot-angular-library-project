import { Component } from '@angular/core';
import { PublisherCompany } from '../../publisher-company.model';
import { ActivatedRoute, Router } from '@angular/router';
import { PublisherCompanyService } from '../../publisher-company.service';
import { ShowMessagesService } from '../../../utils/show-messages.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-publisher-company-update',
  templateUrl: './publisher-company-update.component.html',
  styleUrls: ['./publisher-company-update.component.css']
})
export class PublisherCompanyUpdateComponent {
  publisherCompany: PublisherCompany = new PublisherCompany();

  constructor(
    private readonly router: Router,
    private readonly activatedRoute: ActivatedRoute, 
    private readonly publisherCompanyService: PublisherCompanyService,
    private showMessagesService: ShowMessagesService
  ) { 
    const publisherCompanyId = this.activatedRoute.snapshot.paramMap.get("id");
    if(!publisherCompanyId) {
      this.router.navigateByUrl("publisher-company")
    } else {
      this.findPublisherCompanyById(publisherCompanyId)
    }
  }

  private findPublisherCompanyById = (id: string) => {
    this.publisherCompanyService.findPublisherCompanyById(id)
      .subscribe({
        next: (value) => {
          this.publisherCompany = new PublisherCompany(value.id, value.name)
        },
        error: err => {
          this.showMessagesService.showMessage("Tente novamente mais tarde.")
        },
      })
  }

  updatePublisherCompany(): void {
    this.publisherCompanyService.updatePublisherCompany(this.publisherCompany.id as string, this.publisherCompany)
    .subscribe({
      complete: () => {
        this.initPublisherCompanyModel();
        this.showMessagesService.showMessage("Editora de livros atualizada")
        this.router.navigateByUrl("publisher-company")
      },
      error: (err: HttpErrorResponse) => {
        if(err.status === 0) {
          this.showMessagesService.showMessage("Problemas no servidor, tente novamente mais tarde")
        } else if(err.status === 400) {
          const alreadyExistsWithName = err.error.message === 
            `publishing company already exists with attribute name with value ${this.publisherCompany.name}`
          if(alreadyExistsWithName) {
            this.showMessagesService.showMessage("Editora de livros já existe com esse nome. \nTente com outro nome.")
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
      this.updatePublisherCompany();
    }
  }

  private initPublisherCompanyModel(): void {
    this.publisherCompany = new PublisherCompany();
  }
}
