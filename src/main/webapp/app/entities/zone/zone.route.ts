import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IZone, Zone } from 'app/shared/model/zone.model';
import { ZoneService } from './zone.service';
import { ZoneComponent } from './zone.component';
import { ZoneDetailComponent } from './zone-detail.component';
import { ZoneUpdateComponent } from './zone-update.component';

@Injectable({ providedIn: 'root' })
export class ZoneResolve implements Resolve<IZone> {
  constructor(private service: ZoneService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IZone> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((zone: HttpResponse<Zone>) => {
          if (zone.body) {
            return of(zone.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Zone());
  }
}

export const zoneRoute: Routes = [
  {
    path: '',
    component: ZoneComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'wmsdemoApp.zone.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ZoneDetailComponent,
    resolve: {
      zone: ZoneResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'wmsdemoApp.zone.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ZoneUpdateComponent,
    resolve: {
      zone: ZoneResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'wmsdemoApp.zone.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ZoneUpdateComponent,
    resolve: {
      zone: ZoneResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'wmsdemoApp.zone.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
