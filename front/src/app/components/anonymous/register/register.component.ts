import { ToastrService } from 'ngx-toastr';
import { UserService } from './../../../services/user/user.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { CSRRequest } from 'src/app/model/csrRequest';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit{

  public registerValid = true;
  public csrFile:any;

  public registerForm: FormGroup;

  constructor(private userService:UserService, private toastrService:ToastrService, private router:Router){
  }


  ngOnInit(): void {
    this.registerForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
      firstName: new FormControl('', [Validators.required]),
      lastName: new FormControl('', [Validators.required]),
    })
  }

  public onSubmit(): void {
    let csrRequest: CSRRequest = this.registerForm.value;
    this.userService.register(csrRequest, this.csrFile).subscribe(
      {
        next:(res)=>{
          this.toastrService.success("Request successfully send.");
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
