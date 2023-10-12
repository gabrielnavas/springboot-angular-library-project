import { Component } from '@angular/core';
import { ClassificationBook } from '../../classification-book.model';
import { ClassificationBookService } from '../../classification-book.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ShowMessagesService } from 'src/app/utils/show-messages.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-classification-book-create',
  templateUrl: './classification-book-create.component.html',
  styleUrls: ['./classification-book-create.component.css']
})
export class ClassificationBookCreateComponent {
  classificationBook: ClassificationBook = new ClassificationBook();

  constructor(
    private classificationBookService: ClassificationBookService,
    private router: Router,
    private showMessagesService: ShowMessagesService,
    private readonly titleService: Title
  ) {
    this.titleService.setTitle("Adicionar uma editora de livros")
    this.initClassificationBookModel()
  }

  createClassificationBook(): void {
    const errorValidation = this.classificationBook.validate()
    if(errorValidation.length > 0) {
      this.showMessagesService.showMessage(errorValidation)
      return
    }

    this.classificationBookService.createClassificationBook(this.classificationBook)
      .subscribe({
        next: value => {
          this.showMessagesService.showMessage("Classificação de livros adicionado.")
          this.initClassificationBookModel()
        },
        error: (err: HttpErrorResponse) => {
          if(err.status === 0) {
            this.showMessagesService.showMessage("Problemas no servidor, tente novamente mais tarde")
          } else if(err.status === 400) {
            const alreadyExistsWithName = err.error.message === 
              `classification book already exists with attribute name with value ${this.classificationBook.name}`
            if(alreadyExistsWithName) {
              this.showMessagesService.showMessage("Classificação de livro já existe com esse nome. \nTente com outro nome.")
            }
          }
        }
      })
  }

  keyPress(event: KeyboardEvent) {
    if(event.key === "Enter") {
      this.createClassificationBook();
    }
  }

  cancel(): void {
    this.router.navigateByUrl("classification-book")
  }

  private initClassificationBookModel(): void {
    this.classificationBook = new ClassificationBook();
  }
}
