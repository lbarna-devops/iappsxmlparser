import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { EpaperFormService, EpaperFormGroup } from './epaper-form.service';
import { IEpaper } from '../epaper.model';
import { EpaperService } from '../service/epaper.service';

@Component({
  selector: 'jhi-epaper-update',
  templateUrl: './epaper-update.component.html',
})
export class EpaperUpdateComponent implements OnInit {
  isSaving = false;
  epaper: IEpaper | null = null;

  editForm: EpaperFormGroup = this.epaperFormService.createEpaperFormGroup();

  constructor(
    protected epaperService: EpaperService,
    protected epaperFormService: EpaperFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ epaper }) => {
      this.epaper = epaper;
      if (epaper) {
        this.updateForm(epaper);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const epaper = this.epaperFormService.getEpaper(this.editForm);
    if (epaper.id !== null) {
      this.subscribeToSaveResponse(this.epaperService.update(epaper));
    } else {
      this.subscribeToSaveResponse(this.epaperService.create(epaper));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEpaper>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(epaper: IEpaper): void {
    this.epaper = epaper;
    this.epaperFormService.resetForm(this.editForm, epaper);
  }
}
