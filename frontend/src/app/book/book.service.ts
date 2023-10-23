import { Injectable } from '@angular/core';
import { AuthorBookResponse, Book, ClassificationBookResponse, PublishingCompanyResponse } from './book.model';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

type BookBody = {
  publishingCompanyId: string
  classificationBookId: string
  authorBookId: string
} & Omit<Book, 
  'publishingCompany' 
  | 'classificationBook' 
  | 'authorBookResponse' 
  | 'createdAt' 
  | 'updatedAt'
> 

export type FindAllFilters = {
  title: string
  isbn: string
}


@Injectable({
  providedIn: 'root'
})
export class BookService {

  private endpoint: string = `${environment.endpoints.baseUrl}/${environment.endpoints.book.baseUrl}`

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
    return this.httpClient.post<Book>(this.endpoint, body)
  }

  updateBook(book: Book): Observable<void> {
    const body: BookBody = {
      ...book,
      publishingCompanyId: (book.publishingCompany as PublishingCompanyResponse).id,
      classificationBookId: (book.classificationBook as ClassificationBookResponse).id,
      authorBookId: (book.authorBookResponse as AuthorBookResponse).id,
    }
    const url = `${this.endpoint}/${body.id}`
    return this.httpClient.patch<void>(url, body)
  }

  findBookById(bookId: string): Observable<Book> {
    const url = `${this.endpoint}/${bookId}`
    return this.httpClient.get<Book>(url)
  }

  findAllBooks(page: number=0, size: number = 10, filters: FindAllFilters): Observable<Book[]> {
    let url = `${this.endpoint}?page=${page}&size=${size}&sort=title,ASC&title=${filters.title}&isbn=${filters.isbn}`
    return this.httpClient.get<Book[]>(url)
  }
}
