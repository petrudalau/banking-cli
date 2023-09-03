package org.intexsoft.domain;

import org.intexsoft.domain.entity.*;
import org.intexsoft.domain.exception.GenericException;
import org.intexsoft.domain.exception.NotEnoughMoneyException;
import org.intexsoft.domain.exception.NotFoundException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public interface BankTransactionAggregate {
  String createBank(Bank bank) throws GenericException;
  void updateBank(Bank bank) throws GenericException, NotFoundException;
  List<Bank> listBanks() throws GenericException;
  boolean deleteBank(String id) throws GenericException;

  String createClient(Client client) throws GenericException;
  void updateClient(Client client) throws GenericException, NotFoundException;
  List<Client> listClients() throws GenericException;
  boolean deleteClient(String id) throws GenericException;

  String createAccount(Account account) throws GenericException, NotFoundException;
  void updateAccount(Account account) throws GenericException, NotFoundException;
  List<Account> listAccounts() throws GenericException;
  List<Account> listAccounts(String clientId) throws GenericException;
  boolean deleteAccount(String id) throws GenericException;

  boolean transferMoney(String accountFrom, String accountTo, BigDecimal amount, Currency currency) throws NotFoundException, NotEnoughMoneyException, GenericException;
  List<Transaction> listTransactions() throws GenericException;
  List<Transaction> listTransactions(String clientId, Instant fromTime, Instant toTime) throws GenericException;
}
