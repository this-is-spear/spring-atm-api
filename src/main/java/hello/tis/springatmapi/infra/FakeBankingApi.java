package hello.tis.springatmapi.infra;

import hello.tis.springatmapi.domain.Account;
import hello.tis.springatmapi.domain.Accounts;
import hello.tis.springatmapi.domain.BankingApi;
import java.math.BigInteger;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class FakeBankingApi implements BankingApi {

  private static final BigInteger BALANCE = BigInteger.valueOf(100_000_000L);
  private static final char[] ACCOUNT_NUMBER = "123-2323-231-23".toCharArray();
  private static final String NAME = "name";
  private Account ACCOUNT = new Account(NAME, ACCOUNT_NUMBER, BALANCE);

  @Override
  public boolean verifyCard(char[] cardNumber, char[] cvc, char[] pin) {
    return true;
  }

  @Override
  public Accounts findAccounts(char[] cardNumber) {
    return new Accounts(
        List.of(ACCOUNT)
    );
  }

  @Override
  public void deposit(char[] accountNumber, BigInteger money) {
    ACCOUNT = new Account(NAME, ACCOUNT_NUMBER, BALANCE.add(money));
  }

  @Override
  public void cancelDeposit(char[] accountNumber, BigInteger money) {
    ACCOUNT = new Account(NAME, ACCOUNT_NUMBER, BALANCE.add(money.negate()));
  }

  @Override
  public Account seeBalance(char[] accountNumber) {
    return ACCOUNT;
  }

  @Override
  public void withdraw(char[] accountNumber, BigInteger money) {
    ACCOUNT = new Account(NAME, ACCOUNT_NUMBER, BALANCE.add(money.negate()));
  }

  @Override
  public void cancelWithdraw(char[] accountNumber, BigInteger money) {
    ACCOUNT = new Account(NAME, ACCOUNT_NUMBER, BALANCE.add(money));
  }
}
