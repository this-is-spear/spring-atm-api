package hello.tis.springatmapi.application.dto;

import java.math.BigInteger;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WithdrawResponse {

  private final BigInteger WithdrawalAmount;
  private final BigInteger balance;
}
