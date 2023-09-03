package org.intexsoft.adapter.in.console.command.client;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.intexsoft.adapter.in.console.command.Command;
import org.intexsoft.domain.BankTransactionAggregate;
import org.intexsoft.domain.entity.Client;
import org.intexsoft.domain.exception.GenericException;
import org.intexsoft.domain.exception.NotFoundException;

import java.util.List;

@AllArgsConstructor
@Log4j2
public class ClientUpdate implements Command {
  private static final String NAME = "client-update";
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
      aggregate.updateClient(Client.builder()
          .id(params.get(0))
          .name(params.get(1))
          .isCompany(Boolean.valueOf(params.get(2)))
          .build());
      System.out.println("Successfully updated the client.");
      return true;
    } catch (GenericException | NotFoundException e) {
      log.error("Failed to create client.", e);
      return false;
    }
  }

  public String getDocumentation() {
    return "Format: \"client-update %client_id% %client_name% %is_company?% \". Description: updates the client with id name and isCompany flag(true/false) specified.";
  }
}
