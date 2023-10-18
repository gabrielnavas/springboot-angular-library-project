import { ChangeDetectorRef, Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

import { ClassificationBook } from 'src/app/classification-book/classification-book.model';
import { ClassificationBookService, FindAllFilters } from 'src/app/classification-book/classification-book.service';

import { ShowMessagesService } from 'src/app/utils/show-messages.service';

@Component({
  selector: 'app-classification-book-find-dialog',
  templateUrl: './classification-book-find-dialog.component.html',
  styleUrls: ['./classification-book-find-dialog.component.css']
})
export class ClassificationBookFindDialogComponent {
  data: ClassificationBook[] = []

  displayedColumns: string[] = ['name', 'createdAt', 'updatedAt','actions']

  searchInputField: string = ""

  pageApi: number = 0;
  pageSizeApi: Readonly<number> = 7;

  noMoreDataFromApi: boolean = false;
  noDataFromApi: boolean = false;

  constructor(
    private readonly dialogRef: MatDialogRef<ClassificationBookFindDialogComponent>,
    private readonly classificationBookService: ClassificationBookService, 
    private readonly showMessagesService: ShowMessagesService,
    private readonly changeDetectorRefs: ChangeDetectorRef,
  ) { }

  onNoClick(): void {
    this.dialogRef.close();
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
}
