import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { ClassificationBook } from '../../classification-book.model';
import { ClassificationBookService } from '../../classification-book.service';

import { ShowMessagesService } from 'src/app/utils/show-messages.service';

@Component({
  selector: 'app-classification-book-remove',
  templateUrl: './classification-book-remove.component.html',
  styleUrls: ['./classification-book-remove.component.css']
})
export class ClassificationBookRemoveComponent {

  classificationBook: ClassificationBook = new ClassificationBook()

  constructor(
    private readonly router: Router,
    private readonly activatedRoute: ActivatedRoute, 
    private readonly classificationBookService: ClassificationBookService,
    private readonly showMessagesService: ShowMessagesService
  ) {
    const classificationBookId = this.activatedRoute.snapshot.paramMap.get("id");
      if(!classificationBookId) {
        this.cancel()
      } else {
        this.findClassificationBookById(classificationBookId)
      }
  }

  removeClassificationBook(): void {
    this.classificationBookService.removeClassificationBooks(this.classificationBook.id)
    .subscribe({
      complete: () => {
        this.showMessagesService.showMessage("Classificação de livros removida")
        this.initPublisherCompanyModel();
        this.router.navigateByUrl("classification-book")
      },
      error: () => {
        this.showMessagesService.showMessage("Problemas no servidor, tente novamente mais tarde")
      }
    })
  }

  cancel(): void {
    this.router.navigateByUrl("classification-book")
  }


  private findClassificationBookById = (id: string) => {
    this.classificationBookService.findClassificationBookById(id)
      .subscribe({
        next: (value) => {
          this.classificationBook = new ClassificationBook(value.id, value.name)
        },
        error: err => {
          this.showMessagesService.showMessage("Tente novamente mais tarde.")
        },
      })
  }

  private initPublisherCompanyModel(): void {
    this.classificationBook = new ClassificationBook();
  }
}
