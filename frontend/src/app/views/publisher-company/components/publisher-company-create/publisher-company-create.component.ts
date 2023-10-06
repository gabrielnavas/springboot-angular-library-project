import { Component, OnInit } from '@angular/core';
import { PublisherCompany } from '../publisher-company.model';
import { Router } from '@angular/router';
import { PublisherCompanyService } from '../../publisher-company.service';

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
    private readonly publisherCompanyService: PublisherCompanyService
  ) {}

  ngOnInit(): void {
    this.initPublisherCompanyModel();
  }

  createPublisherCompany(): void {
    this.publisherCompanyService.createPublisherCompany(this.publisherCompany)
      .subscribe({
        complete: () => null,
        error: err => console.log(err)
      })
    this.router.navigateByUrl("publisher-company")
    this.initPublisherCompanyModel();
  }

  cancel(): void {
    this.router.navigateByUrl("publisher-company")
    this.initPublisherCompanyModel();
  }

  private initPublisherCompanyModel(): void {
    this.publisherCompany = {
      name: ""
    }
  }
}
