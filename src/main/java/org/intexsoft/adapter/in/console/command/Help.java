package org.intexsoft.adapter.in.console.command;

import java.util.List;

import static org.intexsoft.adapter.in.console.Constants.COMMAND_DELIMITER;

public class Help implements Command {
  private static final String HELP = "help";
  private List<Command> commandsAvailable;

  public void setCommandsAvailable(List<Command> commands) {
    this.commandsAvailable = commands;
  }

  @Override
  public String getName() {
    return HELP;
  }

  @Override
  public boolean execute(List<String> params) {
    commandsAvailable.stream().forEach(command -> {
      System.out.println(COMMAND_DELIMITER);
      System.out.println(command.getName());
      System.out.println(command.getDocumentation());
    });
    return true;
  }

  public String getDocumentation() {
    return "Format: \"help\". Description: prints documentation for all the commands available.";
  }
}
