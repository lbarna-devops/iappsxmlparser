import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEpaper } from '../epaper.model';

@Component({
  selector: 'jhi-epaper-detail',
  templateUrl: './epaper-detail.component.html',
})
export class EpaperDetailComponent implements OnInit {
  epaper: IEpaper | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ epaper }) => {
      this.epaper = epaper;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
