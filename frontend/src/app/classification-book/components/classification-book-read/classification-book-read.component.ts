import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'classification-book-read',
  templateUrl: './classification-book-read.component.html',
  styleUrls: ['./classification-book-read.component.css']
})
export class ClassificationBookReadComponent {
  data: any = []

  displayedColumns: string[] = ['name', 'actions']

  constructor(
    private router: Router
  ) { }

  initDataView(): void { }

  navigateToCreateClassificationBook() {
    this.router.navigateByUrl("classification-book/create")
  }
}
