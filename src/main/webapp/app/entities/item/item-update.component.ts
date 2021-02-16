import { Component, OnInit, ElementRef, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IItem, Item } from 'app/shared/model/item.model';
import { ItemService } from './item.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { ItemAssociationService } from 'app/entities/item/item-association.service';
import { ItemAssociationActivityModel } from 'app/entities/item/item-association-activity.model';

@Component({
  selector: 'jhi-item-update',
  templateUrl: './item-update.component.html',
})
export class ItemUpdateComponent implements OnInit, OnDestroy {
  isSaving = false;
  subscription?: Subscription;
  epcsInZone = new Set();
  updatedEpcsInZone = new Set();
  numberOfEpcsInZone = 0;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    epc: [null, [Validators.required]],
    description: [null, [Validators.required]],
    description2: [],
    weight: [],
    thumbnail: [],
    thumbnailContentType: [],
    detailImage: [],
    detailImageContentType: [],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected itemService: ItemService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private itemAssociationService: ItemAssociationService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ item }) => {
      this.updateForm(item);
    });

    this.itemAssociationService.connect();
    this.itemAssociationService.subscribe();
    this.subscription = this.itemAssociationService.receive().subscribe((activity: ItemAssociationActivityModel) => {
      this.showActivity(activity);
    });
  }

  updateForm(item: IItem): void {
    this.editForm.patchValue({
      id: item.id,
      name: item.name,
      epc: item.epc,
      description: item.description,
      description2: item.description2,
      weight: item.weight,
      thumbnail: item.thumbnail,
      thumbnailContentType: item.thumbnailContentType,
      detailImage: item.detailImage,
      detailImageContentType: item.detailImageContentType,
    });
  }

  showActivity(activity: ItemAssociationActivityModel): void {
    const curNumEpcs = this.updatedEpcsInZone.size;
    this.updatedEpcsInZone.add(activity.epc);

    if (curNumEpcs !== this.updatedEpcsInZone.size) {
      this.epcsInZone.add(activity.epc);
      console.log('Got the activity way up here! ' + activity.epc);
    }
  }

  setAssociationEpc(epc: string): void {
    console.log('You picked ' + epc);
    this.editForm.patchValue({
      epc,
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: any, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('wmsdemoApp.error', { ...err, key: 'error.file.' + err.key })
      );
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (this.elementRef && idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const item = this.createFromForm();
    if (item.id !== undefined) {
      this.subscribeToSaveResponse(this.itemService.update(item));
    } else {
      this.subscribeToSaveResponse(this.itemService.create(item));
    }
  }

  private createFromForm(): IItem {
    return {
      ...new Item(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      epc: this.editForm.get(['epc'])!.value,
      description: this.editForm.get(['description'])!.value,
      description2: this.editForm.get(['description2'])!.value,
      weight: this.editForm.get(['weight'])!.value,
      thumbnailContentType: this.editForm.get(['thumbnailContentType'])!.value,
      thumbnail: this.editForm.get(['thumbnail'])!.value,
      detailImageContentType: this.editForm.get(['detailImageContentType'])!.value,
      detailImage: this.editForm.get(['detailImage'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IItem>>): void {
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

  ngOnDestroy(): void {
    this.itemAssociationService.disconnect();
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
