export class ClassificationBook {
  public id: string
  public name: string

  constructor(
    id = "",
    name = ""
  ) { 
    this.id = id
    this.name = name
  }

  validate(): string {
    if(this.name === "") {
      return 'Nome está vazio.';
    }
    if(this.name.length > 80) {
      return 'Nome deve ter menos que 80 caracteres.';
    }
    return "";
  }
}