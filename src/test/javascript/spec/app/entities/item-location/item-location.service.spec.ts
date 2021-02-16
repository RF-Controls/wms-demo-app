import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ItemLocationService } from 'app/entities/item-location/item-location.service';
import { IItemLocation, ItemLocation } from 'app/shared/model/item-location.model';

describe('Service Tests', () => {
  describe('ItemLocation Service', () => {
    let injector: TestBed;
    let service: ItemLocationService;
    let httpMock: HttpTestingController;
    let elemDefault: IItemLocation;
    let expectedResult: IItemLocation | IItemLocation[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(ItemLocationService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new ItemLocation(0, currentDate, currentDate, 'AAAAAAA', 0, 0, 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            lastLocationUpdate: currentDate.format(DATE_TIME_FORMAT),
            zoneEnterInstant: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ItemLocation', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            lastLocationUpdate: currentDate.format(DATE_TIME_FORMAT),
            zoneEnterInstant: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            lastLocationUpdate: currentDate,
            zoneEnterInstant: currentDate,
          },
          returnedFromService
        );

        service.create(new ItemLocation()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ItemLocation', () => {
        const returnedFromService = Object.assign(
          {
            lastLocationUpdate: currentDate.format(DATE_TIME_FORMAT),
            zoneEnterInstant: currentDate.format(DATE_TIME_FORMAT),
            description: 'BBBBBB',
            x: 1,
            y: 1,
            z: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            lastLocationUpdate: currentDate,
            zoneEnterInstant: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ItemLocation', () => {
        const returnedFromService = Object.assign(
          {
            lastLocationUpdate: currentDate.format(DATE_TIME_FORMAT),
            zoneEnterInstant: currentDate.format(DATE_TIME_FORMAT),
            description: 'BBBBBB',
            x: 1,
            y: 1,
            z: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            lastLocationUpdate: currentDate,
            zoneEnterInstant: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ItemLocation', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
