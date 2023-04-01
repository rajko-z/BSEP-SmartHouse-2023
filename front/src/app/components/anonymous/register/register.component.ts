import { ToastrService } from 'ngx-toastr';
import { UserService } from './../../../services/user/user.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit{

  public registerValid = true;
  public email = '';
  public firstName = '';
  public lastName = '';
  public csrFile:any;

  constructor(private userService:UserService, private toastrService:ToastrService, private router:Router){
  }


  ngOnInit(): void {
    
  }

  public onSubmit(): void {
    this.userService.register({firstName:this.firstName, lastName:this.lastName, email: this.email}, this.csrFile).subscribe(
      {
        next:(res)=>{
          this.toastrService.success("Request successfully send.");
          console.log("Uspesno")
          this.router.navigateByUrl("anon/login");
  
        },
        error:(err)=>{
          this.toastrService.warning("Something went wrong, please try again!");  
        }
      }
    )
  }

  public onBasicUpload(event:any){
    console.log(event.target.files);
    this.csrFile = event.target.files[0];
    console.log(this.csrFile);
  }

}
