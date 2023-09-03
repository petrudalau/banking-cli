package org.intexsoft.domain.aggregate;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.intexsoft.domain.BankTransactionAggregate;
import org.intexsoft.domain.entity.*;
import org.intexsoft.domain.exception.GenericException;
import org.intexsoft.domain.exception.NotEnoughMoneyException;
import org.intexsoft.domain.exception.NotFoundException;
import org.intexsoft.domain.repository.AccountRepository;
import org.intexsoft.domain.repository.BankRepository;
import org.intexsoft.domain.repository.ClientRepository;
import org.intexsoft.domain.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Log4j2
public class DefaultBankTransactionAggregate implements BankTransactionAggregate {
  private final BankRepository bankRepository;
  private final ClientRepository clientRepository;
  private final AccountRepository accountRepository;
  private final TransactionRepository transactionRepository;

  @Override
  public String createBank(Bank bank) throws GenericException {
    String generatedId = UUID.randomUUID().toString();
    Bank bankToStore = bank.toBuilder().id(generatedId).build();
    bankRepository.create(bankToStore);
    return generatedId;
  }

  @Override
  public void updateBank(Bank bank) throws GenericException, NotFoundException {
    Bank existing = bankRepository.getById(bank.getId());
    if (existing == null) {
      log.error("Bank with id={} not found", bank.getId());
      throw new NotFoundException("Unknown bank specified!");
    }
    bankRepository.update(bank);
  }

  @Override
  public List<Bank> listBanks() throws GenericException {
    return bankRepository.list();
  }

  @Override
  public boolean deleteBank(String id) throws GenericException {
    //TODO delete accounts and transactions
    return bankRepository.delete(id);
  }

  @Override
  public String createClient(Client client) throws GenericException {
    String generatedId = UUID.randomUUID().toString();
    Client clientToStore = client.toBuilder().id(generatedId).build();
    clientRepository.create(clientToStore);
    return generatedId;
  }

  @Override
  public void updateClient(Client client) throws GenericException, NotFoundException {
    Client existing = clientRepository.getById(client.getId());
    if (existing == null) {
      log.error("Client with id={} not found", client.getId());
      throw new NotFoundException("Unknown client specified!");
    }
    clientRepository.update(client);
  }

  @Override
  public List<Client> listClients() throws GenericException {
    return clientRepository.list();
  }

  @Override
  public boolean deleteClient(String id) throws GenericException {
    //TODO delete accounts
    return clientRepository.delete(id);
  }

  @Override
  public String createAccount(Account account) throws GenericException, NotFoundException {
    if (bankRepository.getById(account.getBankId()) == null) {
      throw new NotFoundException("Unknown bank specified!");
    }
    if (clientRepository.getById(account.getClientId()) == null) {
      throw new NotFoundException("Unknown client specified!");
    }
    String generatedId = UUID.randomUUID().toString();
    Account accountToStore = account.toBuilder().id(generatedId).build();
    accountRepository.create(accountToStore);
    return generatedId;
  }

  @Override
  public void updateAccount(Account account) throws GenericException, NotFoundException {
    if (bankRepository.getById(account.getBankId()) == null) {
      throw new NotFoundException("Unknown bank specified!");
    }
    if (clientRepository.getById(account.getClientId()) == null) {
      throw new NotFoundException("Unknown client specified!");
    }
    Account existing = accountRepository.getById(account.getId());
    if (existing == null) {
      log.error("Account with id={} not found", account.getId());
      throw new NotFoundException("Unknown account specified!");
    }
    accountRepository.update(account);
  }

  @Override
  public List<Account> listAccounts() throws GenericException {
    return accountRepository.list();
  }

  @Override
  public List<Account> listAccounts(String clientId) throws GenericException {
    return accountRepository.list(clientId);
  }

  @Override
  public boolean deleteAccount(String id) throws GenericException {
    //TODO delete clients if needed
    return accountRepository.delete(id);
  }

  @Override
  public boolean transferMoney(String accountIdFrom, String accountIdTo, BigDecimal amount, Currency currency) throws NotFoundException, NotEnoughMoneyException, GenericException {
    Account from = accountRepository.getById(accountIdFrom);
    if (from == null) {
      throw new NotFoundException("Unknown account[from] specified!");
    }
    Account to = accountRepository.getById(accountIdTo);
    if (to == null) {
      throw new NotFoundException("Unknown account[to] specified!");
    }
    BigDecimal transactionFee = BigDecimal.valueOf(calculateTransactionFee(from, to));
    BigDecimal amountToDeductFromSender = calculateAmountToDeductFromSender(amount, transactionFee, currency, from);
    if (from.getAmount().compareTo(amountToDeductFromSender) < 0) {
      throw new NotEnoughMoneyException();
    }
    BigDecimal amountToAddToReciever = calculateAmountToAddToReciever(amount, currency, to);
    // TODO transaction is critical here
    transactionRepository.create(Transaction.builder()
        .id(UUID.randomUUID().toString())
        .fromAccount(accountIdFrom)
        .toAccount(accountIdTo)
        .transactionFee(transactionFee)
        .transactionTime(Instant.now())
        .currency(currency)
        .amount(amount)
        .build());
    updateAccount(from.toBuilder().amount(from.getAmount().subtract(amountToDeductFromSender)).build());
    updateAccount(to.toBuilder().amount(to.getAmount().add(amountToAddToReciever)).build());
    return true;
  }

  private BigDecimal getCurrencyExchangeRate(Currency from, Currency to) {
    return BigDecimal.valueOf(from.getWeight() / to.getWeight());
  }

  private BigDecimal calculateAmountToAddToReciever(BigDecimal amount, Currency transactionCurrency, Account to) {
    if (transactionCurrency == to.getCurrency()) {
      return amount;
    }
    return amount.multiply(getCurrencyExchangeRate(transactionCurrency, to.getCurrency()));
  }

  private BigDecimal calculateAmountToDeductFromSender(BigDecimal amount, BigDecimal transactionFee, Currency transactionCurrency, Account from) {
    BigDecimal transferAmount = amount;
    if (transactionCurrency != from.getCurrency()) {
      transferAmount =  amount.divide(getCurrencyExchangeRate(from.getCurrency(), transactionCurrency));
    }
    BigDecimal feeMuliplier = BigDecimal.ONE.add(transactionFee.divide(BigDecimal.valueOf(100)));
    return transferAmount.multiply(feeMuliplier);
  }

  private Double calculateTransactionFee(Account from, Account to) throws GenericException {
    if (from.getBankId().equals(to.getBankId())) {
      return 0.0;
    }
    boolean isCompanyAccount = clientRepository.getById(from.getClientId()).getIsCompany();
    Bank bankFrom = bankRepository.getById(from.getBankId());
    return isCompanyAccount ? bankFrom.getExternalTransferFeeForCompanies() : bankFrom.getExternalTransferFee();
  }

  @Override
  public List<Transaction> listTransactions() throws GenericException {
    return transactionRepository.list();
  }

  @Override
  public List<Transaction> listTransactions(String clientId, Instant fromTime, Instant toTime) throws GenericException {
    return transactionRepository.list(clientId, fromTime, toTime);
  }
}
