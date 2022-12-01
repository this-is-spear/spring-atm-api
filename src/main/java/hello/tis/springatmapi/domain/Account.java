package hello.tis.springatmapi.domain;

import java.math.BigInteger;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Account {

  private final String name;
  private final char[] number;
  private final BigInteger balance;
}
