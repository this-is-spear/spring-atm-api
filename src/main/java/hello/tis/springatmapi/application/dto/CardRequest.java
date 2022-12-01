package hello.tis.springatmapi.application.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CardRequest {
  private char[] cardNumber;
  private char[] cvc;
  private char[] pin;
}
