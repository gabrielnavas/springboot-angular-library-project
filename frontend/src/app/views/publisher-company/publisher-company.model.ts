export class PublisherCompany {
  id?: string
  name: string

  constructor() {
    this.id = "";
    this.name = "";
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