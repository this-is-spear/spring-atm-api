package hello.tis.springatmapi.application.dto;

import java.math.BigInteger;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BankingRequest {

  private char[] accountNumber;
  private BigInteger money;
}
