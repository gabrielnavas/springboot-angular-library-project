import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BookReadComponent } from './components/book-read/book-read.component';

import { FormsModule } from "@angular/forms"

import { MatCardModule } from "@angular/material/card"
import { MatButtonModule } from "@angular/material/button";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { MatGridListModule } from "@angular/material/grid-list";
import { MatListModule } from "@angular/material/list";
import { MatChipsModule } from "@angular/material/chips";
import { MatIconModule } from "@angular/material/icon";

import { BookCreateComponent } from './components/book-create/book-create.component'

@NgModule({
  declarations: [
    BookReadComponent,
    BookCreateComponent
  ],
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatGridListModule,
    MatListModule,
    MatChipsModule,
    MatIconModule,
  ]
})
export class BookModule { }
