package hello.tis.springatmapi.application.dto;

import java.util.ArrayList;
import java.util.List;

public class AccountResponses {

  private final List<AccountResponse> accountResponses;

  public AccountResponses(List<AccountResponse> accountResponses) {
    this.accountResponses = accountResponses;
  }

  public List<AccountResponse> getAccountResponses() {
    return new ArrayList<>(accountResponses);
  }
}
