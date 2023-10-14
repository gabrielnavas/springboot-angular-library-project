export class AuthorBook {
  id?: string
  name: string

  constructor(id: string="", name: string="") {
    this.id = id;
    this.name = name;
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