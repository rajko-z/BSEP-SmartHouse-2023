import {CSRRequest} from './../../model/csrRequest';
import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {AddUserDTO} from 'src/app/model/addUserDTO';
import {environment} from 'src/environments/environment';
import {NewPassword} from "../../model/newPassword";
import {TextResponse} from "../../model/textResponse";
import {catchError, Observable, of} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) {
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      return of(result as T);
    };
  }

  getAllUserEmails(): Observable<String[]> {
    return this.http.get<String[]>(environment.backUrl + "/users/get-all-user-emails");
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
}
