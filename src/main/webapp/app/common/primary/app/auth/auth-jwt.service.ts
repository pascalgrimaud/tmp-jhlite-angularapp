import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { LocalStorageService, SessionStorageService } from 'ngx-webstorage';

import { Login } from '../model/login.model';

type JwtToken = {
  id_token: string;
};

@Injectable({ providedIn: 'root' })
export class AuthServerProvider {
  constructor(
    private http: HttpClient,
    private localStorageService: LocalStorageService,
    private sessionStorageService: SessionStorageService
  ) {}

  login(credentials: Login): Observable<void> {
    return this.http.post<JwtToken>('api/authenticate', credentials).pipe(map(response => this.authenticateSuccess(response)));
  }

  logout(): Observable<void> {
    return new Observable(observer => {
      this.localStorageService.clear('authenticationToken');
      this.sessionStorageService.clear('authenticationToken');
      observer.complete();
    });
  }

  private authenticateSuccess(response: JwtToken): void {
    const jwt = response.id_token;
    this.localStorageService.store('authenticationToken', jwt);
    this.sessionStorageService.clear('authenticationToken');
  }
}
