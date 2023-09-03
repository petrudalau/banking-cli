package org.intexsoft.adapter.in.console.command.accout;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.intexsoft.adapter.in.console.command.Command;
import org.intexsoft.domain.BankTransactionAggregate;
import org.intexsoft.domain.entity.Account;
import org.intexsoft.domain.entity.Currency;
import org.intexsoft.domain.exception.GenericException;
import org.intexsoft.domain.exception.NotFoundException;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Log4j2
public class AccountCreate implements Command {
  private static final String NAME = "account-create";
  private final BankTransactionAggregate aggregate;

  @Override
  public int getNumberOfParamsRequired() {
    return 4;
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public boolean execute(List<String> params) {
    try {
      String id = aggregate.createAccount(Account.builder()
          .clientId(params.get(0))
          .bankId(params.get(1))
          .currency(Currency.valueOf(params.get(2)))
          .amount(new BigDecimal(params.get(3)))
          .build());
      System.out.print("Successfully created account with id: ");
      System.out.println(id);
      return true;
    } catch (GenericException | NotFoundException e) {
      log.error("Failed to create account.", e);
      return false;
    } catch (NumberFormatException e) {
      log.error("Failed to parse numeric value.", e);
      return false;
    } catch (IllegalArgumentException e) {
      log.error("Failed to parse currency.", e);
      return false;
    }
  }

  public String getDocumentation() {
    return "Format: \"account-create %client_id% %bank_id% %currency_code% %amount% \". Description: creates the account with clientId, bankId, currency and amount specified.";
  }
}
