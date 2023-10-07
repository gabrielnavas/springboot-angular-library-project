import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

type TypeMessage = 'message-success' | 'message-failed'


@Injectable({
  providedIn: 'root'
})
export class ShowMessagesService {

  constructor(
    private readonly snackBar: MatSnackBar
  ) { }

  showMessageSuccess(message: string): void {
    this.showMessage(message, 'message-success')
  }
  
  showMessageFailed(message: string): void {
    this.showMessage(message, 'message-failed')
  }
  
  private showMessage(message: string, typeMessage: TypeMessage): void {
    this.snackBar.open(message, 'x', {
      duration: 5000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
      panelClass: [typeMessage],
    })
  }
}
