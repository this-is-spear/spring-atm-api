package hello.tis.springatmapi.application;

import hello.tis.springatmapi.application.dto.AccountResponse;
import hello.tis.springatmapi.application.dto.AccountResponses;
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
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AtmService {

  private final BankingApi bankingApi;
  private final CashBoxApi cashBoxApi;

  /**
   * Enter the card number, cvc, and PIN to check the account list.
   *
   * @param cardRequest Card number, cvc and pin entered.
   * @return account list
   */
  public AccountResponses findAccounts(final CardRequest cardRequest) {
    ensureCard(cardRequest);
    final Accounts accounts = bankingApi.findAccounts(cardRequest.getCardNumber());
    return getAccountResponses(accounts);
  }

  /**
   * Deposit money by entering the account number and cash. An exception occurs if a deposit to a
   * bank fails. If the deposit fails in the cash box, the record you deposited with the bank will
   * be transferred.
   *
   * @param depositRequest Account number and amount entered.
   * @return Deposit result
   */
  public DepositResponse deposit(final BankingRequest depositRequest) {
    final Account account = bankingApi.seeBalance(depositRequest.getAccountNumber());
    bankingApi.deposit(depositRequest.getAccountNumber(), depositRequest.getMoney());

    try {
      cashBoxApi.deposit(depositRequest.getMoney());
    } catch (Exception e) {
      bankingApi.cancelDeposit(depositRequest.getAccountNumber(), depositRequest.getMoney());
      throw new DepositFailException();
    }

    return new DepositResponse(depositRequest.getMoney(),
        depositRequest.getMoney().add(account.getBalance()));
  }

  /**
   * Check the balance using the account number.
   *
   * @param balanceRequest Account number
   * @return Balance
   */
  public BalanceResponse seeBalance(final BalanceRequest balanceRequest) {
    final Account account = bankingApi.seeBalance(balanceRequest.getAccountNumber());
    return new BalanceResponse(account.getBalance());
  }

  public WithdrawResponse withDraw(final BankingRequest bankingRequest) {
    final Account account = bankingApi.seeBalance(bankingRequest.getAccountNumber());
    bankingApi.withdraw(bankingRequest.getAccountNumber(), bankingRequest.getMoney());
    try {
      cashBoxApi.withdraw(bankingRequest.getMoney());
    } catch (Exception e) {
      bankingApi.cancelWithdraw(bankingRequest.getAccountNumber(), bankingRequest.getMoney());
      throw new WithdrawalException();
    }

    return new WithdrawResponse(bankingRequest.getMoney(),
        account.getBalance().add(bankingRequest.getMoney().negate()));
  }

  private void ensureCard(final CardRequest cardRequest) {
    if (!bankingApi.verifyCard(cardRequest.getCardNumber(), cardRequest.getCvc(),
        cardRequest.getPin())) {
      throw new InvalidCardException();
    }
  }

  private AccountResponses getAccountResponses(Accounts accounts) {
    return new AccountResponses(accounts.getAccountList().stream()
        .map(account -> new AccountResponse(account.getName(), account.getNumber()))
        .collect(Collectors.toList()));
  }

}
