export interface AccountDetails {
  accountId:               string;
  balance:                 number;
  type:                    null;
  currentPage:             number;
  totalPages:              number;
  pageSize:                number;
  accountOperationDTOList: AccountOperationDTOList[];
}

export interface AccountOperationDTOList {
  id:            number;
  operationDate: Date;
  amount:        number;
  description:   string;
  type:          string;
}
