package org.intexsoft.adapter.in.console.command.accout;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.intexsoft.adapter.in.console.command.Command;
import org.intexsoft.domain.BankTransactionAggregate;
import org.intexsoft.domain.entity.Account;
import org.intexsoft.domain.entity.Client;
import org.intexsoft.domain.entity.Currency;
import org.intexsoft.domain.exception.GenericException;
import org.intexsoft.domain.exception.NotFoundException;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Log4j2
public class AccountCreateNewClient implements Command {
  private static final String NAME = "account-create-new-client";
  private final BankTransactionAggregate aggregate;

  @Override
  public int getNumberOfParamsRequired() {
    return 5;
  }

  @Override
  public String getName() {
    return NAME;
  }

  // FIXME transaction needed?
  @Override
  public boolean execute(List<String> params) {
    try {
      String clientId = aggregate.createClient(Client.builder()
          .name(params.get(0)).isCompany(Boolean.valueOf(params.get(1))).build());
      System.out.print("Successfully created client with id: ");
      System.out.println(clientId);
      String accountId = aggregate.createAccount(Account.builder()
          .clientId(clientId)
          .bankId(params.get(2))
          .currency(Currency.valueOf(params.get(3)))
          .amount(new BigDecimal(params.get(4)))
          .build());
      System.out.print("Successfully created account with id: ");
      System.out.println(accountId);
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
    return "Format: \"account-create-new-client %client_name% %is_company?% %bank_id% %currency_code% %amount% \". Description: creates the account with client name, isCompany flag(true/false), bankId, currency and amount specified.";
  }
}
