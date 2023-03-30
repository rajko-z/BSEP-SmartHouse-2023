import { HttpClient } from '@angular/common/http';
import {Injectable} from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';
import { AuthCredentials } from 'src/app/model/auth';
import { environment } from 'src/environments/environment';
import {User, UserWithToken} from "../../model/user";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  isloggedUser = new BehaviorSubject(this.loggedUser);

  constructor(private http: HttpClient) { 
  }

  set loggedUser(isLogged:any){
    this.isloggedUser.next(isLogged);
    sessionStorage.setItem("loggedUser", isLogged);
  }

  get loggedUser(){
    return sessionStorage.getItem("loggedUser");
  }

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

  logout(){
    sessionStorage.removeItem("currentUser");
    this.loggedUser = false;
  }
}
