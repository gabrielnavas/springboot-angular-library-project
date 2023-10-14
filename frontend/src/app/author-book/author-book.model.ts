export class AuthorBook {
  id?: string
  name: string
  createdAt?: Date
  updateAt?: Date

  constructor(id: string="", name: string="", createdAt: Date = new Date(), updatedAt: Date = new Date()) {
    this.id = id;
    this.name = name;
    this.createdAt = createdAt
    this.updateAt = updatedAt
  }

  validate(): Error | null {
    if(this.name === "") {
      return new Error('Nome está vazio.');
    }
    if(this.name.length > 80) {
      return new Error('Nome deve ter menos que 80 caracteres.');
    }
    return null;
  }
}