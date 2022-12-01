package hello.tis.springatmapi.infra;

import hello.tis.springatmapi.domain.CashBoxApi;
import java.math.BigInteger;
import org.springframework.stereotype.Component;

@Component
public class FakeCashBoxApi implements CashBoxApi {

  private BigInteger bigInteger = BigInteger.ZERO;

  @Override
  public void deposit(BigInteger cash) {
    bigInteger = bigInteger.add(cash);
  }

  @Override
  public void withdraw(BigInteger cash) {
    bigInteger = bigInteger.add(cash.negate());
  }
}
