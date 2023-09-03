package org.intexsoft.adapter.in.console.command;

import java.util.List;

public class Exit implements Command {
  private static final String EXIT = "exit";

  @Override
  public String getName() {
    return EXIT;
  }

  @Override
  public boolean execute(List<String> params) {
    return false;
  }

  public String getDocumentation() {
    return "Format: \"exit\". Description: stops application execution.";
  }
}
