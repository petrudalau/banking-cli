package org.intexsoft.adapter.in.console.command.bank;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.intexsoft.adapter.in.console.command.Command;
import org.intexsoft.domain.BankTransactionAggregate;
import org.intexsoft.domain.entity.Bank;
import org.intexsoft.domain.exception.GenericException;

import java.util.List;

import static org.intexsoft.adapter.in.console.Constants.DELIMITER;

@AllArgsConstructor
@Log4j2
public class BankList implements Command {
  private static final String NAME = "bank-list";
  private final BankTransactionAggregate aggregate;

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public boolean execute(List<String> params) {
    try {
      List<Bank> banks = aggregate.listBanks();
      printBanks(banks);
      return true;
    } catch (GenericException e) {
      log.error("Failed to read banks list." ,e);
      return false;
    }
  }

  protected static void printBanks(List<Bank> banks) {
    System.out.print("ID");
    System.out.print(DELIMITER);
    System.out.print("NAME");
    System.out.print(DELIMITER);
    System.out.print("EXTERNAL_TRANSFER_FEE");
    System.out.print(DELIMITER);
    System.out.print("EXTERNAL_TRANSFER_FEE_COMPANY");
    System.out.println();
    banks.forEach(bank -> {
      System.out.print(bank.getId());
      System.out.print(DELIMITER);
      System.out.print(bank.getName());
      System.out.print(DELIMITER);
      System.out.print(bank.getExternalTransferFee());
      System.out.print(DELIMITER);
      System.out.print(bank.getExternalTransferFeeForCompanies());
      System.out.println();
    });
  }

  public String getDocumentation() {
    return "Format: \"bank-list\". Description: prints all the banks available.";
  }
}
