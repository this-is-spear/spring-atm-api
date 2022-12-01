package hello.tis.springatmapi.application.dto;

import java.math.BigInteger;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BalanceResponse {

  private final BigInteger balance;
}
