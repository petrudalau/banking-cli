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
public class AccountUpdate implements Command {
  private static final String NAME = "account-update";
  private final BankTransactionAggregate aggregate;

  @Override
  public int getNumberOfParamsRequired() {
    return 5;
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public boolean execute(List<String> params) {
    try {
      aggregate.updateAccount(Account.builder()
          .id(params.get(0))
          .clientId(params.get(1))
          .bankId(params.get(2))
          .currency(Currency.valueOf(params.get(3)))
          .amount(new BigDecimal(params.get(4)))
          .build());
      System.out.println("Successfully updated the account.");
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
    return "Format: \"account-update %account_id% %client_id% %bank_id% %currency_code% %amount% \". Description: updates the account with id, clientId, bankId, currency and amount specified.";
  }
}
