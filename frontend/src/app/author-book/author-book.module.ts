import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from "@angular/forms"

import { MatButtonModule } from "@angular/material/button"
import { MatCardModule } from "@angular/material/card"
import { MatFormFieldModule } from "@angular/material/form-field"
import { MatInputModule } from "@angular/material/input";
import { MatSnackBarModule } from "@angular/material/snack-bar";
import { MatTableModule } from '@angular/material/table';

import { AuthorBookReadComponent } from './components/author-book-read/author-book-read.component';
import { AuthorBookCreateComponent } from './components/author-book-create/author-book-create.component';

import { UtilsModule } from '../utils/utils.module';
import { AuthorBookUpdateComponent } from './components/author-book-update/author-book-update.component'

@NgModule({
  declarations: [
    AuthorBookReadComponent,
    AuthorBookCreateComponent,
    AuthorBookUpdateComponent,
  ],
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSnackBarModule,
    MatTableModule,
    UtilsModule
  ]
})
export class AuthorBookModule { }
