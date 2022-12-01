package hello.tis.springatmapi.application.dto;

import java.math.BigInteger;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DepositResponse {

  private final BigInteger depositAmount;
  private final BigInteger balance;
}
