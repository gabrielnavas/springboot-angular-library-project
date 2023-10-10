import { Injectable } from '@angular/core';
import { PublisherCompany } from './publisher-company.model';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';

export type FindAllFilters = {
  name: string
}

@Injectable({
  providedIn: 'root'
})
export class PublisherCompanyService {

  private static CREATE_PUBLISHER_COMPANY_URL = 
    `${environment.endpoints.baseUrl}/${environment.endpoints.publishingCompany.baseUrl}`

  private static FIND_ALL_PUBLISHER_COMPANY_URL = 
    `${environment.endpoints.baseUrl}/${environment.endpoints.publishingCompany.baseUrl}`

  private static UPDATE_PUBLISHER_COMPANY_URL = 
    `${environment.endpoints.baseUrl}/${environment.endpoints.publishingCompany.baseUrl}`
  
  private static FIND_PUBLISHER_COMPANY_BY_ID_URL = 
    `${environment.endpoints.baseUrl}/${environment.endpoints.publishingCompany.baseUrl}`
  
  private static REMOVE_PUBLISHER_COMPANY_BY_ID_URL = 
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

  findAll(page: number=0, size: number = 10, filters: FindAllFilters): Observable<PublisherCompany[]> {
    console.log(filters);
    
    let queryParams = ""
    const anyParams = Object.values(filters).filter(value => value.length > 0).length > 0
    if(anyParams) {
      queryParams = `&name=${filters.name}`
    }
    const url = `${PublisherCompanyService.FIND_ALL_PUBLISHER_COMPANY_URL}?page=${page}&size=${size}&sort=name,ASC${queryParams}`
    return this.httpClient.get<PublisherCompany[]>(url)
  }

  updatePublisherCompany(publisherCompanyId: string, publisherCompany: PublisherCompany): Observable<PublisherCompany> {
    const url = `${PublisherCompanyService.UPDATE_PUBLISHER_COMPANY_URL}/${publisherCompanyId}`
    return this.httpClient.patch<PublisherCompany>(
      url, 
      publisherCompany
    )
  }

  findPublisherCompanyById(publisherCompanyId: string): Observable<PublisherCompany> {
    const url = `${PublisherCompanyService.FIND_PUBLISHER_COMPANY_BY_ID_URL}/${publisherCompanyId}`
    return this.httpClient.get<PublisherCompany>(url)
  }

  removePublisherCompanyById(publisherCompanyId: string): Observable<PublisherCompany> {
    const url = `${PublisherCompanyService.REMOVE_PUBLISHER_COMPANY_BY_ID_URL}/${publisherCompanyId}`
    return this.httpClient.delete<PublisherCompany>(url)
  }
}
