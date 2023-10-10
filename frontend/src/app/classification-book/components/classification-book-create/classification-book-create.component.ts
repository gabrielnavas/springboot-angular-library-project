import { Component } from '@angular/core';
import { ClassificationBook } from '../../classification-book.model';
import { ClassificationBookService } from '../../classification-book.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ShowMessagesService } from 'src/app/utils/show-messages.service';

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
    private showMessagesService: ShowMessagesService
  ) {
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
          this.router.navigateByUrl("classification-book")
          this.showMessagesService.showMessage("Classificação de livros adicionado.")
        },
        error: err => this.showMessagesService.showMessage("Problema no servidor. Tente novamente mais tarde.")
      })
  }

  keyPress(event: KeyboardEvent) {
    if(event.key === "Enter") {
      this.createClassificationBook();
    }
  }

  private initClassificationBookModel(): void {
    this.classificationBook = new ClassificationBook();
  }
}
