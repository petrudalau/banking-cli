package org.intexsoft.adapter.in.console.command.client;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.intexsoft.adapter.in.console.command.Command;
import org.intexsoft.domain.BankTransactionAggregate;
import org.intexsoft.domain.entity.Client;
import org.intexsoft.domain.exception.GenericException;

import java.util.List;

import static org.intexsoft.adapter.in.console.Constants.DELIMITER;

@AllArgsConstructor
@Log4j2
public class ClientsList implements Command {
  private static final String NAME = "client-list";
  private final BankTransactionAggregate aggregate;

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public boolean execute(List<String> params) {
    try {
      List<Client> clients = aggregate.listClients();
      printClients(clients);
      return true;
    } catch (GenericException e) {
      log.error("Failed to read client list." ,e);
      return false;
    }
  }

  protected static void printClients(List<Client> clients) {
    System.out.print("ID");
    System.out.print(DELIMITER);
    System.out.print("NAME");
    System.out.print(DELIMITER);
    System.out.print("IS_COMPANY");
    System.out.println();
    clients.forEach(client -> {
      System.out.print(client.getId());
      System.out.print(DELIMITER);
      System.out.print(client.getName());
      System.out.print(DELIMITER);
      System.out.print(client.getIsCompany());
      System.out.println();
    });
  }

  public String getDocumentation() {
    return "Format: \"client-list\". Description: prints all the clients available.";
  }
}
