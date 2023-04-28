import { CSRRequest } from './../../model/csrRequest';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AddUserDTO } from 'src/app/model/addUserDTO';
import { environment } from 'src/environments/environment';
import { catchError, Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      return of(result as T);
    };
  }

  register(request: CSRRequest, file:any):Observable<any>{
    const httpOptions = {
      headers: new HttpHeaders({
       "Content-Type": "multipart/form-data" // 👈
      })
    };
    const formData = new FormData();
    formData.append("file", file);
    formData.append("request", new Blob([JSON.stringify(request)], {
          type: "application/json"
     }));

    return this.http.post(environment.backUrl+ "/users/register", formData, );

  }

  addUser(addUserDTO: AddUserDTO):Observable<String>{
    return this.http.post<String>(environment.backUrl + "/users/add-user", addUserDTO)
            .pipe(
              catchError(this.handleError<String>('add-user', ""))
            );
  }

}
