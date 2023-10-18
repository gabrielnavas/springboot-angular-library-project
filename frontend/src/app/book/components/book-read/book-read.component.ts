import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-book-read',
  templateUrl: './book-read.component.html',
  styleUrls: ['./book-read.component.css']
})
export class BookReadComponent {

  constructor(
    private readonly router: Router
  ) { }

  navigateToCreateBook(): void {
    this.router.navigateByUrl("book/create")
  }
}
