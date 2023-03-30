import { AuthService } from './services/auth/auth.service';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'front';

  chatHidden:boolean = false;
  navbarItems:any = [];
  loggedUser:boolean = false;

  adminNavbarItems = [
    {
      label:'Certificates',
      icon: 'verified_user',
      route: 'admin/certificates'
    }
  ];
  
  ownerNavbarItems = [
    //Links for owner
  ];

  constructor(private router: Router, private authService:AuthService){
    authService.isloggedUser.subscribe((nextValue)=>{
      this.loggedUser = nextValue;
    })
  }

  ngOnInit(): void {
    this.navbarItems = this.adminNavbarItems;
  }

  LogOut():void{
    this.authService.logout();
    this.loggedUser = false;
  }
}
