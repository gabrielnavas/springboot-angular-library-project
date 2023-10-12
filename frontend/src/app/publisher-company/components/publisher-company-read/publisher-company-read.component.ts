import { AfterViewInit, ChangeDetectorRef, Component, EventEmitter, ViewChild } from '@angular/core';

import { ShowMessagesService } from '../../../utils/show-messages.service';
import { FindAllFilters, PublisherCompanyService } from '../../publisher-company.service';
import { PublisherCompany } from '../../publisher-company.model';
import { Router } from '@angular/router';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'views-publisher-company-read',
  templateUrl: './publisher-company-read.component.html',
  styleUrls: ['./publisher-company-read.component.css']
})
export class PublisherCompanyReadComponent implements AfterViewInit {
  data: PublisherCompany[] = [];

  displayedColumns: Readonly<string[]> = ['name', 'actions'];

  searchInputField: string = ""

  pageApi: number = 0;
  pageSizeApi: Readonly<number> = 7;

  noMoreDataFromApi: boolean = false;
  noDataFromApi: boolean = false;

  constructor(
    private readonly publisherCompanyService: PublisherCompanyService,
    private readonly showMessagesService: ShowMessagesService,
    private readonly changeDetectorRefs: ChangeDetectorRef,
    private readonly router: Router,
    private readonly titleService: Title
  ) { 
    this.titleService.setTitle("Procurar as Editora de livros");
  }

  ngAfterViewInit(): void {
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

  navigateToPublisherCompanyCreate(): void {
    this.router.navigateByUrl("publisher-company/create")
  }

  private findAllInitPage(): void {
    this.publisherCompanyService.findAllPublisherCompany(
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
    this.publisherCompanyService.findAllPublisherCompany(this.pageApi, this.pageSizeApi, {name: this.searchInputField})
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
    this.publisherCompanyService.findAllPublisherCompany(this.pageApi, this.pageSizeApi, filters)
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
