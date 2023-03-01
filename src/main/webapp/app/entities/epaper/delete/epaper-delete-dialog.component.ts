import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEpaper } from '../epaper.model';
import { EpaperService } from '../service/epaper.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './epaper-delete-dialog.component.html',
})
export class EpaperDeleteDialogComponent {
  epaper?: IEpaper;

  constructor(protected epaperService: EpaperService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.epaperService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
