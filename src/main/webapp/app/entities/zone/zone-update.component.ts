import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IZone, Zone } from 'app/shared/model/zone.model';
import { ZoneService } from './zone.service';

@Component({
  selector: 'jhi-zone-update',
  templateUrl: './zone-update.component.html',
})
export class ZoneUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [],
    maxItems: [],
    x1: [null, [Validators.required]],
    y1: [null, [Validators.required]],
    z1: [null, [Validators.required]],
    x2: [null, [Validators.required]],
    y2: [null, [Validators.required]],
    z2: [null, [Validators.required]],
    associationZone: [null, [Validators.required]],
  });

  constructor(protected zoneService: ZoneService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ zone }) => {
      this.updateForm(zone);
    });
  }

  updateForm(zone: IZone): void {
    this.editForm.patchValue({
      id: zone.id,
      name: zone.name,
      description: zone.description,
      maxItems: zone.maxItems,
      x1: zone.x1,
      y1: zone.y1,
      z1: zone.z1,
      x2: zone.x2,
      y2: zone.y2,
      z2: zone.z2,
      associationZone: zone.associationZone,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const zone = this.createFromForm();
    if (zone.id !== undefined) {
      this.subscribeToSaveResponse(this.zoneService.update(zone));
    } else {
      this.subscribeToSaveResponse(this.zoneService.create(zone));
    }
  }

  private createFromForm(): IZone {
    return {
      ...new Zone(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      maxItems: this.editForm.get(['maxItems'])!.value,
      x1: this.editForm.get(['x1'])!.value,
      y1: this.editForm.get(['y1'])!.value,
      z1: this.editForm.get(['z1'])!.value,
      x2: this.editForm.get(['x2'])!.value,
      y2: this.editForm.get(['y2'])!.value,
      z2: this.editForm.get(['z2'])!.value,
      associationZone: this.editForm.get(['associationZone'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IZone>>): void {
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
}
