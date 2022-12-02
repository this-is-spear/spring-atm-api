package hello.tis.springatmapi.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import hello.tis.springatmapi.exception.AccountException;
import java.math.BigInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class AccountTest {

  private static final char[] ACCOUNT_NUMBER = "12312-324-132123".toCharArray();
  private static final String ACCOUNT_NAME = "new account";

  @ParameterizedTest
  @NullAndEmptySource
  @DisplayName("Name is not null and empty")
  void createAccount_notNullAndEmptyName(String name) {
    assertThatThrownBy(
        () -> new Account(name, ACCOUNT_NUMBER, BigInteger.valueOf(10000L))
    ).isInstanceOf(AccountException.class);
  }

  @Test
  @DisplayName("Account number is not null and empty")
  void createAccount_NotNullAccountNumber() {
    assertAll(
        () -> assertThatThrownBy(
            () -> new Account(ACCOUNT_NAME, null, BigInteger.valueOf(1000L)))
            .isInstanceOf(AccountException.class),
        () -> assertThatThrownBy(
            () -> new Account(ACCOUNT_NAME, new char[]{}, BigInteger.valueOf(1000L)))
            .isInstanceOf(AccountException.class)
    );
  }

  @Test
  @DisplayName("Balance in not negative")
  void createAccount_NotNegativeBalance() {
    assertThatThrownBy(
        () -> new Account(ACCOUNT_NAME, ACCOUNT_NUMBER, BigInteger.valueOf(-1000L)))
        .isInstanceOf(AccountException.class);
  }
}
