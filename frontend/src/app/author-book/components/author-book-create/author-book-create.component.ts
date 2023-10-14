import { Component } from '@angular/core';
import { AuthorBook } from '../../author-book.model';
import { Router } from '@angular/router';
import { AuthorBookService } from '../../author-book.service';
import { ShowMessagesService } from 'src/app/utils/show-messages.service';
import { HttpErrorResponse } from '@angular/common/http';

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
        },
        error: (err: HttpErrorResponse) => {
          if(err.status === 0) {
            this.showMessagesService.showMessage("Problemas no servidor, tente novamente mais tarde")
          } else if(err.status === 400) {
            const alreadyExistsWithName = err.error.message === 
              `author book already exists with attribute name with value ${this.authorBook.name}`
            if(alreadyExistsWithName) {
              this.showMessagesService.showMessage("Autor de livro já existe com esse nome. Tente com outro nome.")
            }
          }
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
