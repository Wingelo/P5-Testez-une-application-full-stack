import {HttpClient, HttpClientModule} from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import {Session} from "../interfaces/session.interface";
import {of} from "rxjs";


describe('SessionsService', () => {
  let service: SessionApiService;
  let httpClientMock: any;
  let sessionMock : Session;

  beforeEach(() => {
    httpClientMock = {
      get: jest.fn(),
      delete: jest.fn(),
      post: jest.fn(),
      put: jest.fn(),
    }
    TestBed.configureTestingModule({
      providers: [
        SessionApiService,
        { provide: HttpClient, useValue: httpClientMock },
      ],
      imports:[
        HttpClientModule
      ]
    });
    service = TestBed.inject(SessionApiService);
    sessionMock = {
      id:1,
      name:'Test Session Hello',
      description: 'Test Description Hello',
      date: new Date(),
      teacher_id:1,
      users: [1,2]
    };
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return an Observable<Session[]> for all', () => {
    const sessionsMock: Session[] = [sessionMock];
    httpClientMock.get.mockReturnValue(of(sessionsMock));

    service.all().subscribe((sessions) => {
      expect(sessions).toEqual(sessionsMock);
    });
  });

  it('should return an Observable<Teacher[]> for detail', () => {
    httpClientMock.get.mockReturnValue(of(null));
    service.detail('1').subscribe((response) => {
      expect(response).toBeUndefined()
    });
  });

  it('should return an Observable<Teacher[]> for delete', () => {
    httpClientMock.delete.mockReturnValue(of(null));

    service.delete('1').subscribe((response) => {
      expect(response).toBeUndefined()
    });
  });

  it('should return an Observable<Teacher[]> for create', () => {
    httpClientMock.post.mockReturnValue(of(sessionMock));

    service.create(sessionMock).subscribe((response) => {
      expect(response).toEqual(sessionMock);
    });
  });

  it('should return an Observable<Teacher[]> for update', () => {
    httpClientMock.put.mockReturnValue(of(sessionMock));

    service.update('1',sessionMock).subscribe((response) => {
      expect(response).toEqual(sessionMock);
    });
  });

  it('should return an Observable<Teacher[]> for participate', () => {
    httpClientMock.post.mockReturnValue(of(null));

    service.participate('1','1').subscribe((response) => {
      expect(response).toBeUndefined();
    });
  });

  it('should return an Observable<Teacher[]> for unParticipate', () => {
    httpClientMock.delete.mockReturnValue(of(null));
    service.unParticipate('1','1').subscribe((response) => {
      expect(response).toBeUndefined();
    });
  });
});
