package org.intexsoft.adapter.in.console.command.bank;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.intexsoft.adapter.in.console.command.Command;
import org.intexsoft.domain.BankTransactionAggregate;
import org.intexsoft.domain.entity.Bank;
import org.intexsoft.domain.exception.GenericException;
import org.intexsoft.domain.exception.NotFoundException;

import java.util.List;

@AllArgsConstructor
@Log4j2
public class BankUpdate implements Command {
  private static final String NAME = "bank-update";
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
      aggregate.updateBank(Bank.builder()
          .id(params.get(0))
          .name(params.get(1))
          .externalTransferFee(Double.parseDouble(params.get(2)))
          .build());
      System.out.println("Successfully updated bank.");
      return true;
    } catch (GenericException | NotFoundException e) {
      log.error("Failed to create bank.", e);
      return false;
    } catch (NumberFormatException e) {
      log.error("Failed to parse numeric value.", e);
      return false;
    }
  }

  public String getDocumentation() {
    return "Format: \"bank-update %bank_id% %bank_name% %external_transfer_fee%\". Description: updates the bank with id, name, external transfer fee and external transfer fee for companies(fees are set in percents, 12.2 means 12.2% fee) specified.";
  }
}
