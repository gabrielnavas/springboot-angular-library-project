import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';

import { ClassificationBook } from './classification-book.model';

export type FindAllFilters = {
  name: string
}

@Injectable({
  providedIn: 'root'
})
export class ClassificationBookService {

  private static CREATE_CLASSIFICATION_BOOK_URL = 
    `${environment.endpoints.baseUrl}/${environment.endpoints.publishingCompany.baseUrl}`

  private static FIND_ALL_CLASSIFICATION_BOOK_URL = 
    `${environment.endpoints.baseUrl}/${environment.endpoints.classificationBook.baseUrl}`

  constructor(
    private httpClient: HttpClient
  ) { }

  createClassificationBook(classificationBook: ClassificationBook): Observable<ClassificationBook> {
    return this.httpClient.post<ClassificationBook>(
      ClassificationBookService.CREATE_CLASSIFICATION_BOOK_URL, 
      classificationBook
    );
  }

  findAllClassificationBooks(page: number=0, size: number = 10, filters: FindAllFilters): Observable<ClassificationBook[]> {
    let url = `${ClassificationBookService.FIND_ALL_CLASSIFICATION_BOOK_URL}?page=${page}&size=${size}&sort=name,ASC`
    
    const anyFilter = (filters: FindAllFilters): boolean => Object
      .values(filters)
      .filter(value => value.length > 0)
      .length > 0
      
    if(anyFilter(filters)) {
      url = `${url}&name=${filters.name}`
    }
    
    return this.httpClient.get<ClassificationBook[]>(url)
  }
}
