import { Injectable } from '@angular/core';
import { Location } from '@angular/common';
import { Router } from '@angular/router';
import { ReplaySubject, Subject, Subscription } from 'rxjs';
import * as SockJS from 'sockjs-client';
import * as Stomp from 'webstomp-client';

import { AuthServerProvider } from 'app/core/auth/auth-jwt.service';
import { ItemLocationTrackerActivityModel } from 'app/entities/item-location/item-location-tracker-activity.model';

@Injectable({ providedIn: 'root' })
export class ItemLocationTrackerService {
  private stompClient: Stomp.Client | null = null;
  private routerSubscription: Subscription | null = null;
  private connectionSubject: ReplaySubject<void> = new ReplaySubject(1);
  private connectionSubscription: Subscription | null = null;
  private stompSubscription: Stomp.Subscription | null = null;
  private listenerSubject: Subject<ItemLocationTrackerActivityModel> = new Subject();

  constructor(private router: Router, private authServerProvider: AuthServerProvider, private location: Location) {}

  connect(): void {
    if (this.stompClient && this.stompClient.connected) {
      return;
    }

    // building absolute path so that websocket doesn't fail when deploying with a context path
    let url = '/websocket/tracker';
    url = this.location.prepareExternalUrl(url);
    const authToken = this.authServerProvider.getToken();
    if (authToken) {
      url += '?access_token=' + authToken;
    }
    const socket: WebSocket = new SockJS(url);
    this.stompClient = Stomp.over(socket);
    const headers: Stomp.ConnectionHeaders = {};
    this.stompClient.connect(headers, () => {
      this.connectionSubject.next();
    });
  }

  disconnect(): void {
    this.unsubscribe();

    this.connectionSubject = new ReplaySubject(1);

    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
      this.routerSubscription = null;
    }

    if (this.stompClient) {
      if (this.stompClient.connected) {
        this.stompClient.disconnect();
      }
      this.stompClient = null;
    }
  }

  receive(): Subject<ItemLocationTrackerActivityModel> {
    return this.listenerSubject;
  }

  subscribe(epc: string): void {
    if (this.connectionSubscription) {
      return;
    }

    this.connectionSubscription = this.connectionSubject.subscribe(() => {
      if (this.stompClient) {
        this.stompSubscription = this.stompClient.subscribe('/topic/itemLocationUpdate.' + epc, (data: Stomp.Message) => {
          this.listenerSubject.next(JSON.parse(data.body));
        });
      }
    });
  }

  unsubscribe(): void {
    if (this.stompSubscription) {
      this.stompSubscription.unsubscribe();
      this.stompSubscription = null;
    }

    if (this.connectionSubscription) {
      this.connectionSubscription.unsubscribe();
      this.connectionSubscription = null;
    }
  }
}
