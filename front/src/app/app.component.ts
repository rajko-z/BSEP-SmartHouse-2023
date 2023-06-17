import {AuthService} from './services/auth/auth.service';
import {Component} from '@angular/core';
import {Router} from '@angular/router';
import {MatDialog} from "@angular/material/dialog";
import {ChangePasswordComponent} from "./components/shared/change-password/change-password.component";
import {Client, Message as StompMessage, over} from "stompjs";
import * as SockJS from "sockjs-client";
import {environment} from "../environments/environment";
import {ActivatedDeviceAlarm, AlarmNotificationForUser} from "./model/activatedAlarms";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'front';

  navbarItems:any = [];
  loggedUser:boolean = false;

  private stompClient : Client;


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
    },
    {
      label: 'Logs',
      icon: 'list',
      route: 'admin/logs'
    },
    {
      label: 'Device Alarm Triggers',
      icon: 'alarm',
      route: 'admin/device_alarm_triggers'
    },
    {
      label: 'Activated device alarms',
      icon: 'warning',
      route: 'admin/device_activated_alarms'
    }
  ];

  ownerNavbarItems = [
    {
      label:'Owner dashboard',
      icon: 'dashboard',
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

  constructor(
    public snackBar: MatSnackBar,
    private router: Router,
    private authService:AuthService,
    private matDialog: MatDialog,

  ) {
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
    let Sock = new SockJS(environment.backUrl + "/ws");
    this.stompClient = over(Sock);
    this.stompClient.connect({}, this.onConnected, () => {});
  }

  onConnected = () => {
    this.stompClient.subscribe("/activated-device-alarm", (data:any) => this.showDeviceAlarmForAdmin(data));
    this.stompClient.subscribe("/activated-device-alarm-for-user", (data:any) => this.showDeviceAlarmForUser(data));
  }

  showDeviceAlarmForAdmin(payload: StompMessage) {
    let newActivatedDeviceAlarm = JSON.parse(payload.body) as ActivatedDeviceAlarm;
    let message = "Alarm: " + newActivatedDeviceAlarm.deviceAlarmTrigger.alarmName + " triggered.\n" + newActivatedDeviceAlarm.message;

    let currentUser = this.authService.getCurrentUser();
    if (currentUser != null && currentUser.role === "ROLE_ADMIN") {
      this.snackBar.open(message, "OK", {
        verticalPosition: 'bottom',
        horizontalPosition: 'right',
      });
    }
  }

  showDeviceAlarmForUser(payload: StompMessage) {
    let notification = JSON.parse(payload.body) as AlarmNotificationForUser;
    let currentUser = this.authService.getCurrentUser();
    if (currentUser != null && currentUser.email === notification.userEmail) {
      let message = "Alarm: " + notification.alarm.deviceAlarmTrigger.alarmName + " triggered.\n" + notification.alarm.message;
      this.snackBar.open(message, "OK", {
        verticalPosition: 'bottom',
        horizontalPosition: 'right',
      });
    }
  }

  LogOut():void{
    this.authService.logout();
    this.loggedUser = false;
  }

  changePassword(): void {
    this.matDialog.open(ChangePasswordComponent);
  }
}
