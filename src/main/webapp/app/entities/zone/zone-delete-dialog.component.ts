import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IZone } from 'app/shared/model/zone.model';
import { ZoneService } from './zone.service';

@Component({
  templateUrl: './zone-delete-dialog.component.html',
})
export class ZoneDeleteDialogComponent {
  zone?: IZone;

  constructor(protected zoneService: ZoneService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.zoneService.delete(id).subscribe(() => {
      this.eventManager.broadcast('zoneListModification');
      this.activeModal.close();
    });
  }
}
