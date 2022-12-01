package hello.tis.springatmapi.documentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.tis.springatmapi.application.AtmService;
import hello.tis.springatmapi.application.dto.AccountResponse;
import hello.tis.springatmapi.application.dto.AccountResponses;
import hello.tis.springatmapi.application.dto.BalanceResponse;
import hello.tis.springatmapi.application.dto.DepositResponse;
import hello.tis.springatmapi.application.dto.WithdrawResponse;
import hello.tis.springatmapi.ui.AtmController;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AtmController.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc
public class AtmDocumentationTest {

  private static final char[] FIRST_ACCOUNT =
      new char[]{
          '1', '2', '3', '4', '1', '2', '-', '3', '4', '1', '2',
          '3', '4', '1', '2', '3', '4', '-', '4', '1', '2', '3'
      };

  private static final char[] SECOND_ACCOUNT =
      new char[]{
          '1', '2', '3', '4', '-', '1', '2', '3', '4', '1', '2',
          '3', '4', '1', '8', '9', '9', '-', '9', '7', '3', '1'
      };

  private static final char[] THIRD_ACCOUNT =
      new char[]{
          '1', '2', '3', '4', '-', '1', '2', '3', '4', '1', '2',
          '3', '4', '1', '2', '1', '1', '-', '1', '8', '1', '2'
      };

  @Autowired
  MockMvc mockMvc;

  ObjectMapper objectMapper = new ObjectMapper();

  @MockBean
  AtmService atmService;

  @Test
  void findAccounts() throws Exception {
    AccountResponses accountResponses = new AccountResponses(
        List.of(
            new AccountResponse("new start account", FIRST_ACCOUNT),
            new AccountResponse("deposit account", SECOND_ACCOUNT),
            new AccountResponse("withdraw account", THIRD_ACCOUNT)
        )
    );

    when(atmService.findAccounts(any())).thenReturn(accountResponses);

    Map<String, String> params = new HashMap<>();
    params.put("cardNumber", "1234-2345-2413-3433");
    params.put("cvc", "234");
    params.put("pin", "123432");

    mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/accounts/find")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params))
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
        .andDo(print())
        .andDo(document(
            "accounts/find",
            getDocumentRequest(),
            getDocumentResponse(),
            pathParameters()
        ));
  }

  @Test
  void seeBalance() throws Exception {
    when(atmService.seeBalance(any())).thenReturn(new BalanceResponse(BigInteger.valueOf(1000L)));

    Map<String, Object> params = new HashMap<>();
    params.put("accountNumber", "23423-452413-3433");

    mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/accounts/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params))
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
        .andDo(print())
        .andDo(document(
            "accounts/seeBalance",
            getDocumentRequest(),
            getDocumentResponse(),
            pathParameters()
        ));
  }

  @Test
  void deposit() throws Exception {
    int money = 1300;

    when(atmService.deposit(any())).thenReturn(
        new DepositResponse(BigInteger.valueOf(money), BigInteger.valueOf(1000L)));

    Map<String, Object> params = new HashMap<>();
    params.put("accountNumber", "23423-452413-3433");
    params.put("money", money);

    mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/accounts/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params))
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
        .andDo(print())
        .andDo(document(
            "accounts/deposit",
            getDocumentRequest(),
            getDocumentResponse(),
            pathParameters()
        ));
  }

  @Test
  void withDraw() throws Exception {
    int money = 1300;

    when(atmService.withDraw(any())).thenReturn(
        new WithdrawResponse(BigInteger.valueOf(money), BigInteger.valueOf(1000L)));

    Map<String, Object> params = new HashMap<>();
    params.put("accountNumber", "23423-452413-3433");
    params.put("money", money);

    mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/accounts/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params))
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
        .andDo(print())
        .andDo(document(
            "accounts/withdraw",
            getDocumentRequest(),
            getDocumentResponse(),
            pathParameters()
        ));
  }


  private OperationResponsePreprocessor getDocumentResponse() {
    return Preprocessors.preprocessResponse(Preprocessors.prettyPrint());
  }

  private OperationRequestPreprocessor getDocumentRequest() {
    return Preprocessors.preprocessRequest(
        Preprocessors.modifyUris()
            .scheme("http")
            .host("localhost")
            .removePort(),
        Preprocessors.prettyPrint()
    );
  }
}
