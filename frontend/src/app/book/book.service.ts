import { Injectable } from '@angular/core';
import { AuthorBookResponse, Book, ClassificationBookResponse, PublishingCompanyResponse } from './book.model';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

type BookBody = {
  publishingCompanyId: string
  classificationBookId: string
  authorBookId: string
} & Omit<Book, 'PublishingCompanyResponse' | 'ClassificationBookResponse' | 'AuthorBookResponse'> 


@Injectable({
  providedIn: 'root'
})
export class BookService {

  constructor(
    private readonly httpClient: HttpClient
  ) { }

  createBook(book: Book): Observable<Book> {
    const body: BookBody = {
      ...book,
      publishingCompanyId: (book.publishingCompany as PublishingCompanyResponse).id,
      classificationBookId: (book.classificationBook as ClassificationBookResponse).id,
      authorBookId: (book.authorBookResponse as AuthorBookResponse).id,
    }
    return this.httpClient.post<Book>('http://localhost:8080/api/v1/book', body)
  }
}
