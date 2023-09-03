package org.intexsoft.main;

import org.intexsoft.adapter.in.console.ConsoleReader;
import org.intexsoft.adapter.in.console.command.*;
import org.intexsoft.adapter.in.console.command.accout.*;
import org.intexsoft.adapter.in.console.command.bank.*;
import org.intexsoft.adapter.in.console.command.client.ClientDelete;
import org.intexsoft.adapter.in.console.command.client.ClientUpdate;
import org.intexsoft.adapter.in.console.command.client.ClientsList;
import org.intexsoft.adapter.in.console.command.transaction.ExecuteTransaction;
import org.intexsoft.adapter.in.console.command.transaction.TransactionList;
import org.intexsoft.adapter.in.console.command.transaction.TransactionListForClient;
import org.intexsoft.adapter.out.mysql.MysqlAccountRepository;
import org.intexsoft.adapter.out.mysql.MysqlBankRepository;
import org.intexsoft.adapter.out.mysql.MysqlClientRepository;
import org.intexsoft.adapter.out.mysql.MysqlTransactionRepository;
import org.intexsoft.domain.BankTransactionAggregate;
import org.intexsoft.domain.aggregate.DefaultBankTransactionAggregate;
import org.intexsoft.domain.repository.AccountRepository;
import org.intexsoft.domain.repository.BankRepository;
import org.intexsoft.domain.repository.ClientRepository;
import org.intexsoft.domain.repository.TransactionRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//  TODO alternative implementations for JSON repo
// we are not allowed to use spring, anyway, let save possibility to orchestrate our app in this ugly way
public class MainConfig {
  public static ConsoleReader consoleReader() {
    return new ConsoleReader(bankTransactionAggregate(), commands());
  }

  private static Map<String, Command> commands() {
    Help helpCommand = new Help();
    List<Command> commands = Arrays.asList(
        new Exit(),
        helpCommand,
        new ExecuteTransaction(bankTransactionAggregate()),
        new TransactionList(bankTransactionAggregate()),
        new TransactionListForClient(bankTransactionAggregate()),
        new AccountCreateNewClient(bankTransactionAggregate()),
        new AccountDelete(bankTransactionAggregate()),
        new AccountList(bankTransactionAggregate()),
        new AccountListForClient(bankTransactionAggregate()),
        new AccountUpdate(bankTransactionAggregate()),
        new ClientDelete(bankTransactionAggregate()),
        new ClientsList(bankTransactionAggregate()),
        new ClientUpdate(bankTransactionAggregate()),
        new BankCreate(bankTransactionAggregate()),
        new BankUpdate(bankTransactionAggregate()),
        new BankDelete(bankTransactionAggregate()),
        new BankList(bankTransactionAggregate())
    );
    commands.sort((command1, command2) -> command1.getName().compareTo(command2.getName()));
    helpCommand.setCommandsAvailable(commands);
    return commands.stream().collect(Collectors.toMap(c -> c.getName(), c -> c));
  }

  public static BankTransactionAggregate bankTransactionAggregate() {
    return new DefaultBankTransactionAggregate(bankRepository(), clientRepository(), accountRepository(), transactionRepository());
  }

  public static BankRepository bankRepository() {
    return new MysqlBankRepository();
  }

  public static ClientRepository clientRepository() {
    return new MysqlClientRepository();
  }

  public static AccountRepository accountRepository() {
    return new MysqlAccountRepository();
  }

  public static TransactionRepository transactionRepository() {
    return new MysqlTransactionRepository();
  }
}
