package hello.tis.springatmapi.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hello.tis.springatmapi.application.dto.BalanceRequest;
import hello.tis.springatmapi.application.dto.BalanceResponse;
import hello.tis.springatmapi.application.dto.BankingRequest;
import hello.tis.springatmapi.application.dto.CardRequest;
import hello.tis.springatmapi.application.dto.DepositResponse;
import hello.tis.springatmapi.application.dto.WithdrawResponse;
import hello.tis.springatmapi.domain.Account;
import hello.tis.springatmapi.domain.Accounts;
import hello.tis.springatmapi.domain.BankingApi;
import hello.tis.springatmapi.domain.CashBoxApi;
import hello.tis.springatmapi.exception.DepositFailException;
import hello.tis.springatmapi.exception.InvalidCardException;
import hello.tis.springatmapi.exception.WithdrawalException;
import java.math.BigInteger;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AtmServiceTest {

  private static final char[] CARD_NUMBER = "1233-1234-2345-3454".toCharArray();
  private static final char[] ACCOUNT_NUMBER = "2133242-234-23434".toCharArray();
  private static final char[] CVC = "434".toCharArray();
  private static final char[] PIN = "123456".toCharArray();
  private static final String ACCOUNT_NAME = "new deposit";
  private static final BigInteger MONEY = BigInteger.valueOf(10000L);
  private static final BigInteger BALANCE = BigInteger.valueOf(10000L);
  private static final Account ACCOUNT = new Account(ACCOUNT_NAME, ACCOUNT_NUMBER, BALANCE);
  private static final BankingRequest DEPOSIT_REQUEST = new BankingRequest(
      ACCOUNT_NUMBER, MONEY);
  private static final CardRequest CARD_REQUEST = new CardRequest(
      CARD_NUMBER, CVC, PIN);

  @Mock
  BankingApi bankingApi;
  @Mock
  CashBoxApi cashBoxApi;
  @InjectMocks
  AtmService atmService;

  @Test
  @DisplayName("Returns the list of accounts.")
  void findAccounts() {
    when(bankingApi.verifyCard(CARD_NUMBER, CVC, PIN)).thenReturn(true);
    when(bankingApi.findAccounts(CARD_NUMBER))
        .thenReturn(new Accounts(List.of(ACCOUNT)));

    assertDoesNotThrow(
        () -> atmService.findAccounts(CARD_REQUEST)
    );
  }

  @Test
  @DisplayName("An InvalidCardException occurs if the card number is not valid.")
  void findAccounts_invalidCard() {
    when(bankingApi.verifyCard(CARD_NUMBER, CVC, PIN)).thenReturn(false);

    assertThatThrownBy(
        () -> atmService.findAccounts(CARD_REQUEST)
    ).isInstanceOf(InvalidCardException.class);
  }

  @Test
  @DisplayName("An exception occurs when a deposit to a bank fails.")
  void deposit_failDepositBank() {
    doThrow(new RuntimeException()).when(bankingApi).deposit(any(), any());
    when(bankingApi.seeBalance(ACCOUNT_NUMBER)).thenReturn(ACCOUNT);

    assertThatThrownBy(
        () -> atmService.deposit(DEPOSIT_REQUEST)
    );
  }

  @Test
  @DisplayName("If the deposit fails in the locker, cash is withdrawn from the bank account again")
  void deposit_failDepositCashBox() {
    doThrow(new DepositFailException()).when(cashBoxApi).deposit(any());
    when(bankingApi.seeBalance(ACCOUNT_NUMBER)).thenReturn(ACCOUNT);

    assertAll(
        () -> assertThatThrownBy(() -> atmService.deposit(DEPOSIT_REQUEST))
            .isInstanceOf(DepositFailException.class),
        () -> verify(bankingApi, atLeastOnce()).cancelDeposit(any(), any())
    );
  }

  @Test
  @DisplayName("Deposit money into the cash box.")
  void deposit() {
    when(bankingApi.seeBalance(ACCOUNT_NUMBER)).thenReturn(ACCOUNT);
    DepositResponse response = atmService.deposit(DEPOSIT_REQUEST);

    assertAll(
        () -> assertThat(response.getBalance()).isEqualTo(MONEY.add(BALANCE)),
        () -> assertThat(response.getDepositAmount()).isEqualTo(MONEY)
    );
  }

  @Test
  @DisplayName("Check the balance.")
  void seeBalance() {
    when(bankingApi.seeBalance(ACCOUNT_NUMBER)).thenReturn(ACCOUNT);

    BalanceResponse balanceResponse = atmService.seeBalance(
        new BalanceRequest(ACCOUNT_NUMBER));

    assertThat(balanceResponse.getBalance()).isEqualTo(ACCOUNT.getBalance());
  }

  @Test
  @DisplayName("An exception occurs when a withdrawal to a bank fails.")
  void withdraw_failWithdrawalBank() {
    when(bankingApi.seeBalance(ACCOUNT_NUMBER)).thenReturn(ACCOUNT);
    doThrow(new WithdrawalException()).when(bankingApi).withdraw(ACCOUNT_NUMBER, MONEY);

    assertThatThrownBy(
        () -> atmService.withDraw(new BankingRequest(ACCOUNT_NUMBER, MONEY))
    ).isInstanceOf(WithdrawalException.class);
  }

  @Test
  @DisplayName("If cash withdrawal fails in the cash box, cash is deposited back into the bank account.")
  void withdraw_failWithdrawalCashBox() {
    when(bankingApi.seeBalance(ACCOUNT_NUMBER)).thenReturn(ACCOUNT);
    doNothing().when(bankingApi).withdraw(ACCOUNT_NUMBER, MONEY);
    doThrow(new WithdrawalException()).when(cashBoxApi).withdraw(MONEY);

    assertAll(
        () -> assertThatThrownBy(
            () -> atmService.withDraw(new BankingRequest(ACCOUNT_NUMBER, MONEY))
        ).isInstanceOf(WithdrawalException.class),
        () -> verify(bankingApi, atLeastOnce()).cancelWithdraw(any(), any())
    );
  }

  @Test
  @DisplayName("withdraw cash")
  void withdraw() {
    when(bankingApi.seeBalance(ACCOUNT_NUMBER)).thenReturn(ACCOUNT);
    doNothing().when(bankingApi).withdraw(ACCOUNT_NUMBER, MONEY);
    doNothing().when(cashBoxApi).withdraw(MONEY);

    WithdrawResponse response = atmService.withDraw(
        new BankingRequest(ACCOUNT_NUMBER, MONEY));

    assertAll(
        () -> assertThat(response.getBalance()).isEqualTo(BigInteger.ZERO),
        () -> assertThat(response.getWithdrawalAmount()).isEqualTo(MONEY)
    );
  }
}
