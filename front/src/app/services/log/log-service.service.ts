import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {Log} from "../../model/logs";

@Injectable({
  providedIn: 'root'
})
export class LogServiceService {


  constructor(private http: HttpClient) { }

  getAllLogs() {
    return this.http.get<Log[]>(environment.backUrl + '/logs');
  }
}
