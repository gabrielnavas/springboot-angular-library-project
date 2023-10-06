import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'views-publisher-company',
  templateUrl: './publisher-company.component.html',
  styleUrls: ['./publisher-company.component.css']
})
export class PublisherCompanyComponent {

  constructor(
    private router: Router
  ) {}

  navigateToPublisherCompanyCreate(): void {
    this.router.navigateByUrl("publisher-company/create")
  }
}