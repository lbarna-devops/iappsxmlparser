import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEpaper } from '../epaper.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../epaper.test-samples';

import { EpaperService } from './epaper.service';

const requireRestSample: IEpaper = {
  ...sampleWithRequiredData,
};

describe('Epaper Service', () => {
  let service: EpaperService;
  let httpMock: HttpTestingController;
  let expectedResult: IEpaper | IEpaper[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EpaperService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Epaper', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const epaper = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(epaper).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Epaper', () => {
      const epaper = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(epaper).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Epaper', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Epaper', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Epaper', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEpaperToCollectionIfMissing', () => {
      it('should add a Epaper to an empty array', () => {
        const epaper: IEpaper = sampleWithRequiredData;
        expectedResult = service.addEpaperToCollectionIfMissing([], epaper);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(epaper);
      });

      it('should not add a Epaper to an array that contains it', () => {
        const epaper: IEpaper = sampleWithRequiredData;
        const epaperCollection: IEpaper[] = [
          {
            ...epaper,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEpaperToCollectionIfMissing(epaperCollection, epaper);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Epaper to an array that doesn't contain it", () => {
        const epaper: IEpaper = sampleWithRequiredData;
        const epaperCollection: IEpaper[] = [sampleWithPartialData];
        expectedResult = service.addEpaperToCollectionIfMissing(epaperCollection, epaper);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(epaper);
      });

      it('should add only unique Epaper to an array', () => {
        const epaperArray: IEpaper[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const epaperCollection: IEpaper[] = [sampleWithRequiredData];
        expectedResult = service.addEpaperToCollectionIfMissing(epaperCollection, ...epaperArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const epaper: IEpaper = sampleWithRequiredData;
        const epaper2: IEpaper = sampleWithPartialData;
        expectedResult = service.addEpaperToCollectionIfMissing([], epaper, epaper2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(epaper);
        expect(expectedResult).toContain(epaper2);
      });

      it('should accept null and undefined values', () => {
        const epaper: IEpaper = sampleWithRequiredData;
        expectedResult = service.addEpaperToCollectionIfMissing([], null, epaper, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(epaper);
      });

      it('should return initial array if no Epaper is added', () => {
        const epaperCollection: IEpaper[] = [sampleWithRequiredData];
        expectedResult = service.addEpaperToCollectionIfMissing(epaperCollection, undefined, null);
        expect(expectedResult).toEqual(epaperCollection);
      });
    });

    describe('compareEpaper', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEpaper(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEpaper(entity1, entity2);
        const compareResult2 = service.compareEpaper(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEpaper(entity1, entity2);
        const compareResult2 = service.compareEpaper(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEpaper(entity1, entity2);
        const compareResult2 = service.compareEpaper(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
