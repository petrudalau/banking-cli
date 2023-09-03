package org.intexsoft.adapter.in.console;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Constants {
  public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm:ss")
      .withZone(ZoneId.systemDefault());
  public static final String DELIMITER = ", ";
  public static final String COMMAND_DELIMITER = "-------------------------";
}
