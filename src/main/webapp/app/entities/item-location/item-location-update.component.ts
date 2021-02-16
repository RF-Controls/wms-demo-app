import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IItemLocation, ItemLocation } from 'app/shared/model/item-location.model';
import { ItemLocationService } from './item-location.service';
import { IItem } from 'app/shared/model/item.model';
import { ItemService } from 'app/entities/item/item.service';
import { IZone } from 'app/shared/model/zone.model';
import { ZoneService } from 'app/entities/zone/zone.service';

type SelectableEntity = IItem | IZone;

@Component({
  selector: 'jhi-item-location-update',
  templateUrl: './item-location-update.component.html',
})
export class ItemLocationUpdateComponent implements OnInit {
  isSaving = false;
  items: IItem[] = [];
  zones: IZone[] = [];

  editForm = this.fb.group({
    id: [],
    lastLocationUpdate: [null, [Validators.required]],
    zoneEnterInstant: [],
    description: [],
    x: [null, [Validators.required]],
    y: [null, [Validators.required]],
    z: [null, [Validators.required]],
    itemId: [],
    zoneId: [],
  });

  constructor(
    protected itemLocationService: ItemLocationService,
    protected itemService: ItemService,
    protected zoneService: ZoneService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemLocation }) => {
      if (!itemLocation.id) {
        const today = moment().startOf('day');
        itemLocation.lastLocationUpdate = today;
        itemLocation.zoneEnterInstant = today;
      }

      this.updateForm(itemLocation);

      this.itemService
        .query({ filter: 'itemlocation-is-null' })
        .pipe(
          map((res: HttpResponse<IItem[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IItem[]) => {
          if (!itemLocation.itemId) {
            this.items = resBody;
          } else {
            this.itemService
              .find(itemLocation.itemId)
              .pipe(
                map((subRes: HttpResponse<IItem>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IItem[]) => (this.items = concatRes));
          }
        });

      this.zoneService.query().subscribe((res: HttpResponse<IZone[]>) => (this.zones = res.body || []));
    });
  }

  updateForm(itemLocation: IItemLocation): void {
    this.editForm.patchValue({
      id: itemLocation.id,
      lastLocationUpdate: itemLocation.lastLocationUpdate ? itemLocation.lastLocationUpdate.format(DATE_TIME_FORMAT) : null,
      zoneEnterInstant: itemLocation.zoneEnterInstant ? itemLocation.zoneEnterInstant.format(DATE_TIME_FORMAT) : null,
      description: itemLocation.description,
      x: itemLocation.x,
      y: itemLocation.y,
      z: itemLocation.z,
      itemId: itemLocation.itemId,
      zoneId: itemLocation.zoneId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const itemLocation = this.createFromForm();
    if (itemLocation.id !== undefined) {
      this.subscribeToSaveResponse(this.itemLocationService.update(itemLocation));
    } else {
      this.subscribeToSaveResponse(this.itemLocationService.create(itemLocation));
    }
  }

  private createFromForm(): IItemLocation {
    return {
      ...new ItemLocation(),
      id: this.editForm.get(['id'])!.value,
      lastLocationUpdate: this.editForm.get(['lastLocationUpdate'])!.value
        ? moment(this.editForm.get(['lastLocationUpdate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      zoneEnterInstant: this.editForm.get(['zoneEnterInstant'])!.value
        ? moment(this.editForm.get(['zoneEnterInstant'])!.value, DATE_TIME_FORMAT)
        : undefined,
      description: this.editForm.get(['description'])!.value,
      x: this.editForm.get(['x'])!.value,
      y: this.editForm.get(['y'])!.value,
      z: this.editForm.get(['z'])!.value,
      itemId: this.editForm.get(['itemId'])!.value,
      zoneId: this.editForm.get(['zoneId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IItemLocation>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
