import { Injectable } from '@angular/core';
import { PublisherCompany } from './components/publisher-company.model';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PublisherCompanyService {

  private baseUrl: string = "http://localhost:8080/api/v1/publishing-company"

  constructor(
    private httpClient: HttpClient
  ) { }

  createPublisherCompany(publisherCompany: PublisherCompany): Observable<PublisherCompany> {
    return this.httpClient.post<PublisherCompany>(this.baseUrl, publisherCompany)
  }

  findAll(): Observable<PublisherCompany[]> {
    return this.httpClient.get<PublisherCompany[]>(this.baseUrl)
  }
}
