package org.intexsoft.adapter.in.console.command.bank;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.intexsoft.adapter.in.console.command.Command;
import org.intexsoft.domain.BankTransactionAggregate;
import org.intexsoft.domain.entity.Bank;
import org.intexsoft.domain.exception.GenericException;

import java.util.List;

@AllArgsConstructor
@Log4j2
public class BankCreate implements Command {
  private static final String NAME = "bank-create";
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
      String id = aggregate.createBank(Bank.builder()
          .name(params.get(0))
          .externalTransferFee(Double.parseDouble(params.get(1)))
          .externalTransferFeeForCompanies(Double.parseDouble(params.get(2)))
          .build());
      System.out.print("Successfully created bank with id: ");
      System.out.println(id);
      return true;
    } catch (GenericException e) {
      log.error("Failed to create bank." ,e);
      return false;
    } catch (NumberFormatException e) {
      log.error("Failed to parse numeric value.", e);
      return false;
    }
  }

  public String getDocumentation() {
    return "Format: \"bank-create %bank_name% %external_transfer_fee% %external_transfer_fee_for_companies% %external_transfer_fee_for_companies%\". Description: creates the bank with the name, external transfer fee and external transfer fee for companies(fees are set in percents, 12.2 means 12.2% fee) specified.";
  }
}
