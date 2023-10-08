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

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
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
    this.updateDataTable()
  }

  changePage(event: PageEvent) {
    const finalPage = (~~(event.length / event.pageSize)) -1 ===  event.pageIndex
    console.log(finalPage, event.length, this.pageSizeApi);
    
    if(finalPage && event.length >= this.pageSizeApi) {
      this.publisherCompanyServiceFindAll()
    }
  }

  private updateDataTable(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    this.table.dataSource = this.dataSource;
  }

  private publisherCompanyServiceFindAll(pageIndex: number=this.pageApi, pageSize: number=this.pageSizeApi) {
    this.publisherCompanyService.findAll(pageIndex, pageSize)
      .subscribe({
        next: data => {
          if(data.length > 0) {
            if(this.dataSource) {
              const allData = this.dataSource.data.concat(data)
              this.dataSource = new PublisherCompanyReadDataSource(allData);
            } else {
              this.dataSource = new PublisherCompanyReadDataSource(data);
            }
            this.pageApi++;
            this.updateDataTable()
            this.changeDetectorRefs.detectChanges();
           
          }
        },
        error: err => this.showMessagesService
          .showMessageFailed("Problemas no servidor, tente novamente mais tarde")
      })
  }
}
