import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EpaperComponent } from '../list/epaper.component';
import { EpaperDetailComponent } from '../detail/epaper-detail.component';
import { EpaperUpdateComponent } from '../update/epaper-update.component';
import { EpaperRoutingResolveService } from './epaper-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const epaperRoute: Routes = [
  {
    path: '',
    component: EpaperComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EpaperDetailComponent,
    resolve: {
      epaper: EpaperRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EpaperUpdateComponent,
    resolve: {
      epaper: EpaperRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EpaperUpdateComponent,
    resolve: {
      epaper: EpaperRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(epaperRoute)],
  exports: [RouterModule],
})
export class EpaperRoutingModule {}
