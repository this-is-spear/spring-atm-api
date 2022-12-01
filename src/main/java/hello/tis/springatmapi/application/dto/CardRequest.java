package hello.tis.springatmapi.application.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CardRequest {
  private char[] cardNumber;
  private char[] cvc;
  private char[] pin;
}
