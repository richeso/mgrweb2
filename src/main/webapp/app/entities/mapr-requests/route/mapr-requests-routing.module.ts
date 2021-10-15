import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MaprRequestsComponent } from '../list/mapr-requests.component';
import { MaprRequestsDetailComponent } from '../detail/mapr-requests-detail.component';
import { MaprRequestsUpdateComponent } from '../update/mapr-requests-update.component';
import { MaprRequestsRoutingResolveService } from './mapr-requests-routing-resolve.service';

const maprRequestsRoute: Routes = [
  {
    path: '',
    component: MaprRequestsComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MaprRequestsDetailComponent,
    resolve: {
      maprRequests: MaprRequestsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MaprRequestsUpdateComponent,
    resolve: {
      maprRequests: MaprRequestsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MaprRequestsUpdateComponent,
    resolve: {
      maprRequests: MaprRequestsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(maprRequestsRoute)],
  exports: [RouterModule],
})
export class MaprRequestsRoutingModule {}
