import {Injectable} from '@angular/core';
import {User, UserWithToken} from "../../model/user";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor() { }

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
}
