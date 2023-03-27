import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'front';

  chatHidden:boolean = false;
  navbarItems:any = [];

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

  constructor(private router: Router){}

  ngOnInit(): void {
    this.navbarItems = this.adminNavbarItems;

  }
}
