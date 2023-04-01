import { CSRRequest } from './../../model/csrRequest';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

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
}
