import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IItemLocation } from 'app/shared/model/item-location.model';

type EntityResponseType = HttpResponse<IItemLocation>;
type EntityArrayResponseType = HttpResponse<IItemLocation[]>;

@Injectable({ providedIn: 'root' })
export class ItemLocationService {
  public resourceUrl = SERVER_API_URL + 'api/item-locations';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/item-locations';

  constructor(protected http: HttpClient) {}

  create(itemLocation: IItemLocation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(itemLocation);
    return this.http
      .post<IItemLocation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(itemLocation: IItemLocation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(itemLocation);
    return this.http
      .put<IItemLocation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IItemLocation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IItemLocation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IItemLocation[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(itemLocation: IItemLocation): IItemLocation {
    const copy: IItemLocation = Object.assign({}, itemLocation, {
      lastLocationUpdate:
        itemLocation.lastLocationUpdate && itemLocation.lastLocationUpdate.isValid() ? itemLocation.lastLocationUpdate.toJSON() : undefined,
      zoneEnterInstant:
        itemLocation.zoneEnterInstant && itemLocation.zoneEnterInstant.isValid() ? itemLocation.zoneEnterInstant.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.lastLocationUpdate = res.body.lastLocationUpdate ? moment(res.body.lastLocationUpdate) : undefined;
      res.body.zoneEnterInstant = res.body.zoneEnterInstant ? moment(res.body.zoneEnterInstant) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((itemLocation: IItemLocation) => {
        itemLocation.lastLocationUpdate = itemLocation.lastLocationUpdate ? moment(itemLocation.lastLocationUpdate) : undefined;
        itemLocation.zoneEnterInstant = itemLocation.zoneEnterInstant ? moment(itemLocation.zoneEnterInstant) : undefined;
      });
    }
    return res;
  }
}
