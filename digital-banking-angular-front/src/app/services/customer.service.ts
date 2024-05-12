
import { Injectable } from '@angular/core';
import { Customer } from "../model/customer.model";
import { environment } from "../../environments/environment";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({
  providedIn: 'root'
})

export class CustomerService {

  constructor(private http: HttpClient) { }

  public getCustomer(): Observable<Array<Customer>>{
    return this.http.get<Array<Customer>>(environment.backendHost + "/customers");
 }

 public searchCustomers(keyword : string): Observable<Array<Customer>>{
    return this.http.get<Array<Customer>>(environment.backendHost + "/customers/search?keyword=" + keyword);
 }

 public saveCustomer(customer: Customer):Observable<Customer>{
    return this.http.post<Customer>(environment.backendHost + "/create", customer);
 }

 public deleteCustomer(id: number){
    return this.http.delete(environment.backendHost + "/delete/" + id);
 }

}
