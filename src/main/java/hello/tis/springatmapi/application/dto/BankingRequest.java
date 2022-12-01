package hello.tis.springatmapi.application.dto;

import java.math.BigInteger;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BankingRequest {

  private char[] accountNumber;
  private BigInteger money;
}
