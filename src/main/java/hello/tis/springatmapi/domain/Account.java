package hello.tis.springatmapi.domain;

import hello.tis.springatmapi.exception.AccountException;
import java.math.BigInteger;
import lombok.Getter;

@Getter
public class Account {

  private final String name;
  private final char[] number;
  private final BigInteger balance;

  public Account(String name, char[] number, BigInteger balance) {
    ensureName(name);
    ensureNumber(number);
    ensureBalance(balance);

    this.name = name;
    this.number = number;
    this.balance = balance;
  }

  private void ensureBalance(BigInteger balance) {
    if (!balance.equals(balance.abs())) {
      throw AccountException.invalidBalance();
    }
  }

  private void ensureNumber(char[] number) {
    if (number == null || number.length == 0) {
      throw AccountException.invalidNumber();
    }
  }

  private void ensureName(String name) {
    if (name == null || name.isBlank()) {
      throw AccountException.invalidName();
    }
  }
}
