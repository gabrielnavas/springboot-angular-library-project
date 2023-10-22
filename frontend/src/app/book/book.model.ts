import * as moment from 'moment';

moment.locale('pt-BR');

export class PublishingCompanyResponse {
  constructor(
    public id: string = "",
    public name: string = "",
    public createdAt: Date = new Date(),
    public updatedAt: Date = new Date(),
  ) { }

  static fromLiteralToObject = (data: PublishingCompanyResponse): PublishingCompanyResponse => {
    return new PublishingCompanyResponse(
      data.id,
      data.name,
      new Date(data.createdAt),
      new Date(data.updatedAt)
    )
  }
}

export class ClassificationBookResponse {
  constructor(
    public id: string = "",
    public name: string = "",
    public createdAt: Date = new Date(),
    public updatedAt: Date = new Date(),
  ) { }

  static fromLiteralToObject = (data: ClassificationBookResponse): ClassificationBookResponse => {
    return new ClassificationBookResponse(
      data.id,
      data.name,
      new Date(data.createdAt),
      new Date(data.updatedAt)
    )
  }
}

export class AuthorBookResponse {
  constructor(
    public id: string = "",
    public name: string = "",
    public createdAt: Date = new Date(),
    public updatedAt: Date = new Date(),
  ) { }

  static fromLiteralToObject = (data: AuthorBookResponse): AuthorBookResponse => {
    return new AuthorBookResponse(
      data.id,
      data.name,
      new Date(data.createdAt),
      new Date(data.updatedAt)
    )
  }
}

export class Book {
  constructor(
    public id: string = "",
    public title: string = "",
    public isbn: string = "",
    public pages: number = 0,
    public keyWords: string[] = [],
    public publication: Date = new Date(),
    public createdAt: Date = new Date(),
    public updatedAt: Date = new Date(),
    public publishingCompany: PublishingCompanyResponse | null = null,
    public classificationBook: ClassificationBookResponse | null = null,
    public authorBookResponse: AuthorBookResponse | null = null, 
  ) { }

  private isAfterToday(date: Date): boolean {
    const now = new Date()
    const isBefore = moment(date).isBefore(now)
    return isBefore
  }

  validate = (): string[] => {
    const errors: string[] = []
    if(this.title.length === 0) {
      errors.push("O título é necessário.");
    }
    if(this.title.length > 120) {
      errors.push("O título deve ter no máximo 120 caracteres.");
    }
    if(!this.isAfterToday(this.publication)) {
      errors.push("Data de lancamento deve ser hoje ou antes.");
    }
    if(this.isbn.length === 0) {
      errors.push("O ISBN é necessário.");
    }
    if(this.isbn.length > 20) {
      errors.push("O ISBN deve ter no máximo 20 caracteres.");
    }
    if(this.pages <= 0) {
      errors.push("O número de páginas deve ser maior que 1.");
    }
    if(this.pages > 50_000) {
      errors.push("O número de páginas ser no máximo 50000.");
    }
    if(this.keyWords.length > 20) {
      errors.push("Deve ter no máximo 20 palavras-chave.");
    }
    if(this.classificationBook === null) {
      errors.push("Selecione uma classificação.");
    }
    if(this.authorBookResponse === null) {
      errors.push("Selecione um author.");
    }
    if(this.authorBookResponse === null) {
      errors.push("Selecione uma editora.");
    }

    return errors
  }

  static fromLiteralToObject = (book: Book): Book => {
    return new Book(
      book.id,
      book.title,
      book.isbn,
      book.pages,
      book.keyWords,
      new Date(book.publication),
      new Date(book.createdAt),
      new Date(book.updatedAt),
      PublishingCompanyResponse.fromLiteralToObject(book.publishingCompany as PublishingCompanyResponse),
      ClassificationBookResponse.fromLiteralToObject(book.classificationBook as ClassificationBookResponse),
      AuthorBookResponse.fromLiteralToObject(book.authorBookResponse as AuthorBookResponse)
    )
  }
}