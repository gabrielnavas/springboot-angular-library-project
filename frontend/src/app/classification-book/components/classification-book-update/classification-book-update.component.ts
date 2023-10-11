import { Component } from '@angular/core';
import { ClassificationBook } from '../../classification-book.model';
import { ActivatedRoute, Router } from '@angular/router';
import { ClassificationBookService } from '../../classification-book.service';
import { ShowMessagesService } from 'src/app/utils/show-messages.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-classification-book-update',
  templateUrl: './classification-book-update.component.html',
  styleUrls: ['./classification-book-update.component.css']
})
export class ClassificationBookUpdateComponent {
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

  updateClassificationBook():void {
    this.classificationBookService.updatePartialsClassificationBooks(this.classificationBook)
    .subscribe({
      next: (value) => {
        this.showMessagesService.showMessage("Classificação de livros atualizada")
        this.initPublisherCompanyModel();
        this.router.navigateByUrl("classification-book")
      },
      error: (err: HttpErrorResponse) => {
        if(err.status === 0) {
          this.showMessagesService.showMessage("Problemas no servidor, tente novamente mais tarde")
        } else if(err.status === 400) {
          const alreadyExistsWithName = err.error.message === 
            `classification book already exists with attribute name with value ${this.classificationBook.name}`
          if(alreadyExistsWithName) {
            this.showMessagesService.showMessage("Classificação de livros já existe com esse nome. \nTente com outro nome.")
          }
        }
      }
    })
  }

  cancel(): void {
    this.router.navigateByUrl("classification-book")
  }

  keyPress(event: KeyboardEvent) {
    if(event.key === "Enter") {
      this.updateClassificationBook()
    }
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
