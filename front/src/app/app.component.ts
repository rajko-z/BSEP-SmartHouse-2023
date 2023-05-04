import {AuthService} from './services/auth/auth.service';
import {Component} from '@angular/core';
import {Router} from '@angular/router';
import {MatDialog} from "@angular/material/dialog";
import {ChangePasswordComponent} from "./components/shared/change-password/change-password.component";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'front';

  navbarItems:any = [];
  loggedUser:boolean = false;

  adminNavbarItems = [
    {
      label:'Certificates',
      icon: 'verified_user',
      route: 'admin/certificates'
    },
    {
      label:'CSR Requests',
      icon: 'assignment_ind',
      route: 'admin/csr_requests'
    },
    {
      label:'Add User',
      icon: 'person_add',
      route: 'admin/add_user'
    }, 
    {
      label:'Users',
      icon: 'people',
      route: 'admin/all_users'
    }
  ];

  ownerNavbarItems = [
    {
      label:'Proba za ownera',
      icon: 'verified_user',
      route: 'owner'
    },
  ];

  anonymousNavbarItems = [
    {
      label:'Login',
      icon: 'https',
      route: 'anon/login'
    },
    {
      label:'Register',
      icon: 'person_add',
      route: 'anon/register'
    },
  ]

  constructor(private router: Router, private authService:AuthService, private matDialog: MatDialog){
    authService.isloggedUser.subscribe((nextValue)=>{
      this.loggedUser = nextValue;
      this.updateSideMenu();
    })
  }

  updateSideMenu() {
    if (this.loggedUser) {
      const userRole = this.authService.getCurrentUser()?.role;
      if (userRole === 'ROLE_ADMIN') {
        this.navbarItems = this.adminNavbarItems;
      } else if (userRole === 'ROLE_OWNER') {
        this.navbarItems = this.ownerNavbarItems;
      } else {
        this.navbarItems = this.adminNavbarItems;
      }
    } else {
      this.navbarItems = this.anonymousNavbarItems;
    }
  }

  ngOnInit(): void {
    this.updateSideMenu();
  }

  LogOut():void{
    this.authService.logout();
    this.loggedUser = false;
  }

  changePassword(): void {
    this.matDialog.open(ChangePasswordComponent);
  }
}
