package org.intexsoft.adapter.in.console.command.accout;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.intexsoft.adapter.in.console.command.Command;
import org.intexsoft.domain.BankTransactionAggregate;
import org.intexsoft.domain.entity.Account;
import org.intexsoft.domain.exception.GenericException;

import java.util.List;

import static org.intexsoft.adapter.in.console.Constants.DELIMITER;

@AllArgsConstructor
@Log4j2
public class AccountList implements Command {
  private static final String NAME = "account-list";
  private final BankTransactionAggregate aggregate;

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public boolean execute(List<String> params) {
    try {
      List<Account> accounts = aggregate.listAccounts();
      printAccounts(accounts);
      return true;
    } catch (GenericException e) {
      log.error("Failed to read accounts list." ,e);
      return false;
    }
  }

  protected static void printAccounts(List<Account> accounts) {
    System.out.print("ID");
    System.out.print(DELIMITER);
    System.out.print("CLIENT_ID");
    System.out.print(DELIMITER);
    System.out.print("BANK_ID");
    System.out.print(DELIMITER);
    System.out.print("CURRENCY");
    System.out.print(DELIMITER);
    System.out.print("AMOUNT");
    System.out.println();
    accounts.forEach(account -> {
      System.out.print(account.getId());
      System.out.print(DELIMITER);
      System.out.print(account.getClientId());
      System.out.print(DELIMITER);
      System.out.print(account.getBankId());
      System.out.print(DELIMITER);
      System.out.print(account.getCurrency().getCode());
      System.out.print(DELIMITER);
      System.out.print(account.getAmount());
      System.out.println();
    });
  }

  public String getDocumentation() {
    return "Format: \"account-list\". Description: prints all the accounts available.";
  }
}
