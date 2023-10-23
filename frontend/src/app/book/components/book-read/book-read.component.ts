import { ChangeDetectorRef, Component } from '@angular/core';
import { Router } from '@angular/router';
import { Book } from '../../book.model';
import { BookService, FindAllFilters } from '../../book.service';
import { ShowMessagesService } from 'src/app/utils/show-messages.service';

@Component({
  selector: 'app-book-read',
  templateUrl: './book-read.component.html',
  styleUrls: ['./book-read.component.css']
})
export class BookReadComponent {
  data: Book[] = []

  displayedColumns: string[] = [
    'title', 
    'isbn',
    'publication', 
    'createdAt', 
    'updatedAt', 
    'actions'
  ]

  searchInputField: string = ""

  noMoreDataFromApi: boolean = false
  noDataFromApi: boolean = false

  pageApi: number = 0;
  pageSizeApi: Readonly<number> = 7;

  constructor(
    private readonly router: Router,
    private readonly bookService: BookService,
    private readonly showMessagesService: ShowMessagesService,
    private readonly changeDetectorRef: ChangeDetectorRef
  ) {
    this.initDataView()
  }

  initDataView() {
    this.data = []
    this.searchInputField = "";
    this.noMoreDataFromApi = false;
    this.noDataFromApi = false;
    this.pageApi = 0;
    this.findAllInitPage();
  }

  onClickButtonFindByFilter(): void {
    this.noMoreDataFromApi = false;
    this.pageApi = 0;
    if(this.searchInputField.length === 0) {
      this.findAllInitPage()
    } else {
      this.findAllByFilter();
    }
  }

  keyPressSearchInputField(event: KeyboardEvent): void {
    if(event.key !== "Enter") {
      return
    }

    if(this.noMoreDataFromApi || this.noDataFromApi) {
      this.showMessagesService.showMessage("Nenhum dado a mais foi encontrado")
      return;
    }

    if(this.searchInputField.length === 0) {
      this.data = []
      this.findAllInitPage()
    } else {
      this.findAllByFilter();
    }
  }

  navigateToBookCreate(): void {
    this.router.navigateByUrl("book/create")
  }

  loadMoreData(): void {
    if(this.noMoreDataFromApi || this.noDataFromApi) {
      this.showMessagesService.showMessage("Nenhum dado a mais foi encontrado")
      return;
    }

    this.pageApi++
    this.findAllByMoreData()
  }


  private findAllInitPage(): void {
    const filters: FindAllFilters = {
      title: this.searchInputField.trim(),
      isbn: this.searchInputField.trim()
    } 
    this.bookService.findAllBooks(
      this.pageApi, 
      this.pageSizeApi, 
      filters
    )
      .subscribe({
        next: data => {
          if(data.length === 0) {
            this.noDataFromApi = true;
            this.showMessagesService.showMessage("Nenhum dado foi encontrado")
            return
          }
          this.data = data
          this.changeDetectorRef.detectChanges();
        },
        error: err => this.showMessagesService
          .showMessage("Problemas no servidor, tente novamente mais tarde")
      })
  }

  private findAllByMoreData(): void {
    const filters: FindAllFilters = {
      title: this.searchInputField.trim(),
      isbn: this.searchInputField.trim()
    } 
    this.bookService.findAllBooks(this.pageApi, this.pageSizeApi, filters)
      .subscribe({
        next: data => {
          if(data.length === 0) {
            this.noMoreDataFromApi = true;
            this.showMessagesService.showMessage("Nenhum dado a mais foi encontrado")
            return
          }
          const allData = this.data.concat(data)
          this.data = allData
          this.changeDetectorRef.detectChanges();
        },
        error: err => this.showMessagesService
          .showMessage("Problemas no servidor, tente novamente mais tarde")
      })
  }

  private findAllByFilter() {
    const filters: FindAllFilters = {
      title: this.searchInputField.trim(),
      isbn: this.searchInputField.trim()
    } 
    this.bookService.findAllBooks(this.pageApi, this.pageSizeApi, filters)
      .subscribe({
        next: data => {
          if(data.length === 0) {
            this.noMoreDataFromApi = true;
            this.showMessagesService.showMessage("Nenhum dado a mais foi encontrado")
          }
          this.data = data
          this.changeDetectorRef.detectChanges();
        },
        error: err => this.showMessagesService
          .showMessage("Problemas no servidor, tente novamente mais tarde")
      })
  }
}
