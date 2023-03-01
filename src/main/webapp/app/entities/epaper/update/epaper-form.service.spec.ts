import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../epaper.test-samples';

import { EpaperFormService } from './epaper-form.service';

describe('Epaper Form Service', () => {
  let service: EpaperFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EpaperFormService);
  });

  describe('Service methods', () => {
    describe('createEpaperFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEpaperFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            newspaperName: expect.any(Object),
            width: expect.any(Object),
            height: expect.any(Object),
            dpi: expect.any(Object),
            uploadtime: expect.any(Object),
            filename: expect.any(Object),
          })
        );
      });

      it('passing IEpaper should create a new form with FormGroup', () => {
        const formGroup = service.createEpaperFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            newspaperName: expect.any(Object),
            width: expect.any(Object),
            height: expect.any(Object),
            dpi: expect.any(Object),
            uploadtime: expect.any(Object),
            filename: expect.any(Object),
          })
        );
      });
    });

    describe('getEpaper', () => {
      it('should return NewEpaper for default Epaper initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEpaperFormGroup(sampleWithNewData);

        const epaper = service.getEpaper(formGroup) as any;

        expect(epaper).toMatchObject(sampleWithNewData);
      });

      it('should return NewEpaper for empty Epaper initial value', () => {
        const formGroup = service.createEpaperFormGroup();

        const epaper = service.getEpaper(formGroup) as any;

        expect(epaper).toMatchObject({});
      });

      it('should return IEpaper', () => {
        const formGroup = service.createEpaperFormGroup(sampleWithRequiredData);

        const epaper = service.getEpaper(formGroup) as any;

        expect(epaper).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEpaper should not enable id FormControl', () => {
        const formGroup = service.createEpaperFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEpaper should disable id FormControl', () => {
        const formGroup = service.createEpaperFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
