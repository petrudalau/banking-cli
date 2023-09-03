package org.intexsoft.adapter.in.console.command.transaction;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.intexsoft.adapter.in.console.command.Command;
import org.intexsoft.domain.BankTransactionAggregate;
import org.intexsoft.domain.entity.Transaction;
import org.intexsoft.domain.exception.GenericException;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.intexsoft.adapter.in.console.Constants.DATE_TIME_FORMATTER;
import static org.intexsoft.adapter.in.console.Constants.DELIMITER;

@AllArgsConstructor
@Log4j2
public class TransactionList implements Command {
  private static final String NAME = "transaction-list";
  private final BankTransactionAggregate aggregate;

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public boolean execute(List<String> params) {
    try {
      List<Transaction> transactions = aggregate.listTransactions();
      printTransactions(transactions);
      return true;
    } catch (GenericException e) {
      log.error("Failed to read client list." ,e);
      return false;
    }
  }

  protected static void printTransactions(List<Transaction> transactions) {
    System.out.print("ID");
    System.out.print(DELIMITER);
    System.out.print("FROM_ACCOUNT");
    System.out.print(DELIMITER);
    System.out.print("TO_ACCOUNT");
    System.out.print(DELIMITER);
    System.out.print("FEE");
    System.out.print(DELIMITER);
    System.out.print("TIMESTAMP");
    System.out.print(DELIMITER);
    System.out.print("CURRENCY");
    System.out.print(DELIMITER);
    System.out.print("AMOUNT");
    System.out.println();
    transactions.forEach(transaction -> {
      System.out.print(transaction.getId());
      System.out.print(DELIMITER);
      System.out.print(transaction.getFromAccount());
      System.out.print(DELIMITER);
      System.out.print(transaction.getToAccount());
      System.out.print(DELIMITER);
      System.out.print(DATE_TIME_FORMATTER.format(transaction.getTransactionTime()));
      System.out.print(DELIMITER);
      System.out.print(transaction.getCurrency().getCode());
      System.out.print(DELIMITER);
      System.out.print(transaction.getAmount());
      System.out.println();
    });
  }

  public String getDocumentation() {
    return "Format: \"transaction-list\". Description: prints all the transactions available.";
  }
}
