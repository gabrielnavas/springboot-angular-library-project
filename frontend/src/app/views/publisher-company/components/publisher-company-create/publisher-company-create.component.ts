import { Component, OnInit } from '@angular/core';
import { PublisherCompany } from '../publisher-company.model';
import { Router } from '@angular/router';

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
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    this.initPublisherCompanyModel();
  }

  createPublisherCompany(): void {
    this.router.navigateByUrl("publisher-company")
  }

  cancel(): void {
    this.router.navigateByUrl("publisher-company")
  }

  private initPublisherCompanyModel(): void {
    this.publisherCompany = {
      name: ""
    }
  }
}
