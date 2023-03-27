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
      label:'Dashboard',
      icon: 'dashboard',
      router: 'homepage'
    },{
      label:'Register Driver',
      icon: 'person_add',
      router: 'register-driver'
    },{
      label:'Info Change Request',
      icon: 'data_usage',
      router: 'driver-info-request'
    },{
      label:'Report',
      icon: 'bar_chart',
      router: 'report'
    },{
      label:'Live Chat',
      icon: 'chat',
      router: 'livechat'
    },{
      label:'Block Users ',
      icon: 'block',
      router: 'block-users'
    },{
      label:'Ride History',
      icon: 'history',
      router: 'history'
    },
  ];

  constructor(private router: Router){}

  ngOnInit(): void {
  }
}
