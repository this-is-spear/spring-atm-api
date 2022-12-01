package hello.tis.springatmapi.application;

import hello.tis.springatmapi.application.dto.AccountResponses;
import hello.tis.springatmapi.application.dto.BalanceRequest;
import hello.tis.springatmapi.application.dto.CardRequest;
import hello.tis.springatmapi.application.dto.BankingRequest;
import hello.tis.springatmapi.application.dto.DepositResponse;
import hello.tis.springatmapi.application.dto.BalanceResponse;
import hello.tis.springatmapi.application.dto.WithdrawResponse;
import org.springframework.stereotype.Service;

@Service
public class AtmService {

  public AccountResponses findAccounts(CardRequest cardRequest) {
    return null;
  }

  public DepositResponse deposit(BankingRequest depositRequest) {
    return null;
  }

  public BalanceResponse seeBalance(BalanceRequest balanceRequest) {
    return null;
  }

  public WithdrawResponse withDraw(BankingRequest bankingRequest) {
    return null;
  }
}
