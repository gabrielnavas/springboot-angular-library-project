import { Component } from '@angular/core';
import { AuthorBook } from '../../author-book.model';
import { ActivatedRoute, Router } from '@angular/router';
import { ShowMessagesService } from 'src/app/utils/show-messages.service';
import { Title } from '@angular/platform-browser';
import { AuthorBookService } from '../../author-book.service';

@Component({
  selector: 'app-author-book-remove',
  templateUrl: './author-book-remove.component.html',
  styleUrls: ['./author-book-remove.component.css']
})
export class AuthorBookRemoveComponent {

  authorBook!: AuthorBook

  constructor(
    private readonly router: Router,
    private readonly activatedRoute: ActivatedRoute, 
    private readonly authorBookService: AuthorBookService,
    private readonly showMessagesService: ShowMessagesService,
    private readonly titleService: Title
  ) {
    this.titleService.setTitle("Remover uma Classificação de Livros");

    const classificationBookId = this.activatedRoute.snapshot.paramMap.get("id");
      if(!classificationBookId) {
        this.cancel()
      } else {
        this.findAuthorBookById(classificationBookId)
      }
  }

  removeClassificationBook(): void {
    this.authorBookService.removeAuthorBookById(this.authorBook.id as string)
    .subscribe({
      complete: () => {
        this.showMessagesService.showMessage("Author de livros removido")
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

  private initPublisherCompanyModel(): void {
    this.authorBook = new AuthorBook();
  }
}
