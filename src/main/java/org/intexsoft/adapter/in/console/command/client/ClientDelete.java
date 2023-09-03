package org.intexsoft.adapter.in.console.command.client;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.intexsoft.adapter.in.console.command.Command;
import org.intexsoft.domain.BankTransactionAggregate;
import org.intexsoft.domain.exception.GenericException;
import org.intexsoft.domain.exception.NotFoundException;

import java.util.List;

@AllArgsConstructor
@Log4j2
public class ClientDelete implements Command {
  private static final String NAME = "client-delete";
  private final BankTransactionAggregate aggregate;

  @Override
  public int getNumberOfParamsRequired() {
    return 1;
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public boolean execute(List<String> params) {
    try {
      boolean deleted = aggregate.deleteClient(params.get(0));
      if (deleted) {
        System.out.println("Successfully removed.");
      }
      return deleted;
    } catch (GenericException e) {
      log.error("Failed to delete client." ,e);
      return false;
    }
  }

  public String getDocumentation() {
    return "Format: \"client-delete %id%\". Description: removes the client with the id specified.";
  }
}
