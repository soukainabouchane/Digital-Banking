import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from "@angular/forms";
import { AccountsService } from "../services/accounts.service";
import { catchError, Observable, throwError } from "rxjs";
import { AccountDetails } from "../model/account.model";

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.css']
})

export class AccountsComponent implements OnInit {

  accountFormGroup! : FormGroup;
  currentPage : number = 0;
  pageSize : number = 5;
  accountObservable! : Observable<AccountDetails>; // accountObservable <=> account$

  operationFormGroup! : FormGroup;
  constructor(private fb: FormBuilder, private accountService: AccountsService) { }

  ngOnInit(): void {
      this.accountFormGroup = this.fb.group({
      accountId : this.fb.control('')});

    this.operationFormGroup = this.fb.group({
      operationType : this.fb.control(null),
      amount : this.fb.control(0),
      description : this.fb.control(null),
      accountDestination : this.fb.control(null),
      accountSource : this.fb.control(null),
    })

  }

  handleSearchAccount() {
    let accountId: string = this.accountFormGroup.value.accountId;
    this.accountObservable = this.accountService.getAccount(accountId, this.currentPage, this.pageSize);
  }

  gotoPage(page: number) {
    console.log("Page: " + page);
    this.currentPage = page;
    this.handleSearchAccount();
  }

  handleAccountOperation() {
    let accountId: string = this.accountFormGroup.value.accountId;
    let operationType = this.operationFormGroup.value.operationType;
    let amount : number = this.operationFormGroup.value.amount;
    let description : string = this.operationFormGroup.value.description;
    let accountDestination: string = this.operationFormGroup.value.accountDestination;

    if(operationType == 'DEBIT') {
      this.accountService.debitAccount(accountId, amount, description).subscribe({
        next : (data) => {
          alert("Success credit");
          this.operationFormGroup.reset();
          this.handleSearchAccount();
        },
        error : (err) => {
           console.log("Something went wrong!")
        }
      })
    } else if(operationType == 'CREDIT') {
      this.accountService.creditAccount(accountId, amount, description).subscribe({
        next : (data) => {
         alert("Success debit");
         this.operationFormGroup.reset();
         this.handleSearchAccount();
        }, error : (err) => {
          console.log("Something went wrong!");
        }
      })
    } else if(operationType == 'TRANSFER'){
      this.accountService.transferAccount(accountId , accountDestination , amount , description).subscribe({
        next : (data) => {
          this.operationFormGroup.reset();
          this.handleSearchAccount();
        } , error : (err) => {
          console.log("Something went wrong!")
        }
      })
    }


  }

}
