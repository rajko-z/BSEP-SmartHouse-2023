import { AuthService } from './../../../services/auth/auth.service';
import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { environment } from 'src/environments/environment';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit{
  public loginValid = true;
  public email = '';
  public password = '';

  constructor(private authService:AuthService, private toastService: ToastrService, private router: Router){
    
  }

  ngOnInit() {
    
  }

  public onSubmit(): void {
    this.loginValid = true;

    this.authService.login({password:this.password, email:this.email}).subscribe({
      next:(res)=>{
        this.authService.setCurrentUser(res);
        this.toastService.success("Successfully logged in!");
        console.log("Uspesno");
        this.authService.loggedUser = true;

        let role: string | undefined = this.authService.getCurrentUser()?.role;

        switch(role){
          case 'CLIENT':
            this.router.navigate(['/client', {outlets: {'ClientRouter': ['request-ride-page']}}]);
            break;
          case 'ADMIN':
            this.router.navigate(['/admin', {outlets: {'AdminRouter': ['homepage']}}]);
            break;
          default:
            this.router.navigateByUrl('/login');
        }

      },
      error:(err)=>{
        this.toastService.warning("Credentials are not valid!");  
        this.loginValid = false;
      }
    });
  }


}
