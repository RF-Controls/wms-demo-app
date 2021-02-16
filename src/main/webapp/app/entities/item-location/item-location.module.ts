import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { WmsdemoSharedModule } from 'app/shared/shared.module';
import { ItemLocationComponent } from './item-location.component';
import { ItemLocationDetailComponent } from './item-location-detail.component';
import { ItemLocationUpdateComponent } from './item-location-update.component';
import { ItemLocationDeleteDialogComponent } from './item-location-delete-dialog.component';
import { itemLocationRoute } from './item-location.route';

@NgModule({
  imports: [WmsdemoSharedModule, RouterModule.forChild(itemLocationRoute)],
  declarations: [ItemLocationComponent, ItemLocationDetailComponent, ItemLocationUpdateComponent, ItemLocationDeleteDialogComponent],
  entryComponents: [ItemLocationDeleteDialogComponent],
})
export class WmsdemoItemLocationModule {}
