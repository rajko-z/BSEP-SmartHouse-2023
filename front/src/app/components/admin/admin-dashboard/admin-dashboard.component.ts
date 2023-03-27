import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.scss'],
})
export class AdminDashboardComponent implements OnInit {

  chatHidden:boolean = false;

  navbarItems = [
    {
      label:'Certificates',
      icon: 'verified_user',
      router: 'certificates'
    }
  ];

  constructor(private router: Router){}

  ngOnInit(): void {
  }
}
