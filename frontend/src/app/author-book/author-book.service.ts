import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthorBook } from './author-book.model';

@Injectable({
  providedIn: 'root'
})
export class AuthorBookService {

  constructor(
    private readonly httpClient: HttpClient
  ) { }

  createAuthorBook(authorBook: AuthorBook): Observable<AuthorBook> {
    return this.httpClient.post<AuthorBook>(
      "http://localhost:8080/api/v1/author-book", 
      authorBook
    )
  }
}
