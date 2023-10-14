import { Component } from '@angular/core';
import { AuthorBook } from '../../author-book.model';
import { Router } from '@angular/router';
import { AuthorBookService } from '../../author-book.service';
import { ShowMessagesService } from 'src/app/utils/show-messages.service';

@Component({
  selector: 'app-author-book-create',
  templateUrl: './author-book-create.component.html',
  styleUrls: ['./author-book-create.component.css']
})
export class AuthorBookCreateComponent {

  authorBook!: AuthorBook

  constructor(
    private readonly router: Router,
    private readonly authorBookService: AuthorBookService,
    private readonly showMessagesService: ShowMessagesService
  ) { 
    this.initAuthorBook();
  }

  keyPress(event: KeyboardEvent) {
    if(event.key === "Enter") {
      this.createAuthorBook();
    }
  }

  createAuthorBook(): void {
    const err = this.authorBook.validate()
    if(err) {
      this.showMessagesService.showMessage(err.message)
      return;
    }

    this.authorBookService.createAuthorBook(this.authorBook)
      .subscribe({
        next: () => {
          this.showMessagesService.showMessage("Autor de livros adicionado.")
          this.initAuthorBook()
        }
      })
  }

  cancel(): void {
    this.router.navigateByUrl("author-book")
  }

  private initAuthorBook(): void {
    this.authorBook = new AuthorBook();
  }
}
