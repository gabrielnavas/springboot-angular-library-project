import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { AuthorBook } from './author-book.model';

import { environment } from '../../environments/environment';

export type FindAllFilters = {
  name: string
}

@Injectable({
  providedIn: 'root'
})
export class AuthorBookService {

  constructor(
    private readonly httpClient: HttpClient
  ) { }

  createAuthorBook(authorBook: AuthorBook): Observable<AuthorBook> {
    const url = `${environment.endpoints.baseUrl}/${environment.endpoints.authorBook.baseUrl}`
    return this.httpClient.post<AuthorBook>(url, authorBook)
  }

  updatePartialsAuthorBook(authorBook: AuthorBook): Observable<void> {
    const url = `${environment.endpoints.baseUrl}/${environment.endpoints.authorBook.baseUrl}/${authorBook.id}`
    return this.httpClient.patch<void>(url, authorBook)
  }

  findAuthorBookById(authorBookId: string): Observable<AuthorBook> {
    const url = `${environment.endpoints.baseUrl}/${environment.endpoints.authorBook.baseUrl}/${authorBookId}`
    return this.httpClient.get<AuthorBook>(url) 
  }

  removeAuthorBookById(authorBookId: string): Observable<void> {
    const url = `${environment.endpoints.baseUrl}/${environment.endpoints.authorBook.baseUrl}/${authorBookId}`
    return this.httpClient.delete<void>(url) 
  }

  findAllAuthorBook(page: number=0, pageSize: number=10, filters: FindAllFilters): Observable<AuthorBook[]> {
    let queryParams = ""
    const hasAnyParams = Object.values(filters)
      .filter(value => value.length > 0)
      .length > 0
    
    if(hasAnyParams) {
      queryParams = `&name=${filters.name}`
    }

    let url = `${environment.endpoints.baseUrl}/${environment.endpoints.authorBook.baseUrl}?page=${page}&size=${pageSize}&sort=name,ASC${queryParams}`
    return this.httpClient.get<AuthorBook[]>(url)
  }
}
