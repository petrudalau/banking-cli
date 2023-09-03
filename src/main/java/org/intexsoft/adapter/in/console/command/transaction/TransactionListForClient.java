package org.intexsoft.adapter.in.console.command.transaction;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.intexsoft.adapter.in.console.command.Command;
import org.intexsoft.domain.BankTransactionAggregate;
import org.intexsoft.domain.entity.Transaction;
import org.intexsoft.domain.exception.GenericException;

import java.time.Instant;
import java.util.List;

import static org.intexsoft.adapter.in.console.Constants.DATE_TIME_FORMATTER;
import static org.intexsoft.adapter.in.console.Constants.DELIMITER;
import static org.intexsoft.adapter.in.console.command.transaction.TransactionList.printTransactions;

@AllArgsConstructor
@Log4j2
public class TransactionListForClient implements Command {
  private static final String NAME = "transaction-list-for-client";
  private final BankTransactionAggregate aggregate;

  @Override
  public int getNumberOfParamsRequired() {
    return 3;
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public boolean execute(List<String> params) {
    try {

      List<Transaction> transactions = aggregate.listTransactions(params.get(0), DATE_TIME_FORMATTER.parse(params.get(1), Instant::from), DATE_TIME_FORMATTER.parse(params.get(2), Instant::from));
      printTransactions(transactions);
      return true;
    } catch (GenericException e) {
      log.error("Failed to read client list." ,e);
      return false;
    }
  }

  public String getDocumentation() {
    return "Format: \"transaction-list-for-client %client_id% %time_from% %time_to%\". Description: prints all the transactions for the client specified within the time range specified.";
  }
}
