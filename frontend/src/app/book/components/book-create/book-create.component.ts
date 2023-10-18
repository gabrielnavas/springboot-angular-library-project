import { Component, inject } from '@angular/core';
import { COMMA, ENTER } from '@angular/cdk/keycodes';
import { MatChipEditedEvent, MatChipInputEvent } from '@angular/material/chips';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';

import { FormControl, FormGroupDirective, NgForm, Validators } from '@angular/forms';

import { ClassificationBookFindDialogComponent } from 'src/app/classification-book/components/classification-book-find-dialog/classification-book-find-dialog.component';
import { AuthorBookResponse, Book, ClassificationBookResponse, PublishingCompanyResponse } from '../../book.model';
import { AuthorBookFindDialogComponent } from 'src/app/author-book/components/author-book-find-dialog/author-book-find-dialog.component';
import { PublisherCompanyFindDialogComponent } from 'src/app/publisher-company/components/publisher-company-find-dialog/publisher-company-find-dialog.component';

import { ErrorStateMatcher } from '@angular/material/core';

export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}

@Component({
  selector: 'app-book-create',
  templateUrl: './book-create.component.html',
  styleUrls: ['./book-create.component.css']
})
export class BookCreateComponent {
  book!: Book

  // for keyWorkds Dynamic Input
  addOnBlur = true;
  readonly separatorKeysCodes = [ENTER, COMMA] as const;
  keyWords: string[] = [];

  //validations
  titleFormControl = new FormControl('', [Validators.required, Validators.minLength(1), Validators.maxLength(120)]);
  isbnFormControl = new FormControl('', [Validators.required, Validators.minLength(1), Validators.maxLength(20)]);
  pagesFormControl = new FormControl('', [Validators.required, Validators.min(1), Validators.max(20)]);
  matcher = new MyErrorStateMatcher();

  constructor(
    private readonly router: Router,
    public dialog: MatDialog
  ) {
    this.initBook()
  }

  createBook(): void {

  }

  cancel(): void {
    this.router.navigateByUrl("book")
  }

  private initBook(): void {
    this.book = new Book()
  }

  addKeyWord(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();

    if (value) {
      this.keyWords.push(value);
    }

    event.chipInput!.clear();
  }

  removeKeyWord(keyWord: string): void {
    const index = this.keyWords.indexOf(keyWord);

    if (index >= 0) {
      this.keyWords.splice(index, 1);
    }
  }

  editKeyWord(fruit: string, event: MatChipEditedEvent) {
    const value = event.value.trim();

    if (!value) {
      this.removeKeyWord(fruit);
      return;
    }

    const index = this.keyWords.indexOf(fruit);
    if (index >= 0) {
      this.keyWords[index] = value;
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
}
