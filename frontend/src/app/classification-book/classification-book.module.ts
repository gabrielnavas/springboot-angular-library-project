
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
import { MatDialogModule } from "@angular/material/dialog";

import { HttpClientModule } from "@angular/common/http"

import { UtilsModule } from '../utils/utils.module';

import { ClassificationBookReadComponent } from './components/classification-book-read/classification-book-read.component';
import { ClassificationBookCreateComponent } from './components/classification-book-create/classification-book-create.component';
import { ClassificationBookUpdateComponent } from './components/classification-book-update/classification-book-update.component';
import { ClassificationBookRemoveComponent } from './components/classification-book-remove/classification-book-remove.component';
import { ClassificationBookFindDialogComponent } from './components/classification-book-find-dialog/classification-book-find-dialog.component';


@NgModule({
  declarations: [
    ClassificationBookReadComponent,
    ClassificationBookCreateComponent,
    ClassificationBookUpdateComponent,
    ClassificationBookRemoveComponent,
    ClassificationBookFindDialogComponent,
    
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
    MatDialogModule,
    UtilsModule                 
  ]
})
export class ClassificationBookModule { }
