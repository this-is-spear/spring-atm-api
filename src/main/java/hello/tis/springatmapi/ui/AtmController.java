package hello.tis.springatmapi.ui;

import hello.tis.springatmapi.application.dto.BalanceRequest;
import hello.tis.springatmapi.application.dto.AccountResponses;
import hello.tis.springatmapi.application.AtmService;
import hello.tis.springatmapi.application.dto.BalanceResponse;
import hello.tis.springatmapi.application.dto.CardRequest;
import hello.tis.springatmapi.application.dto.BankingRequest;
import hello.tis.springatmapi.application.dto.DepositResponse;
import hello.tis.springatmapi.application.dto.WithdrawResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("accounts")
@RequiredArgsConstructor
public class AtmController {

  private final AtmService atmService;

  @PostMapping("find")
  public ResponseEntity<AccountResponses> findAccounts(@RequestBody CardRequest cardRequest) {
    return ResponseEntity.ok(atmService.findAccounts(cardRequest));
  }

  @PostMapping("balance")
  public ResponseEntity<BalanceResponse> seeBalance(@RequestBody BalanceRequest balanceRequest) {
    return ResponseEntity.ok(atmService.seeBalance(balanceRequest));
  }

  @PostMapping("deposit")
  public ResponseEntity<DepositResponse> deposit(@RequestBody BankingRequest bankingRequest) {
    return ResponseEntity.ok(atmService.deposit(bankingRequest));
  }

  @PostMapping("withdraw")
  public ResponseEntity<WithdrawResponse> withdraw(@RequestBody BankingRequest bankingRequest) {
    return ResponseEntity.ok(atmService.withDraw(bankingRequest));
  }

}
