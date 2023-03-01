import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EpaperComponent } from './list/epaper.component';
import { EpaperDetailComponent } from './detail/epaper-detail.component';
import { EpaperUpdateComponent } from './update/epaper-update.component';
import { EpaperDeleteDialogComponent } from './delete/epaper-delete-dialog.component';
import { EpaperRoutingModule } from './route/epaper-routing.module';

@NgModule({
  imports: [SharedModule, EpaperRoutingModule],
  declarations: [EpaperComponent, EpaperDetailComponent, EpaperUpdateComponent, EpaperDeleteDialogComponent],
})
export class EpaperModule {}
