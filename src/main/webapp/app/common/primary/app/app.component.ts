import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';
import { AccountService } from './auth/account.service';
import { LoginService } from './login/login.service';
import { Account } from './model/account.model';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit, OnDestroy {
  appName = '';

  private readonly destroy$ = new Subject<void>();

  account: Account | null = null;

  loginForm = this.fb.group({
    username: [null, [Validators.required]],
    password: [null, [Validators.required]],
  });

  constructor(private accountService: AccountService, private loginService: LoginService, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.appName = 'angularapp';
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  login(): void {
    this.loginService
      .login({
        username: this.loginForm.get('username')!.value,
        password: this.loginForm.get('password')!.value,
      })
      .subscribe();
  }

  logout(): void {
    this.loginService.logout();
  }
}
