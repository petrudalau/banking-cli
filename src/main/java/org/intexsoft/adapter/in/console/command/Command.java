package org.intexsoft.adapter.in.console.command;


import java.util.List;

public interface Command {
  String getName();

  default int getNumberOfParamsRequired() {
    return 0;
  }

  boolean execute(List<String> params);

  String getDocumentation();
}
