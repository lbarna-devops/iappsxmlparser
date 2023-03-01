import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EpaperFormService } from './epaper-form.service';
import { EpaperService } from '../service/epaper.service';
import { IEpaper } from '../epaper.model';

import { EpaperUpdateComponent } from './epaper-update.component';

describe('Epaper Management Update Component', () => {
  let comp: EpaperUpdateComponent;
  let fixture: ComponentFixture<EpaperUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let epaperFormService: EpaperFormService;
  let epaperService: EpaperService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EpaperUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(EpaperUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EpaperUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    epaperFormService = TestBed.inject(EpaperFormService);
    epaperService = TestBed.inject(EpaperService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const epaper: IEpaper = { id: 456 };

      activatedRoute.data = of({ epaper });
      comp.ngOnInit();

      expect(comp.epaper).toEqual(epaper);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEpaper>>();
      const epaper = { id: 123 };
      jest.spyOn(epaperFormService, 'getEpaper').mockReturnValue(epaper);
      jest.spyOn(epaperService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ epaper });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: epaper }));
      saveSubject.complete();

      // THEN
      expect(epaperFormService.getEpaper).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(epaperService.update).toHaveBeenCalledWith(expect.objectContaining(epaper));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEpaper>>();
      const epaper = { id: 123 };
      jest.spyOn(epaperFormService, 'getEpaper').mockReturnValue({ id: null });
      jest.spyOn(epaperService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ epaper: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: epaper }));
      saveSubject.complete();

      // THEN
      expect(epaperFormService.getEpaper).toHaveBeenCalled();
      expect(epaperService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEpaper>>();
      const epaper = { id: 123 };
      jest.spyOn(epaperService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ epaper });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(epaperService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
