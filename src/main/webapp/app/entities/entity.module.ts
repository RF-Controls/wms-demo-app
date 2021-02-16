import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'zone',
        loadChildren: () => import('./zone/zone.module').then(m => m.WmsdemoZoneModule),
      },
      {
        path: 'item',
        loadChildren: () => import('./item/item.module').then(m => m.WmsdemoItemModule),
      },
      {
        path: 'item-location',
        loadChildren: () => import('./item-location/item-location.module').then(m => m.WmsdemoItemLocationModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class WmsdemoEntityModule {}
