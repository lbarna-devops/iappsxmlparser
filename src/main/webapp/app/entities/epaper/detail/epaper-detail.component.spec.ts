import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EpaperDetailComponent } from './epaper-detail.component';

describe('Epaper Management Detail Component', () => {
  let comp: EpaperDetailComponent;
  let fixture: ComponentFixture<EpaperDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EpaperDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ epaper: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EpaperDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EpaperDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load epaper on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.epaper).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
