package org.intexsoft.adapter.in.console.command.transaction;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.intexsoft.adapter.in.console.command.Command;
import org.intexsoft.domain.BankTransactionAggregate;
import org.intexsoft.domain.entity.Currency;
import org.intexsoft.domain.exception.GenericException;
import org.intexsoft.domain.exception.NotEnoughMoneyException;
import org.intexsoft.domain.exception.NotFoundException;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Log4j2
public class ExecuteTransaction implements Command {
  private static final String NAME = "transaction-execute";
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
      aggregate.transferMoney(params.get(0), params.get(1), new BigDecimal(params.get(2)), Currency.valueOf(params.get(3)));
      System.out.println("Successfully completed the transaction.");
      return true;
    } catch (GenericException | NotFoundException | NotEnoughMoneyException e) {
      log.error("Failed to do a transaction.", e);
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
    return "Format: \"transaction-execute %account_id_from% %account_id_to% %amount% %currency_code% \". Description: money transfer with parameters: account Id from, account Id to, amount, currency code.";
  }
}
