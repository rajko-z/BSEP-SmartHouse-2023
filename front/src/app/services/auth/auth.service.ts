import { HttpClient } from '@angular/common/http';
import {Injectable} from '@angular/core';
import { Observable } from 'rxjs';
import { AuthCredentials } from 'src/app/model/auth';
import { environment } from 'src/environments/environment';
import {User, UserWithToken} from "../../model/user";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) { }

  getCurrentUserToken() : string | null{
    const currentUser : UserWithToken | null = this.getCurrentUserWithToken();
    return currentUser ? currentUser.token : null;
  }

  getCurrentUser() : User | null{
    const currentUser : UserWithToken | null = this.getCurrentUserWithToken();
    return currentUser ? currentUser.user : null;
  }

  getCurrentUserWithToken() : UserWithToken | null {
    const rawUser = sessionStorage.getItem('currentUser');
    if (rawUser === null) {
      return null;
    }
    return JSON.parse(rawUser);
  }

  isUserLoggedIn(): boolean {
    return this.getCurrentUserToken() !== null
  }

  setCurrentUser(userWithToken: UserWithToken){
    sessionStorage.setItem('currentUser', JSON.stringify(userWithToken));
  }

  login(credentials: AuthCredentials):Observable<any>{
    return this.http.post(environment.apiURL+ "/auth/login", credentials);

  }
}
