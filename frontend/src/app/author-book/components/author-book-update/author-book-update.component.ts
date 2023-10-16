import { Component } from '@angular/core';
import { AuthorBookService } from '../../author-book.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthorBook } from '../../author-book.model';
import { ShowMessagesService } from 'src/app/utils/show-messages.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-author-book-update',
  templateUrl: './author-book-update.component.html',
  styleUrls: ['./author-book-update.component.css']
})
export class AuthorBookUpdateComponent {
  authorBook!: AuthorBook

  constructor(
    private readonly router: Router,
    private readonly authorBookService: AuthorBookService,
    private readonly showMessagesService: ShowMessagesService,
    private readonly activatedRoute: ActivatedRoute
  ) { 
    const authorBookId = this.activatedRoute.snapshot.paramMap.get("id")
    if(!authorBookId) {
      this.cancel()
    } else {
      this.findAuthorBookById(authorBookId)
    }
  }

  keyPress(event: KeyboardEvent) {
    if(event.key === "Enter") {
      this.updateAuthorBook();
    }
  }

  updateAuthorBook(): void {
    const err = this.authorBook.validate()
    if(err) {
      this.showMessagesService.showMessage(err.message)
      return;
    }

    this.authorBookService.updatePartialsAuthorBook(this.authorBook)
      .subscribe({
        next: () => {
          this.showMessagesService.showMessage("Autor de livros atualizado.")
          this.initAuthorBook()
          this.router.navigateByUrl("author-book")
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

  private findAuthorBookById = (id: string) => {
    this.authorBookService.findAuthorBookById(id)
      .subscribe({
        next: (value) => {
          this.authorBook = new AuthorBook(value.id, value.name)
        },
        error: err => {
          this.showMessagesService.showMessage("Tente novamente mais tarde.")
          this.router.navigateByUrl("author-book")
        },
      })
  }

  private initAuthorBook(): void {
    this.authorBook = new AuthorBook();
  }
}
