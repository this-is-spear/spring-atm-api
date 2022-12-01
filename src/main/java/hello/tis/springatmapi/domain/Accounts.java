package hello.tis.springatmapi.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Accounts {

  private final List<Account> accountList;

  public List<Account> getAccountList() {
    return new ArrayList<>(accountList);
  }
}
