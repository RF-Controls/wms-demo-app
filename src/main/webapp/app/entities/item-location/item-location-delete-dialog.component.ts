import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IItemLocation } from 'app/shared/model/item-location.model';
import { ItemLocationService } from './item-location.service';

@Component({
  templateUrl: './item-location-delete-dialog.component.html',
})
export class ItemLocationDeleteDialogComponent {
  itemLocation?: IItemLocation;

  constructor(
    protected itemLocationService: ItemLocationService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.itemLocationService.delete(id).subscribe(() => {
      this.eventManager.broadcast('itemLocationListModification');
      this.activeModal.close();
    });
  }
}
