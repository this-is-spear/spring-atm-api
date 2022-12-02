package hello.tis.springatmapi.domain;

import java.math.BigInteger;

public interface BankingApi {

  boolean verifyCard(char[] cardNumber, char[] cvc, char[] pin);

  Accounts findAccounts(char[] cardNumber);

  void deposit(char[] accountNumber, BigInteger money);

  void cancelDeposit(char[] accountNumber, BigInteger money);

  Account seeBalance(char[] accountNumber);

  void withdraw(char[] accountNumber, BigInteger money);

  void cancelWithdraw(char[] accountNumber, BigInteger money);
}
