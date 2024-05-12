import { Component, OnInit } from '@angular/core';
import {CustomerService} from "../services/customer.service";
import {catchError, map, Observable, throwError} from "rxjs";
import {Customer} from "../model/customer.model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Router} from "@angular/router";

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html',
  styleUrls: ['./customers.component.css']
})

export class CustomersComponent implements OnInit {

  customers!: Observable<Array<Customer>>;
  errorMessage: string | undefined;
  searchFormGroup!: FormGroup | undefined;

  constructor(private customerService: CustomerService,
              private fb: FormBuilder, private router: Router) {
  }

  ngOnInit(): void {
    // this.customerService.getCustomer().subscribe(
    //   {
    //     next : (data) => {
    //       this.customers = data;
    //     },
    //     error : (err) => {
    //       console.log(err);
    //       this.errorMessage = err.error.message;
    //     }
    //   }
    // )
    this.searchFormGroup = this.fb.group(
      {
        keyword: this.fb.control("")
      }
    )

    this.customers = this.customerService.getCustomer().pipe(
      catchError(err => {
        this.errorMessage = err.message;
        return throwError(err);
      })
    );
  }


  handleSearchCustomers() {
    let kw = this.searchFormGroup?.value.keyword;
    this.customers = this.customerService.searchCustomers(kw).pipe(
      catchError(err => {
        this.errorMessage = err.message;
        return throwError(err);
      })
    )
  }

  /*deleteCustomer() {
    this.customerService.deleteCustomer(this.customers)
  }*/

  deleteCustomer(c: Customer) {
    let conf = confirm("Are you sure ?");
    if(!conf) return;
    console.log(c.id);
    this.customerService.deleteCustomer(c.id).subscribe(
      (resp) => {
        alert("Customer with Id " + c.id + " is deleted successfully");
        // this.handleSearchCustomers();
        this.customers = this.customers.pipe(
          map( data => {
            let index = data.indexOf(c);
            data.slice(index, 1);
            return data;
          })
        )
      },
      error => {
        console.log(error);
      }
    );
  }

  handleCustomerAccounts(customer: Customer) {
    this.router.navigateByUrl("/customer-accounts/"+customer.id, {
      state : customer
    });
  }


}
