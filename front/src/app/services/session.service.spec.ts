import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should be initialized with isLogged as false', () => {
    expect(service.isLogged).toBeFalsy();
  })

  it('should set isLogged to true and update sessionInformation on logIn', () => {
    const mockSessionInformation = {
      token: 'string',
      type: 'string',
      id: 1,
      username: 'John',
      firstName: 'John',
      lastName: 'Doe',
      admin: false,
    };
    service.logIn(mockSessionInformation)
    expect(service.isLogged).toBeTruthy();
    expect(service.sessionInformation).toEqual(mockSessionInformation);
  })

  it('should set isLogged to false and reset sessionInformation on logOut', () => {
    service.logOut();
    expect(service.isLogged).toBeFalsy();
    expect(service.sessionInformation).toBeUndefined()
  });

  it('should return an Observable that emits the correct value isLogged', (done) =>{
    service.$isLogged().subscribe((val) => {
      expect(val).toBe(service.isLogged)
      done();
    })
  })

});
