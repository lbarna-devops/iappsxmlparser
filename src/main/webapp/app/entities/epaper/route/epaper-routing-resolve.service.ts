import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEpaper } from '../epaper.model';
import { EpaperService } from '../service/epaper.service';

@Injectable({ providedIn: 'root' })
export class EpaperRoutingResolveService implements Resolve<IEpaper | null> {
  constructor(protected service: EpaperService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEpaper | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((epaper: HttpResponse<IEpaper>) => {
          if (epaper.body) {
            return of(epaper.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
