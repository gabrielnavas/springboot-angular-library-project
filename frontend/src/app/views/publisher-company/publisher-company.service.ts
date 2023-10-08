import { Injectable } from '@angular/core';
import { PublisherCompany } from './publisher-company.model';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PublisherCompanyService {

  private static CREATE_PUBLISHER_COMPANY_URL = 
    `${environment.endpoints.baseUrl}/${environment.endpoints.publishingCompany.baseUrl}`

  private static FIND_ALL_PUBLISHER_COMPANY_URL = 
    `${environment.endpoints.baseUrl}/${environment.endpoints.publishingCompany.baseUrl}`

  constructor(
    private httpClient: HttpClient
  ) { }

  createPublisherCompany(publisherCompany: PublisherCompany): Observable<PublisherCompany> {
    return this.httpClient.post<PublisherCompany>(
      PublisherCompanyService.CREATE_PUBLISHER_COMPANY_URL, 
      publisherCompany
    )
  }

  findAll(page: number=0, size: number = 10): Observable<PublisherCompany[]> {
    const url = `${PublisherCompanyService.FIND_ALL_PUBLISHER_COMPANY_URL}?page=${page}&size=${size}&sort=name,ASC`
    return this.httpClient.get<PublisherCompany[]>(url)
  }
}
