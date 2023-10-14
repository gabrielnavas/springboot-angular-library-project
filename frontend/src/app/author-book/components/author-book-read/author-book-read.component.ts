import { ChangeDetectorRef, Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthorBookService, FindAllFilters } from '../../author-book.service';
import { AuthorBook } from '../../author-book.model';
import { ShowMessagesService } from 'src/app/utils/show-messages.service';

@Component({
  selector: 'app-author-book-read',
  templateUrl: './author-book-read.component.html',
  styleUrls: ['./author-book-read.component.css']
})
export class AuthorBookReadComponent { 

  data: AuthorBook[] = []

  displayedColumns: string[] = ['name', 'createdAt', 'updatedAt', 'actions']

  searchInputField: string = ""

  noMoreDataFromApi: boolean = false
  noDataFromApi: boolean = false

  pageApi: number = 0;
  pageSizeApi: Readonly<number> = 7;

  constructor(
    private readonly router: Router,
    private readonly authorBookService: AuthorBookService,
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

  navigateToAuthorBookCreate(): void {
    this.router.navigateByUrl("author-book/create")
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
    this.authorBookService.findAllAuthorBook(
      this.pageApi, 
      this.pageSizeApi, 
      {name: this.searchInputField}
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
    this.authorBookService.findAllAuthorBook(this.pageApi, this.pageSizeApi, {name: this.searchInputField})
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
      name: this.searchInputField.trim()
    } 
    this.authorBookService.findAllAuthorBook(this.pageApi, this.pageSizeApi, filters)
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
