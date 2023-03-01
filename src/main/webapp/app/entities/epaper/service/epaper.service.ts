import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEpaper, NewEpaper } from '../epaper.model';

export type PartialUpdateEpaper = Partial<IEpaper> & Pick<IEpaper, 'id'>;

export type EntityResponseType = HttpResponse<IEpaper>;
export type EntityArrayResponseType = HttpResponse<IEpaper[]>;

@Injectable({ providedIn: 'root' })
export class EpaperService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/epapers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(epaper: NewEpaper): Observable<EntityResponseType> {
    return this.http.post<IEpaper>(this.resourceUrl, epaper, { observe: 'response' });
  }

  update(epaper: IEpaper): Observable<EntityResponseType> {
    return this.http.put<IEpaper>(`${this.resourceUrl}/${this.getEpaperIdentifier(epaper)}`, epaper, { observe: 'response' });
  }

  partialUpdate(epaper: PartialUpdateEpaper): Observable<EntityResponseType> {
    return this.http.patch<IEpaper>(`${this.resourceUrl}/${this.getEpaperIdentifier(epaper)}`, epaper, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEpaper>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEpaper[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEpaperIdentifier(epaper: Pick<IEpaper, 'id'>): number {
    return epaper.id;
  }

  compareEpaper(o1: Pick<IEpaper, 'id'> | null, o2: Pick<IEpaper, 'id'> | null): boolean {
    return o1 && o2 ? this.getEpaperIdentifier(o1) === this.getEpaperIdentifier(o2) : o1 === o2;
  }

  addEpaperToCollectionIfMissing<Type extends Pick<IEpaper, 'id'>>(
    epaperCollection: Type[],
    ...epapersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const epapers: Type[] = epapersToCheck.filter(isPresent);
    if (epapers.length > 0) {
      const epaperCollectionIdentifiers = epaperCollection.map(epaperItem => this.getEpaperIdentifier(epaperItem)!);
      const epapersToAdd = epapers.filter(epaperItem => {
        const epaperIdentifier = this.getEpaperIdentifier(epaperItem);
        if (epaperCollectionIdentifiers.includes(epaperIdentifier)) {
          return false;
        }
        epaperCollectionIdentifiers.push(epaperIdentifier);
        return true;
      });
      return [...epapersToAdd, ...epaperCollection];
    }
    return epaperCollection;
  }
}
