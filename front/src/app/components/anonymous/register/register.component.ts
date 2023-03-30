import { Component, OnInit } from '@angular/core';

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

  constructor(){
  }


  ngOnInit(): void {
    
  }

  public onSubmit(): void {

  }

  public onBasicUpload(event:any){
    console.log(event.target.files);
    this.csrFile = event.target.files[0];
    console.log(this.csrFile);
  }

}
