import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { BehaviorSubject } from 'rxjs';
import { environment } from 'src/app/common/env/env';
import { Message } from 'src/app/models/message';
import * as SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';


@Injectable({
  providedIn: 'root'
})
export class SocketApiService {

  private serverUrl = environment.apiHost + 'socket';
  private stompClient: any;

  private stompSubject = new BehaviorSubject<any>(null);
  private username:string


  isLoaded: boolean = false;
  isCustomSocketOpened = false;
  messages: Message[] = [];

  constructor(private snackBar: MatSnackBar) {
    this.initializeWebSocketConnection();
  }


  initializeWebSocketConnection() {
    // serverUrl je vrednost koju smo definisali u registerStompEndpoints() metodi na serveru
    let ws = new SockJS(this.serverUrl);
    this.stompClient = Stomp.over(ws);
    let that = this;

    this.stompClient.connect({}, function () {
      that.isLoaded = true;
      that.openGlobalSocket()
    });

  }

  openGlobalSocket() {
    if (this.isLoaded) {
      this.stompClient.subscribe("/socket-publisher", (message: { body: string }) => {
        this.handleResult(message);
      });
    }
  }
  openSocket(username:String) {
    if (this.isLoaded) {
      this.isCustomSocketOpened = true;
      this.stompClient.subscribe("/socket-publisher/" + username, (message: { body: string; }) => {
        this.handleResult(message);
      });
    }
  }

  handleResult(message: { body: string; }) {
    if (message.body) {
      let messageResult: Message = JSON.parse(message.body);
      console.log("MESSAGE : "+message)
      this.messages.push(messageResult);
      this.openSnackBar('New message received : '+messageResult.content, 'Dismiss');
    }
  }

  openSnackBar(message: string, action: string) {
    this.snackBar.open(message, action, {
      duration: 10000,
    });
  }


  getStompClient(): BehaviorSubject<any> {
    return this.stompClient;
    }
}
