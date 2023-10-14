import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-author-book-read',
  templateUrl: './author-book-read.component.html',
  styleUrls: ['./author-book-read.component.css']
})
export class AuthorBookReadComponent { 

  constructor(
    private readonly router: Router 
  ) { }

  navigateToAuthorBookCreate(): void {
    this.router.navigateByUrl("author-book/create")
  }
}
