import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IItemLocation } from 'app/shared/model/item-location.model';
import { Subscription } from 'rxjs';
import { ItemLocationTrackerService } from 'app/entities/item-location/item-location-tracker.service';
import { ItemLocationTrackerActivityModel } from 'app/entities/item-location/item-location-tracker-activity.model';
import { JhiDataUtils } from 'ng-jhipster';

@Component({
  selector: 'jhi-item-location-detail',
  templateUrl: './item-location-detail.component.html',
})
export class ItemLocationDetailComponent implements OnInit, OnDestroy {
  itemLocation: IItemLocation | null = null;
  subscription?: Subscription;

  constructor(
    protected activatedRoute: ActivatedRoute,
    private itemLocationTrackerService: ItemLocationTrackerService,
    protected dataUtils: JhiDataUtils
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemLocation }) => (this.itemLocation = itemLocation));

    if (this.itemLocation != null && this.itemLocation.epc != null) {
      this.itemLocationTrackerService.connect();
      this.itemLocationTrackerService.subscribe(this.itemLocation.epc);
      this.subscription = this.itemLocationTrackerService.receive().subscribe((activity: ItemLocationTrackerActivityModel) => {
        this.showActivity(activity);
      });
    }
  }

  private showActivity(activity: ItemLocationTrackerActivityModel): void {
    if (this.itemLocation) {
      this.itemLocation.x = activity.x;
      this.itemLocation.y = activity.y;
      this.itemLocation.z = activity.z;
      this.itemLocation.zoneName = activity.zoneName;
      this.itemLocation.lastLocationUpdate = activity.updateTime;
    }
  }

  previousState(): void {
    window.history.back();
  }

  ngOnDestroy(): void {
    this.itemLocationTrackerService.disconnect();
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }
}
