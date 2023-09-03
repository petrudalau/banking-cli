package org.intexsoft.main;

// TODO java docs and logs everywhere, tests, log in a separate file not in the console, review DB constaints, transactions, currency exchange
//TODO review exceptions, commands docs and examples, error handling
// TODO code cleanup, repository EXISTS api
//TODO DB column types
//TODO clarify fee - is it amount of percentage?
// TODO trancade decimal amounts
//TODO format output lists
//TODO check dates and timezones
public class BankConsoleApp {
  public static void main(String[] args) {
    MainConfig.consoleReader().readCommandsFromConsole();
  }
}
