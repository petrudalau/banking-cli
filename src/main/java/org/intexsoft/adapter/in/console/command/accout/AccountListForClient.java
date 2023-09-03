package org.intexsoft.adapter.in.console.command.accout;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.intexsoft.adapter.in.console.command.Command;
import org.intexsoft.domain.BankTransactionAggregate;
import org.intexsoft.domain.entity.Account;
import org.intexsoft.domain.exception.GenericException;

import java.util.List;

import static org.intexsoft.adapter.in.console.Constants.DELIMITER;
import static org.intexsoft.adapter.in.console.command.accout.AccountList.printAccounts;

@AllArgsConstructor
@Log4j2
public class AccountListForClient implements Command {
  private static final String NAME = "account-list-for-client";
  private final BankTransactionAggregate aggregate;

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public int getNumberOfParamsRequired() {
    return 1;
  }

  @Override
  public boolean execute(List<String> params) {
    try {
      List<Account> accounts = aggregate.listAccounts(params.get(0));
      printAccounts(accounts);
      return true;
    } catch (GenericException e) {
      log.error("Failed to read accounts list." ,e);
      return false;
    }
  }

  public String getDocumentation() {
    return "Format: \"account-list-for-client %client_id% \". Description: prints all the accounts available for the client specified.";
  }
}
