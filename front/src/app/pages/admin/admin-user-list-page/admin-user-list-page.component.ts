import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ChangeRoleDialogComponent } from 'src/app/components/admin/change-role-dialog/change-role-dialog.component';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-admin-user-list-page',
  templateUrl: './admin-user-list-page.component.html',
  styleUrls: ['./admin-user-list-page.component.scss']
})
export class AdminUserListPageComponent {
  displayedColumns = ['email', 'firstName', 'lastName', 'role', 'facilities', 'blocked', 'deleted', 'actions'];
  searchString = "";

  users:any = [];

  toggleFilter: boolean= false;
  selectedProp: string= "role";
  selectedComp: string= "==";
  filterValue: any = "";

  dataSource = new MatTableDataSource(this.users);

  constructor(private userService: UserService, public dialog: MatDialog, private toaster:ToastrService, private router:Router) {}

  ngOnInit(): void {
    this.loadData();
  }

  private loadData() {
    this.userService.getAllUsers()
      .subscribe({
          next: (response) => {
            this.users = response;
            for (const user of this.users) {
              user.role = user.role.name
              if(!user.facilities)
                user.facilities = 0;
            }
            this.dataSource = new MatTableDataSource(this.users);
          }
        }
      );
  }

  public onSubmit(){

  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  showFilterClicked(){
    this.toggleFilter = !this.toggleFilter;

    if(!this.toggleFilter){
      this.dataSource = new MatTableDataSource(this.users);
    }

  }

  propChanged(){
    if(this.selectedProp == "blocked" || this.selectedProp == "deleted"){
      this.selectedComp = "==";
      this.filterValue = true;
    }
    else if(this.selectedProp == "role"){
      this.selectedComp = "==";
      this.filterValue = "";
    }
    else{
      this.filterValue = "";
    }
  }

  filterClicked(){
    console.log(this.selectedProp);
    console.log(this.selectedComp);
    console.log(this.filterValue);

    if(this.selectedProp == "deleted"){

    }

    let filteredData = [];

    for (const user of this.users) {
      if(this.selectedComp == "=="){
        if(user[this.selectedProp] == this.filterValue)
          filteredData.push(user);
      }
      else if(this.selectedComp == ">"){
        if(user[this.selectedProp] > this.filterValue)
          filteredData.push(user);
      }
      else{
        if(user[this.selectedProp] < this.filterValue)
          filteredData.push(user);
      }
    }
    
    this.dataSource = new MatTableDataSource(filteredData);

  }

  openChangeRoleDialog(event:any, element:any){
    event.preventDefault();
    event.stopPropagation();

    const dialogRef = this.dialog.open(ChangeRoleDialogComponent, {
      data: element,
    });

    dialogRef.afterClosed().subscribe(result => {
      if(result){
        console.log(result);
        this.changeRole(element, result);
      }
    });
  }

  private changeRole(user:any, newRole:any){
    this.userService.changeRoleOfUser(user, newRole).subscribe({
      next: (response:any) => {
        this.toaster.success(response.text, "Success");
        this.loadData();
      },
      error: (error) => {
        console.log(error);
        this.toaster.error(error.error.message);
      }
    }
  );
  }

  deteleUser(event:any, element:any){
    event.preventDefault();
    event.stopPropagation();

    this.userService.deleteUser(element).subscribe({
      next: (response:any) => {
        this.toaster.success(response.text, "Success");
        this.loadData();
      },
      error: (error) => {
        console.log(error);
        this.toaster.error(error.error.message);
      }
    }
  );
  }
  
  undeteleUser(event:any, element:any){
    event.preventDefault();
    event.stopPropagation();

    this.userService.undeleteUser(element).subscribe({
      next: (response:any) => {
        this.toaster.success(response.text, "Success");
        this.loadData();
      },
      error: (error) => {
        console.log(error);
        this.toaster.error(error.error.message);
      }
    }
  );
  }

  blockUser(event:any, element:any){
    event.preventDefault();
    event.stopPropagation();

    this.userService.blockUser(element).subscribe({
      next: (response:any) => {
        this.toaster.success(response.text, "Success");
        this.loadData();
      },
      error: (error) => {
        console.log(error);
        this.toaster.error(error.error.message);
      }
    }
    );
  }
  
  unblockUser(event:any, element:any){
    event.preventDefault();
    event.stopPropagation();


    this.userService.unblockUser(element).subscribe({
      next: (response:any) => {
        this.toaster.success(response.text, "Success");
        this.loadData();
      },
      error: (error) => {
        console.log(error);
        this.toaster.error(error.error.message);
      }
    }
    );
  }

  clickedRow(row:any){
    console.log(row);
    this.router.navigateByUrl("admin/user/"+row.email);
  }

}
