import { Component, inject } from '@angular/core';
import { COMMA, ENTER } from '@angular/cdk/keycodes';
import { MatChipEditedEvent, MatChipInputEvent } from '@angular/material/chips';
import { LiveAnnouncer } from '@angular/cdk/a11y';
import { Router } from '@angular/router';

@Component({
  selector: 'app-book-create',
  templateUrl: './book-create.component.html',
  styleUrls: ['./book-create.component.css']
})
export class BookCreateComponent {

  addOnBlur = true;
  readonly separatorKeysCodes = [ENTER, COMMA] as const;
  keyWords: string[] = [];

  constructor(
    private readonly router: Router
  ) {}

  createBook(): void {

  }

  cancel(): void {
    this.router.navigateByUrl("book")
  }

  addKeyWord(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();

    if (value) {
      this.keyWords.push(value);
    }

    event.chipInput!.clear();
  }

  removeKeyWord(keyWord: string): void {
    const index = this.keyWords.indexOf(keyWord);

    if (index >= 0) {
      this.keyWords.splice(index, 1);
    }
  }

  editKeyWord(fruit: string, event: MatChipEditedEvent) {
    const value = event.value.trim();

    if (!value) {
      this.removeKeyWord(fruit);
      return;
    }

    const index = this.keyWords.indexOf(fruit);
    if (index >= 0) {
      this.keyWords[index] = value;
    }
  }
}
