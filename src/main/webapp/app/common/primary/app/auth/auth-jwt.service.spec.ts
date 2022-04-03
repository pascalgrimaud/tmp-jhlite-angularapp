import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthServerProvider } from './auth-jwt.service';
import { LocalStorageService, NgxWebstorageModule, SessionStorageService } from 'ngx-webstorage';

describe('Auth JWT', () => {
  let service: AuthServerProvider;
  let localStorageService: LocalStorageService;
  let sessionStorageService: SessionStorageService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, NgxWebstorageModule.forRoot()],
    });

    httpMock = TestBed.inject(HttpTestingController);
    service = TestBed.inject(AuthServerProvider);
    localStorageService = TestBed.inject(LocalStorageService);
    sessionStorageService = TestBed.inject(SessionStorageService);
  });

  describe('Login', () => {
    it('should clear session storage and save in local storage', () => {
      // GIVEN
      localStorageService.store = jest.fn();
      sessionStorageService.clear = jest.fn();

      // WHEN
      service.login({ username: 'John', password: '123' }).subscribe();
      httpMock.expectOne('api/authenticate').flush({ id_token: '1' });

      // THEN
      httpMock.verify();
      expect(localStorageService.store).toHaveBeenCalledWith('authenticationToken', '1');
      expect(sessionStorageService.clear).toHaveBeenCalled();
    });
  });

  describe('Logout', () => {
    it('should clear storage', () => {
      // GIVEN
      sessionStorageService.clear = jest.fn();
      localStorageService.clear = jest.fn();

      // WHEN
      service.logout().subscribe();

      // THEN
      expect(localStorageService.clear).toHaveBeenCalled();
      expect(sessionStorageService.clear).toHaveBeenCalled();
    });
  });
});
