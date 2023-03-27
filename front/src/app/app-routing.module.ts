import {NgModule} from '@angular/core';
import {PreloadAllModules, RouterModule, Routes} from '@angular/router';
import {AnonymousGuard} from "./guards/auth/anonymous.guard";
import {AdminGuard} from "./guards/auth/admin.guard";
import {OwnerGuard} from "./guards/auth/owner.guard";
import {HomePageComponent} from "./pages/home-page/home-page.component";
import {HomepageGuard} from "./guards/homepage.guard";

const routes: Routes = [
  {
    path: 'anon',
    canActivate: [AnonymousGuard],
    loadChildren: () => import('./modules/anonymous/anonymous.module').then(m => m.AnonymousModule)
  },
  {
    path: 'admin',
    canActivate: [AdminGuard],
    loadChildren: () => import('./modules/admin/admin.module').then(m => m.AdminModule)
  },
  {
    path: 'owner',
    canActivate: [OwnerGuard],
    loadChildren: () => import('./modules/owner/owner.module').then(m => m.OwnerModule)
  },
  {
    path: '**',
    component: HomePageComponent,
    canActivate: [HomepageGuard],
  }
];

@NgModule({
  imports: [
    RouterModule.forRoot(
      routes,
      {
        preloadingStrategy: PreloadAllModules
      }
    )
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
