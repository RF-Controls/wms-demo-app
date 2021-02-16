import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IItemLocation, ItemLocation } from 'app/shared/model/item-location.model';
import { ItemLocationService } from './item-location.service';
import { ItemLocationComponent } from './item-location.component';
import { ItemLocationDetailComponent } from './item-location-detail.component';
import { ItemLocationUpdateComponent } from './item-location-update.component';

@Injectable({ providedIn: 'root' })
export class ItemLocationResolve implements Resolve<IItemLocation> {
  constructor(private service: ItemLocationService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IItemLocation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((itemLocation: HttpResponse<ItemLocation>) => {
          if (itemLocation.body) {
            return of(itemLocation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ItemLocation());
  }
}

export const itemLocationRoute: Routes = [
  {
    path: '',
    component: ItemLocationComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'wmsdemoApp.itemLocation.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ItemLocationDetailComponent,
    resolve: {
      itemLocation: ItemLocationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'wmsdemoApp.itemLocation.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ItemLocationUpdateComponent,
    resolve: {
      itemLocation: ItemLocationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'wmsdemoApp.itemLocation.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ItemLocationUpdateComponent,
    resolve: {
      itemLocation: ItemLocationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'wmsdemoApp.itemLocation.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
