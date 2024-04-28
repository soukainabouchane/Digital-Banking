package org.sb.ebankingbackend.exceptions;

public class BalanceNotSufficientException extends Throwable {
    public BalanceNotSufficientException(String balanceNotSufficient) {
    super(balanceNotSufficient);
    }
}
