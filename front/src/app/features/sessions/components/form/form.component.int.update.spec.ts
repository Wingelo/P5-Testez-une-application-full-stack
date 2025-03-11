import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ReactiveFormsModule} from '@angular/forms';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {MatSelectModule} from '@angular/material/select';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {RouterTestingModule} from '@angular/router/testing';
import {expect, jest} from '@jest/globals';
import {SessionService} from 'src/app/services/session.service';
import {SessionApiService} from '../../services/session-api.service';

import {FormComponent} from './form.component';
import {of} from "rxjs";
import {Session} from "../../interfaces/session.interface";
import {ActivatedRoute, convertToParamMap, Router} from "@angular/router";

describe('FormComponent with update', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let matSnackBar: MatSnackBar;
  let router: Router;
  let sessionApiService: SessionApiService;
  let sessionService: SessionService;
  let session: Session;

  const mockSessionService = {
    sessionInformation: {
      admin: false
    }
  }

  class MockSnackBar {
    open() {
      return {
        onAction: () => of({}),
      };
    }
  }

  class MockRouter {
    get url(): string {
      return 'update';
    }

    navigate(): Promise<boolean> {
      return new Promise<boolean>((resolve, _) => resolve(true));
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FormComponent],
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        {provide: Router, useClass: MockRouter},
        {provide: SessionService, useValue: mockSessionService},
        {provide: MatSnackBar, useClass: MockSnackBar},
        {
          provide: ActivatedRoute, useValue: {snapshot: {paramMap: convertToParamMap({id: '1'})}},
        },
        SessionApiService,
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    sessionApiService = TestBed.inject(SessionApiService);
    sessionService = TestBed.inject(SessionService);
    matSnackBar = TestBed.inject(MatSnackBar);
    router = TestBed.inject(Router);

    fixture.detectChanges();

    session = {
      name: 'Séance Essaie',
      description: 'Séance de musculation',
      date: new Date('2024-12-03'),
      teacher_id: 3,
      users: [],
      createdAt: new Date(),
      updatedAt: new Date()
    };
  });

  it('should call submit form to update session', () => {
    component.onUpdate = true;

    const spySessionApiServiceUpdate = jest.spyOn(sessionApiService, 'update').mockReturnValue(of(session));
    const spySessionApiServiceDetail = jest.spyOn(sessionApiService, 'detail').mockReturnValue(of(session));
    const spyMatSnackBar = jest.spyOn(matSnackBar, 'open');
    const spyRouter = jest.spyOn(router, 'navigate');

    component.ngOnInit();

    expect(component.sessionForm?.get('name')?.value).toEqual('Séance Essaie');
    expect(component.sessionForm?.get('date')?.value).toEqual('2024-12-03');
    expect(component.sessionForm?.get('teacher_id')?.value).toEqual(3);
    expect(component.sessionForm?.get('description')?.value).toEqual('Séance de musculation');
    expect(spySessionApiServiceDetail).toHaveBeenCalled();

    component.submit();

    expect(spySessionApiServiceUpdate).toHaveBeenCalled();
    expect(spyMatSnackBar).toHaveBeenCalledWith('Session updated !', 'Close', {duration: 3000});
    expect(spyRouter).toHaveBeenCalledWith(['sessions']);
  });
});
