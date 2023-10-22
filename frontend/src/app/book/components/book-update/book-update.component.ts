import { HttpErrorResponse, HttpResponseBase } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { MyErrorStateMatcher } from '../book-create/book-create.component';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { BookService } from '../../book.service';
import { ShowMessagesService } from 'src/app/utils/show-messages.service';
import { MatDatepickerInputEvent } from '@angular/material/datepicker';
import { MatChipEditedEvent, MatChipInputEvent } from '@angular/material/chips';
import { ClassificationBookFindDialogComponent } from 'src/app/classification-book/components/classification-book-find-dialog/classification-book-find-dialog.component';
import { AuthorBookResponse, Book, ClassificationBookResponse, PublishingCompanyResponse } from '../../book.model';
import { AuthorBookFindDialogComponent } from 'src/app/author-book/components/author-book-find-dialog/author-book-find-dialog.component';
import { PublisherCompanyFindDialogComponent } from 'src/app/publisher-company/components/publisher-company-find-dialog/publisher-company-find-dialog.component';
import { COMMA, ENTER } from '@angular/cdk/keycodes';

@Component({
  selector: 'app-book-update',
  templateUrl: './book-update.component.html',
  styleUrls: ['./book-update.component.css']
})
export class BookUpdateComponent {
  book!: Book

  // for keyWorkds Dynamic Input
  addOnBlur = true;
  readonly separatorKeysCodes = [ENTER, COMMA] as const;

  //validations
  readonly maxLengthTitle = 120;
  readonly maxLengthIsbn = 20;
  readonly maxPages = 50000;
  readonly maxLengthKeyWord = 50;
  readonly maxKeyWord = 20;
  dateNow: Date = new Date()
  titleFormControl = new FormControl('', [Validators.required]);
  pagesFormControl = new FormControl('', [Validators.required, Validators.min(1), Validators.max(50000)]);
  isbnFormControl = new FormControl('', [Validators.required]);
  keyWordsArrayFormControl = new FormControl('', [Validators.maxLength(this.maxKeyWord)]);
  matcher = new MyErrorStateMatcher();

  constructor(
    private readonly activatedRoute: ActivatedRoute,
    private readonly router: Router,
    public readonly dialog: MatDialog,
    private readonly bookService: BookService,
    private readonly showMessagesService: ShowMessagesService
  ) {
    const bookId = this.activatedRoute.snapshot.paramMap.get('id')
    if(bookId) {
      this.initBook()
      this.findBookById(bookId)
      this.updateDateNow()
    } else {
      this.router.navigateByUrl("book")
    }
  }

  changePublication(event: MatDatepickerInputEvent<Date>) {
    if(event.value) {
      this.book.publication = event.value
    }
  }

  updateBook(): void {
    const errors: string[] = this.book.validate()
    if(errors.length > 0) {
      this.showMessagesService.showMessage(errors[0])
      return
    }

    this.bookService.updateBook(this.book)
      .subscribe({
        complete: (() => {
          this.router.navigateByUrl("book")
          this.showMessagesService.showMessage("Livro atualizado!")
          this.initBook()
        }),
        error: (err: HttpErrorResponse) => {
          if(err.status === 0 || err.status > 500 || err.status === 404) {
            this.showMessagesService.showMessage("Problemas no servidor. Tente novamente mais tarde.")
          } else if(err.status === 400) {
            const possibleBadResponse = `book already exists with attribute title with value ${this.book.title}`
            if(err.error.message === possibleBadResponse) {
              this.showMessagesService.showMessage("Livro já adicionado com esse título")
            }
          }
        },
      })
  }

  findBookById(bookId: string): void {
    this.bookService.findBookById(bookId)
      .subscribe({
        next: book => this.book = Book.fromLiteralToObject(book),
        error: (err: HttpErrorResponse) => {
          if(err.status === 0 || err.status >= 500) {
            this.showMessagesService.showMessage("Problemas no servidor. Tente novamente mais tarde.")
            return
          }

          const possibleBookNotFoundMessage = 'book not found'
          if(err.error.message === possibleBookNotFoundMessage) {
            this.showMessagesService.showMessage("Livro não encontrado.")
          } else {
            this.showMessagesService.showMessage("Problemas no servidor. Tente novamente mais tarde.")
          }
        }
      })
  }

  cancel(): void {
    this.router.navigateByUrl("book")
  }

  addKeyWord(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();

    if (!value) {
      return
    }

    if(value.length > this.maxKeyWord) {
      this.showMessagesService.showMessage(`A palavra-chave deve ter no máximo ${this.maxKeyWord} caracteres`)
      return
    }

    this.book.keyWords.push(value);
    event.chipInput!.clear();
  }

  removeKeyWord(keyWord: string): void {
    const index = this.book.keyWords.indexOf(keyWord);

    if (index >= 0) {
      this.book.keyWords.splice(index, 1);
    }
  }

  editKeyWord(fruit: string, event: MatChipEditedEvent) {
    const value = event.value.trim();

    if (!value) {
      this.removeKeyWord(fruit);
      return;
    }

    if(value.length > this.maxKeyWord) {
      this.showMessagesService.showMessage(`A palavra-chave deve ter no máximo ${this.maxKeyWord} caracteres`)
      return
    }

    const index = this.book.keyWords.indexOf(fruit);
    if (index >= 0) {
      this.book.keyWords[index] = value;
    }
  }

  openDialogClassificationBookFind(): void {
    const dialogRef = this.dialog.open(ClassificationBookFindDialogComponent, {
      height: '600px',
      width: '1200px'
    });

    dialogRef.afterClosed().subscribe((classificationBook: ClassificationBookResponse | null) => {
      if(classificationBook) {
        this.book.classificationBook = classificationBook
      }
    });
  }

  openDialogAuthorBookFind(): void {
    const dialogRef = this.dialog.open(AuthorBookFindDialogComponent, {
      height: '600px',
      width: '1200px'
    });

    dialogRef.afterClosed().subscribe((authorBook: AuthorBookResponse | null) => {
      if(authorBook) {
        this.book.authorBookResponse = authorBook
      }
    });
  }

  openDialogPublisherCompanyFind(): void {
    const dialogRef = this.dialog.open(PublisherCompanyFindDialogComponent, {
      height: '600px',
      width: '1200px'
    });

    dialogRef.afterClosed().subscribe((publishingCompany: PublishingCompanyResponse | null) => {
      if(publishingCompany) {
        this.book.publishingCompany = publishingCompany
      }
    });
  }

  private updateDateNow(): void {
    setInterval(() => {
      this.dateNow = new Date()
    }, 1000)
  }

  private initBook(): void {
    this.book = new Book()
  }
}
