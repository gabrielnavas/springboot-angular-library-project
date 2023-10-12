import { AfterViewInit, ChangeDetectorRef, Component } from '@angular/core';
import { Router } from '@angular/router';

import { ClassificationBookService, FindAllFilters } from '../../classification-book.service';
import { ClassificationBook } from '../../classification-book.model';

import { ShowMessagesService } from 'src/app/utils/show-messages.service';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'classification-book-read',
  templateUrl: './classification-book-read.component.html',
  styleUrls: ['./classification-book-read.component.css']
})
export class ClassificationBookReadComponent implements AfterViewInit {
  data: ClassificationBook[] = []

  displayedColumns: string[] = ['name', 'actions']

  searchInputField: string = ""

  pageApi: number = 0;
  pageSizeApi: Readonly<number> = 7;

  noMoreDataFromApi: boolean = false;
  noDataFromApi: boolean = false;

  constructor(
    private readonly router: Router,
    private readonly classificationBookService: ClassificationBookService, 
    private showMessagesService: ShowMessagesService,
    private changeDetectorRefs: ChangeDetectorRef,
    private readonly titleService: Title
  ) { 
    this.titleService.setTitle("Buscar e listar Classificações de livros");
  }

  ngAfterViewInit(): void {
    this.initDataView()
  }

  initDataView(): void { 
    this.data = []
    this.searchInputField = "";
    this.noMoreDataFromApi = false;
    this.noDataFromApi = false;
    this.pageApi = 0;
    this.findAllInitPage()
  }

  loadMoreData() {
    if(this.noMoreDataFromApi || this.noDataFromApi) {
      this.showMessagesService.showMessage("Nenhum dado a mais foi encontrado")
      return;
    }

    this.pageApi++
    this.findAllByMoreData()
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

  keyPressSearchInputField(event: KeyboardEvent) {
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

  private findAllInitPage(): void {
    this.classificationBookService.findAllClassificationBooks(
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
          this.changeDetectorRefs.detectChanges();
        },
        error: err => this.showMessagesService
          .showMessage("Problemas no servidor, tente novamente mais tarde")
      })
  }

  private findAllByMoreData(): void {
    this.classificationBookService.findAllClassificationBooks(this.pageApi, this.pageSizeApi, {name: this.searchInputField})
      .subscribe({
        next: data => {
          if(data.length === 0) {
            this.noMoreDataFromApi = true;
            this.showMessagesService.showMessage("Nenhum dado a mais foi encontrado")
            return
          }
          const allData = this.data.concat(data)
          this.data = allData
          this.changeDetectorRefs.detectChanges();
        },
        error: err => this.showMessagesService
          .showMessage("Problemas no servidor, tente novamente mais tarde")
      })
  }

  private findAllByFilter() {
    const filters: FindAllFilters = {
      name: ""
    } 
    const anyParams: boolean = this.searchInputField.length > 0
    if(anyParams) {
      this.pageApi = 0;
      filters.name = this.searchInputField.trim()
    }
    this.classificationBookService.findAllClassificationBooks(this.pageApi, this.pageSizeApi, filters)
      .subscribe({
        next: data => {
          if(data.length === 0) {
            this.noMoreDataFromApi = true;
            this.showMessagesService.showMessage("Nenhum dado a mais foi encontrado")
          }
          this.data = data
          this.changeDetectorRefs.detectChanges();
        },
        error: err => this.showMessagesService
          .showMessage("Problemas no servidor, tente novamente mais tarde")
      })
  }

  navigateToCreateClassificationBook() {
    this.router.navigateByUrl("classification-book/create")
  }
}
