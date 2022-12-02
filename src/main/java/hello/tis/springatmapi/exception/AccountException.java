package hello.tis.springatmapi.exception;

public class AccountException extends RuntimeException{

  public AccountException(String message) {
    super(message);
  }

  public static AccountException invalidName() {
    return new AccountException("이름이 유효하지 않습니다.");
  }

  public static AccountException invalidNumber() {
    return new AccountException("계좌 번호가 유효하지 않습니다.");
  }

  public static AccountException invalidBalance() {
    return new AccountException("계좌 금액이 유효하지 않습니다.");
  }
}
