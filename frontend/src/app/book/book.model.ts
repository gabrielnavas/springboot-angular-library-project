export class PublishingCompanyResponse {
  constructor(
    public id: string = "",
    public name: string = "",
    public createdAt: Date = new Date(),
    public updatedAt: Date = new Date(),
  ) { }
}

export class ClassificationBookResponse {
  constructor(
    public id: string = "",
    public name: string = "",
    public createdAt: Date = new Date(),
    public updatedAt: Date = new Date(),
  ) { }
}

export class AuthorBookResponse {
  constructor(
    public id: string = "",
    public name: string = "",
    public createdAt: Date = new Date(),
    public updatedAt: Date = new Date(),
  ) { }
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
}