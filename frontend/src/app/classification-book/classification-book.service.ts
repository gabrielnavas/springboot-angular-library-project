import { Injectable } from '@angular/core';
import { ClassificationBook } from './classification-book.model';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ClassificationBookService {

  constructor(
    private httpClient: HttpClient
  ) { }

  createClassificationBook(classificationBook: ClassificationBook): Observable<ClassificationBook> {
    return this.httpClient.post<ClassificationBook>("http://localhost:8080/api/v1/classification-book", classificationBook)
  }
}
