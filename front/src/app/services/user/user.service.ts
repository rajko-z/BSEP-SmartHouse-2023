import {CSRRequest} from './../../model/csrRequest';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {AddUserDTO} from 'src/app/model/addUserDTO';
import {environment} from 'src/environments/environment';
import {NewPassword} from "../../model/newPassword";
import {TextResponse} from "../../model/textResponse";
import {catchError, Observable, of} from 'rxjs';
import { FacilityData } from 'src/app/model/facilityData';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  
  
  
  

  constructor(private http: HttpClient) {
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(`${operation} failed: ${error.message}`);
      throw error; // or re-throw the error
    };
  }
  

  getAllNonAdminEmails(): Observable<string[]> {
    return this.http.get<string[]>(environment.backUrl + "/users/get-all-non-admin-emails");
  }

  register(request: CSRRequest, file: any): Observable<any> {
    const formData = new FormData();
    formData.append("file", file);
    formData.append("request", new Blob([JSON.stringify(request)], {
      type: "application/json"
    }));

    return this.http.post(environment.backUrl + "/users/register", formData,);
  }

  changePassword(payload: NewPassword) {
    return this.http.put<TextResponse>(environment.backUrl + "/users/change-password", payload);
  }

  addUser(addUserDTO: AddUserDTO): Observable<String> {
    return this.http.post<String>(environment.backUrl + "/users/add-user", addUserDTO)
      .pipe(
        catchError(this.handleError<String>('add-user', ""))
      );
  }

  getAllUsers() {
    return this.http.get(environment.backUrl + "/users/all");
  }

  changeRoleOfUser(user: any, newRole: any) {
    let data = {
      email:user.email,
      newRole:newRole
    }

    return this.http.put(environment.backUrl + "/users/change-user-role", data);
  }

  deleteUser(element: any) {
    let httpParams = new HttpParams()
    .append("userEmail", element.email);

    return this.http.delete(environment.backUrl + "/users/delete-user",{params:httpParams});
  }
  
  undeleteUser(element: any) {
    let httpParams = new HttpParams()
    .append("userEmail", element.email);

    return this.http.delete(environment.backUrl + "/users/undelete-user",{params:httpParams});
  }

  blockUser(element: any) {
    let httpParams = new HttpParams()
    .append("userEmail", element.email);

    return this.http.delete(environment.backUrl + "/users/block-user",{params:httpParams});
  } 
  
  unblockUser(element: any) {
    let httpParams = new HttpParams()
    .append("userEmail", element.email);

    return this.http.delete(environment.backUrl + "/users/unblock-user",{params:httpParams});
  }

  getUserByEmail(email:string) {
    return this.http.get(environment.backUrl + "/users/"+email);
  }

  saveFacilities(facilities: any, userEmail: string) {
    let data = {
      email: userEmail,
      facilities: facilities
    }

    return this.http.post(environment.backUrl + "/users/save-facilities", data);
  }

  getUserFacilities(email: string):Observable<FacilityData[]> {
    return this.http.get<FacilityData[]>(environment.backUrl + "/users/facilities/"+email);
  }
}
