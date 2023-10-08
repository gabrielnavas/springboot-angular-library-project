import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { MatTable } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
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

  constructor(
    private publisherCompanyService: PublisherCompanyService,
    private showMessagesService: ShowMessagesService
  ) { }


  ngOnInit() {
    this.publisherCompanyService.findAll()
      .subscribe({
        next: data => {
          this.dataSource = new PublisherCompanyReadDataSource(data);
        },
        error: err => this.showMessagesService
          .showMessageFailed("Problemas no servidor, tente novamente mais tarde")
      })
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    this.table.dataSource = this.dataSource;
  }
}
