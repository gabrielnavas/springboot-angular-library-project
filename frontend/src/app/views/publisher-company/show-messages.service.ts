import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class ShowMessagesService {

  constructor(
    private readonly snackBar: MatSnackBar
  ) { }

  showMessage(message: string): void {
    const a = this.snackBar.open(message, 'x', {
      duration: 10000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
    })
  }
}
