import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { NgxWebstorageModule } from 'ngx-webstorage';
import { FormBuilder } from '@angular/forms';
import { of, Subject } from 'rxjs';
import { LoginService } from './login/login.service';
import { AccountService } from './auth/account.service';
import { Account } from './model/account.model';

import { AppComponent } from './app.component';

describe('App Component', () => {
  let comp: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let mockAccountService: AccountService;
  let mockLoginService: LoginService;
  const account: Account = {
    activated: true,
    authorities: [],
    email: '',
    firstName: null,
    langKey: '',
    lastName: null,
    login: 'login',
  };

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [AppComponent],
      imports: [HttpClientTestingModule, NgxWebstorageModule.forRoot()],
      providers: [
        FormBuilder,
        AccountService,
        {
          provide: LoginService,
          useValue: {
            login: jest.fn(() => of({})),
            logout: jest.fn(() => of({})),
          },
        },
      ],
    })
      .overrideTemplate(AppComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AppComponent);
    comp = fixture.componentInstance;
    mockLoginService = TestBed.inject(LoginService);
    mockAccountService = TestBed.inject(AccountService);
  });

  describe('ngOnInit', () => {
    it('should have appName', () => {
      // GIVEN
      const authenticationState = new Subject<Account | null>();
      mockAccountService.getAuthenticationState = jest.fn(() => authenticationState.asObservable());

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.appName).toEqual('angularapp');
      expect(mockAccountService.getAuthenticationState).toHaveBeenCalled();

      // THEN
      expect(comp.account).toBeNull();

      // WHEN
      authenticationState.next(account);

      // THEN
      expect(comp.account).toEqual(account);

      // WHEN
      authenticationState.next(null);

      // THEN
      expect(comp.account).toBeNull();
    });
  });

  describe('login', () => {
    it('should authenticate the user', () => {
      // GIVEN
      const credentials = {
        username: 'admin',
        password: 'admin',
      };

      comp.loginForm.patchValue({
        username: 'admin',
        password: 'admin',
      });

      // WHEN
      comp.login();

      // THEN
      expect(mockLoginService.login).toHaveBeenCalledWith(credentials);
    });
  });

  describe('logout', () => {
    it('should logout the user', () => {
      // WHEN
      comp.logout();

      // THEN
      expect(mockLoginService.logout).toHaveBeenCalled();
    });
  });
});
