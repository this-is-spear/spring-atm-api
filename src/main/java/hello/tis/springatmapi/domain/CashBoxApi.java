package hello.tis.springatmapi.domain;

import java.math.BigInteger;

public interface CashBoxApi {
  void deposit(BigInteger cash);

  void withdraw(BigInteger cash);
}
