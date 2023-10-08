import { AfterViewInit, ChangeDetectorRef, Component, EventEmitter, ViewChild } from '@angular/core';
import { MatTable } from '@angular/material/table';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { PublisherCompanyReadDataSource } from './publisher-company-read-datasource';
import { ShowMessagesService } from '../../show-messages.service';
import { PublisherCompanyService } from '../../publisher-company.service';
import { PublisherCompany } from '../../publisher-company.model';

@Component({
  selector: 'views-publisher-company-read',
  templateUrl: './publisher-company-read.component.html',
  styleUrls: ['./publisher-company-read.component.css']
})
export class PublisherCompanyReadComponent implements AfterViewInit {
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatTable) table!: MatTable<PublisherCompany>;
  dataSource!: PublisherCompanyReadDataSource

  displayedColumns = ['name', 'actions'];

  pageIndex = 0;

  pageApi: number = 0;
  pageSizeApi: number = 25

  constructor(
    private publisherCompanyService: PublisherCompanyService,
    private showMessagesService: ShowMessagesService,
    private changeDetectorRefs: ChangeDetectorRef
  ) { }


  ngOnInit() {
    this.publisherCompanyServiceFindAll()
  }

  ngAfterViewInit(): void {
    this.initDataSourceAndTable()
  }

  changePage(event: PageEvent) {
    const pageNow = (~~(event.length / event.pageSize)) -1;
    const finalPage = pageNow ===  event.pageIndex;
    const quantityIsLessPaginator = event.length >= this.pageSizeApi;
    if(finalPage && quantityIsLessPaginator) {
      this.publisherCompanyServiceFindAll()
    }
  }

  private initDataSourceAndTable(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    this.table.dataSource = this.dataSource;
  }

  private concatDataFromApi(data: PublisherCompany[]) {
    if(data.length > 0) {
      if(this.dataSource) {
        const allData = this.dataSource.data.concat(data)
        this.dataSource = new PublisherCompanyReadDataSource(allData);
      } else {
        this.dataSource = new PublisherCompanyReadDataSource(data);
      }
      this.pageApi++;
      this.initDataSourceAndTable()
      this.changeDetectorRefs.detectChanges();
    }
  }

  private publisherCompanyServiceFindAll(pageIndex: number=this.pageApi, pageSize: number=this.pageSizeApi) {
    this.publisherCompanyService.findAll(pageIndex, pageSize)
      .subscribe({
        next: data => this.concatDataFromApi(data),
        error: err => this.showMessagesService
          .showMessageFailed("Problemas no servidor, tente novamente mais tarde")
      })
  }
}
