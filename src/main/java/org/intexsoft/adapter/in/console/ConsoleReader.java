package org.intexsoft.adapter.in.console;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.intexsoft.adapter.in.console.command.Command;
import org.intexsoft.adapter.in.console.command.Exit;
import org.intexsoft.domain.BankTransactionAggregate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Log4j2
public class ConsoleReader {
  private final BankTransactionAggregate aggregate;
  private final Map<String, Command> commands;

  private String parseCommandName(String userInput) {
    return userInput.split("\\s+")[0].toLowerCase().trim();
  }

  private List<String> extractParams(String userInput) {
    String[] splitted = userInput.split("\\s+");
    return Arrays.asList(splitted).subList(1, splitted.length);
  }

  public void readCommandsFromConsole() {
    BufferedReader reader = new BufferedReader(
        new InputStreamReader(System.in));

    while (true) {
      try {
        String userInput = reader.readLine();
        if (userInput != null && !userInput.isBlank()) {
          String commandName = parseCommandName(userInput);
          List<String> params = extractParams(userInput);
          Command commandToExecute = commands.get(commandName);
          if (commandToExecute == null) {
            unknownCommand();
          } else if (commandToExecute.getNumberOfParamsRequired() != params.size()) {
            argumentsMismatch(commandToExecute, params);
          } else if (commandToExecute instanceof Exit) {
            stopConsoleApp();
            break;
          } else {
            boolean success = commandToExecute.execute(params);
            if (!success) {
              operationFailed();
            }
          }
        }
      } catch (IOException e) {
        log.error("Error occured while reading the console input.", e);
        break;
      }
    }
  }

  private void operationFailed() {
    System.err.println("Error encountered.");
  }

  private void argumentsMismatch(Command commandToExecute, List<String> params) {
    System.err.print("Wrong input for the command:");
    System.err.println(commandToExecute.getName());
    System.out.println("Please check the documentation:");
    System.out.println(commandToExecute.getDocumentation());
  }

  private void stopConsoleApp() {
    System.out.println("Bye!.");
  }

  private void unknownCommand() {
    System.err.println("Unknown command, please run \"help\" to see all the commands.");
  }
}
