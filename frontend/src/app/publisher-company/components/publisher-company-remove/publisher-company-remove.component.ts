import { Component } from '@angular/core';
import { PublisherCompany } from '../../publisher-company.model';
import { ActivatedRoute, Router } from '@angular/router';
import { PublisherCompanyService } from '../../publisher-company.service';
import { ShowMessagesService } from '../../../utils/show-messages.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-publisher-company-remove',
  templateUrl: './publisher-company-remove.component.html',
  styleUrls: ['./publisher-company-remove.component.css']
})
export class PublisherCompanyRemoveComponent {
  publisherCompany: PublisherCompany = new PublisherCompany();

  constructor(
    private readonly router: Router,
    private readonly activatedRoute: ActivatedRoute, 
    private readonly publisherCompanyService: PublisherCompanyService,
    private showMessagesService: ShowMessagesService,
    private readonly titleService: Title
  ) { 
    this.titleService.setTitle("Procurar as Editora de livros");
    
    const publisherCompanyId = this.activatedRoute.snapshot.paramMap.get("id");
    if(!publisherCompanyId) {
      this.router.navigateByUrl("publisher-company")
    } else {
      this.findPublisherCompanyById(publisherCompanyId)
    }
  }
  
  removePublisherCompany(): void {
    this.publisherCompanyService.removePublisherCompanyById(this.publisherCompany.id as string)
    .subscribe({
      complete: () => {
        this.initPublisherCompanyModel();
        this.showMessagesService.showMessage("Editora de livros removida")
        this.router.navigateByUrl("publisher-company")
      },
      error: (err: HttpErrorResponse) => {
        this.showMessagesService.showMessage("Problemas no servidor, tente novamente mais tarde")
      }
    })
  }

  cancel(): void {
    this.router.navigateByUrl("publisher-company")
    this.initPublisherCompanyModel();
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

  private initPublisherCompanyModel(): void {
    this.publisherCompany = new PublisherCompany();
  }
}
