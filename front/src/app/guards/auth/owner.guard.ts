import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {AuthService} from "../../services/auth/auth.service";
import {Role} from "../../model/user";

@Injectable({
  providedIn: 'root'
})
export class OwnerGuard implements CanActivate {

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    if (!this.authService.isUserLoggedIn()) {
      this.router.navigate(['/anon']);
      return false;
    }
    const role = this.authService.getCurrentUser()?.role;

    if (role === Role.ROLE_OWNER || role === Role.ROLE_TENANT) {
      return true;
    } else if (role === "ROLE_ADMIN") {
      this.router.navigate(['/admin']);
    }
    return false;
  }

}
