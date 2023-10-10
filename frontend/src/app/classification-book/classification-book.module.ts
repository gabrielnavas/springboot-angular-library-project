
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

import { HttpClientModule } from "@angular/common/http"

import { ClassificationBookComponent } from './classification-book.component';
import { ClassificationBookReadComponent } from './components/classification-book-read/classification-book-read.component';
import { ClassificationBookCreateComponent } from './components/classification-book-create/classification-book-create.component';
import { UtilsModule } from '../utils/utils.module';



@NgModule({
  declarations: [
    ClassificationBookComponent,
    ClassificationBookReadComponent,
    ClassificationBookCreateComponent,
  ],
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    HttpClientModule,
    MatButtonModule,                 
    MatCardModule,                 
    MatFormFieldModule,              
    MatInputModule,                 
    MatSnackBarModule ,             
    MatTableModule,
    UtilsModule                 
  ]
})
export class ClassificationBookModule { }
