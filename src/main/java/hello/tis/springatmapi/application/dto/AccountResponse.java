package hello.tis.springatmapi.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccountResponse {
  private final String accountName;
  private final char[] accountNumber;
}
