import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {AuthService} from "../auth/auth.service";

@Injectable({
  providedIn: 'root'
})
export class CustomHttpService {

  constructor(private http: HttpClient, private authService: AuthService) {
  }

  public createHeader() {
    let headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': "Bearer " + this.authService.getCurrentUserToken()
    });
    return headers;
  }
  public createHeaderMock() {
    let headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });
    return headers;
  }

  get(url: string) {
    return this.http.get(url, {
      headers: this.createHeader(),
    });
  }

  post(url: string, data: any) {
    return this.http.post(url, data, {
      headers: this.createHeader()
    });
  }

  postT<Type>(url: string, data: any) {
    return this.http.post<Type>(url, data, {
      headers: this.createHeader(),
    });
  }

  getT<Type>(url: string) {
    return this.http.get<Type>(url, {
      headers: this.createHeaderMock()
    });
  }

  getWithText(url: string) {
    return this.http.get(url, {
      headers: this.createHeader(),
      responseType: 'text'
    });
  }

  deleteWithText(url: string) {
    return this.http.delete(url, {
      headers: this.createHeader(),
      responseType: 'text'
    });
  }

  putT<Type>(url: string, data: any) {
    return this.http.put<Type>(url, data, {
      headers: this.createHeader(),
    });
  }

  deleteT<Type>(url: string) {
    return this.http.delete<Type>(url, {
      headers: this.createHeader(),
    });
  }
}
