import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {jwtDecode} from "jwt-decode";

@Injectable({
  providedIn: 'root'
})

export class AuthService {

  private isAuthenticated!: boolean;
  public accessToken!: any;
  private username: any;
  private roles: any;

  constructor(private http : HttpClient) { }

  public login(username : string, password : string) {

    let options = {
      headers :  new HttpHeaders().set("Content-Type", "application/x-www-form-urlencoded")
    }

    let params = new HttpParams().set("username", username).set("password", password);
    // "http://localhost:8081"
    return this.http.post("http://localhost:8081/auth/login", params, options)


  }

  loadProfile(data : any) {
      this.isAuthenticated = true;
      this.accessToken = data['access-token'];
      let decodedJwt : any = jwtDecode(this.accessToken);
      this.username = decodedJwt.sub;
      this.roles = decodedJwt.scope;
  }


}
