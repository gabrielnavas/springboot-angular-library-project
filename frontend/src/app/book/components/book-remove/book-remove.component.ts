import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Book } from '../../book.model';
import { BookService } from '../../book.service';
import { ShowMessagesService } from 'src/app/utils/show-messages.service';
import { Title } from '@angular/platform-browser';
import { COMMA, ENTER } from '@angular/cdk/keycodes';
import { FormControl, Validators } from '@angular/forms';
import { MyErrorStateMatcher } from '../book-create/book-create.component';

@Component({
  selector: 'app-book-remove',
  templateUrl: './book-remove.component.html',
  styleUrls: ['./book-remove.component.css']
})
export class BookRemoveComponent {

  book!: Book

  constructor(
    private readonly router: Router,
    private readonly activatedRoute: ActivatedRoute, 
    private readonly bookService: BookService,
    private readonly showMessagesService: ShowMessagesService,
    private readonly titleService: Title
  ) {
    this.initBookModel();
    this.titleService.setTitle("Remover um Livro");

    const bookId = this.activatedRoute.snapshot.paramMap.get("id");
      if(!bookId) {
        this.cancel()
      } else {
        this.findBookById(bookId)
      }
  }

  removeBook(): void {
    this.bookService.removeBookById(this.book.id)
    .subscribe({
      complete: () => {
        this.showMessagesService.showMessage("Livro removido")
        this.initBookModel();
        this.router.navigateByUrl("book")
      },
      error: () => {
        this.showMessagesService.showMessage("Problemas no servidor, tente novamente mais tarde")
      }
    })
  }

  cancel(): void {
    this.router.navigateByUrl("book")
  }


  private findBookById = (id: string) => {
    this.bookService.findBookById(id)
      .subscribe({
        next: (value) => {
          this.book = Book.fromLiteralToObject(value)
        },
        error: err => {
          this.showMessagesService.showMessage("Tente novamente mais tarde.")
        },
      })
  }

  private initBookModel(): void {
    this.book = new Book();
  }
}
