package org.intexsoft.domain.aggregate;

import org.intexsoft.domain.entity.*;
import org.intexsoft.domain.exception.GenericException;
import org.intexsoft.domain.exception.NotEnoughMoneyException;
import org.intexsoft.domain.exception.NotFoundException;
import org.intexsoft.domain.repository.AccountRepository;
import org.intexsoft.domain.repository.BankRepository;
import org.intexsoft.domain.repository.ClientRepository;
import org.intexsoft.domain.repository.TransactionRepository;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;

import static org.junit.Assert.*;


public class DefaultBankTransactionAggregateTest {

  @Test
  public void testTransaction_sameBank_currencyExchange() throws GenericException, NotEnoughMoneyException, NotFoundException {
    BankRepository bankRepo = new MockBankRepository() {
      @Override
      public Bank getById(String id) throws GenericException {
        if (id.equals("bank1")) {
          return Bank.builder().build();
        }
        return super.getById(id);
      }
    };
    ClientRepository clientRepo = new MockClientRepository() {
      @Override
      public Client getById(String id) throws GenericException {
        if (id.equals("client1")) {
          return Client.builder().build();
        } else if (id.equals("client2")) {
          return Client.builder().build();
        }
        return super.getById(id);
      }
    };
    AccountRepository accountRepo = new MockAccountRepository() {
      @Override
      public Account getById(String id) throws GenericException {
        if (id.equals("from")) {
          return Account.builder().id("from").bankId("bank1").clientId("client1").currency(Currency.EUR).amount(new BigDecimal("123")).build();
        } else if (id.equals("to")) {
          return Account.builder().id("to").bankId("bank1").clientId("client2").currency(Currency.USD).amount(BigDecimal.ONE).build();
        }
        return super.getById(id);
      }

      @Override
      public void update(Account account) throws GenericException {
        if (account.getId().equals("from")) {
          assertEqualsBigDecimal(new BigDecimal("94.52"), account.getAmount());
        } else if (account.getId().equals("to")) {
          assertEqualsBigDecimal(new BigDecimal("31.97"), account.getAmount());
        } else {
          super.update(account);
        }
      }
    };
    TransactionRepository transactionRepo = new MockTransactionRepository(){
      @Override
      public void create(Transaction transaction) throws GenericException {
        assertEqualsBigDecimal(new BigDecimal("100"), transaction.getAmount());
        assertEqualsBigDecimal(BigDecimal.ZERO, transaction.getTransactionFee());
        assertEquals(Currency.BYN, transaction.getCurrency());
        assertEquals("from", transaction.getFromAccount());
        assertEquals("to", transaction.getToAccount());
        assertNotNull(transaction.getTransactionTime());
      }
    };
    DefaultBankTransactionAggregate aggregate = new DefaultBankTransactionAggregate(bankRepo, clientRepo, accountRepo, transactionRepo);

    aggregate.transferMoney("from", "to", new BigDecimal("100"), Currency.BYN);
  }

  @Test
  public void testTransaction_differentBank_currencyExchange_company() throws GenericException, NotEnoughMoneyException, NotFoundException {
    BankRepository bankRepo = new MockBankRepository() {
      @Override
      public Bank getById(String id) throws GenericException {
        if (id.equals("bank1")) {
          return Bank.builder().externalTransferFeeForCompanies(10.0).build();
        }
        if (id.equals("bank2")) {
          return Bank.builder().build();
        }
        return super.getById(id);
      }
    };
    ClientRepository clientRepo = new MockClientRepository() {
      @Override
      public Client getById(String id) throws GenericException {
        if (id.equals("client1")) {
          return Client.builder().isCompany(true).build();
        } else if (id.equals("client2")) {
          return Client.builder().build();
        }
        return super.getById(id);
      }
    };
    AccountRepository accountRepo = new MockAccountRepository() {
      @Override
      public Account getById(String id) throws GenericException {
        if (id.equals("from")) {
          return Account.builder().id("from").bankId("bank1").clientId("client1").currency(Currency.EUR).amount(new BigDecimal("123")).build();
        } else if (id.equals("to")) {
          return Account.builder().id("to").bankId("bank2").clientId("client2").currency(Currency.USD).amount(BigDecimal.ONE).build();
        }
        return super.getById(id);
      }

      @Override
      public void update(Account account) throws GenericException {
        if (account.getId().equals("from")) {
          assertEqualsBigDecimal(new BigDecimal("91.67"), account.getAmount());
        } else if (account.getId().equals("to")) {
          assertEqualsBigDecimal(new BigDecimal("31.97"), account.getAmount());
        } else {
          super.update(account);
        }
      }
    };
    TransactionRepository transactionRepo = new MockTransactionRepository(){
      @Override
      public void create(Transaction transaction) throws GenericException {
        assertEqualsBigDecimal(new BigDecimal("100"), transaction.getAmount());
        assertEqualsBigDecimal(new BigDecimal("10.00"), transaction.getTransactionFee());
        assertEquals(Currency.BYN, transaction.getCurrency());
        assertEquals("from", transaction.getFromAccount());
        assertEquals("to", transaction.getToAccount());
        assertNotNull(transaction.getTransactionTime());
      }
    };
    DefaultBankTransactionAggregate aggregate = new DefaultBankTransactionAggregate(bankRepo, clientRepo, accountRepo, transactionRepo);

    aggregate.transferMoney("from", "to", new BigDecimal("100"), Currency.BYN);
  }

  @Test
  public void testTransaction_sameBank_sameCurrency() throws GenericException, NotEnoughMoneyException, NotFoundException {
    BankRepository bankRepo = new MockBankRepository() {
      @Override
      public Bank getById(String id) throws GenericException {
        if (id.equals("bank1")) {
          return Bank.builder().build();
        }
        return super.getById(id);
      }
    };
    ClientRepository clientRepo = new MockClientRepository() {
      @Override
      public Client getById(String id) throws GenericException {
        if (id.equals("client1")) {
          return Client.builder().build();
        } else if (id.equals("client2")) {
          return Client.builder().build();
        }
        return super.getById(id);
      }
    };
    AccountRepository accountRepo = new MockAccountRepository() {
      @Override
      public Account getById(String id) throws GenericException {
        if (id.equals("from")) {
          return Account.builder().id("from").bankId("bank1").clientId("client1").currency(Currency.EUR).amount(new BigDecimal("123")).build();
        } else if (id.equals("to")) {
          return Account.builder().id("to").bankId("bank1").clientId("client2").currency(Currency.EUR).amount(BigDecimal.ONE).build();
        }
        return super.getById(id);
      }

      @Override
      public void update(Account account) throws GenericException {
        if (account.getId().equals("from")) {
          assertEqualsBigDecimal(BigDecimal.ZERO, account.getAmount());
        } else if (account.getId().equals("to")) {
          assertEqualsBigDecimal(new BigDecimal("124"), account.getAmount());
        } else {
          super.update(account);
        }
      }
    };
    TransactionRepository transactionRepo = new MockTransactionRepository(){
      @Override
      public void create(Transaction transaction) throws GenericException {
        assertEqualsBigDecimal(new BigDecimal("123"), transaction.getAmount());
        assertEqualsBigDecimal(BigDecimal.ZERO, transaction.getTransactionFee());
        assertEquals(Currency.EUR, transaction.getCurrency());
        assertEquals("from", transaction.getFromAccount());
        assertEquals("to", transaction.getToAccount());
        assertNotNull(transaction.getTransactionTime());
      }
    };
    DefaultBankTransactionAggregate aggregate = new DefaultBankTransactionAggregate(bankRepo, clientRepo, accountRepo, transactionRepo);

    aggregate.transferMoney("from", "to", new BigDecimal("123"), Currency.EUR);
  }

  @Test
  public void testTransaction_differentBank_sameCurrency() throws GenericException, NotEnoughMoneyException, NotFoundException {
    BankRepository bankRepo = new MockBankRepository() {
      @Override
      public Bank getById(String id) throws GenericException {
        if (id.equals("bank1")) {
          return Bank.builder().externalTransferFee(2.5).build();
        }
        if (id.equals("bank2")) {
          return Bank.builder().build();
        }
        return super.getById(id);
      }
    };
    ClientRepository clientRepo = new MockClientRepository() {
      @Override
      public Client getById(String id) throws GenericException {
        if (id.equals("client1")) {
          return Client.builder().isCompany(false).build();
        } else if (id.equals("client2")) {
          return Client.builder().build();
        }
        return super.getById(id);
      }
    };
    AccountRepository accountRepo = new MockAccountRepository() {
      @Override
      public Account getById(String id) throws GenericException {
        if (id.equals("from")) {
          return Account.builder().id("from").bankId("bank1").clientId("client1").currency(Currency.EUR).amount(new BigDecimal("123")).build();
        } else if (id.equals("to")) {
          return Account.builder().id("to").bankId("bank2").clientId("client2").currency(Currency.EUR).amount(BigDecimal.ONE).build();
        }
        return super.getById(id);
      }

      @Override
      public void update(Account account) throws GenericException {
        if (account.getId().equals("from")) {
          assertEqualsBigDecimal(new BigDecimal("20.5"), account.getAmount());
        } else if (account.getId().equals("to")) {
          assertEqualsBigDecimal(new BigDecimal("101"), account.getAmount());
        } else {
          super.update(account);
        }
      }
    };
    TransactionRepository transactionRepo = new MockTransactionRepository(){
      @Override
      public void create(Transaction transaction) throws GenericException {
        assertEqualsBigDecimal(new BigDecimal("100"), transaction.getAmount());
        assertEqualsBigDecimal(new BigDecimal("2.50"), transaction.getTransactionFee());
        assertEquals(Currency.EUR, transaction.getCurrency());
        assertEquals("from", transaction.getFromAccount());
        assertEquals("to", transaction.getToAccount());
        assertNotNull(transaction.getTransactionTime());
      }
    };
    DefaultBankTransactionAggregate aggregate = new DefaultBankTransactionAggregate(bankRepo, clientRepo, accountRepo, transactionRepo);

    aggregate.transferMoney("from", "to", new BigDecimal("100"), Currency.EUR);
  }

  @Test(expected = NotEnoughMoneyException.class)
  public void testTransaction_sameBank_notEnoughMoney() throws GenericException, NotEnoughMoneyException, NotFoundException {
    BankRepository bankRepo = new MockBankRepository();
    ClientRepository clientRepo = new MockClientRepository() ;
    AccountRepository accountRepo = new MockAccountRepository() {
      @Override
      public Account getById(String id) throws GenericException {
        if (id.equals("from")) {
          return Account.builder().id("from").bankId("bank1").clientId("client1").currency(Currency.EUR).amount(new BigDecimal("123")).build();
        } else if (id.equals("to")) {
          return Account.builder().id("to").bankId("bank1").clientId("client2").currency(Currency.EUR).amount(BigDecimal.ONE).build();
        }
        return super.getById(id);
      }
    };
    TransactionRepository transactionRepo = new MockTransactionRepository();
    DefaultBankTransactionAggregate aggregate = new DefaultBankTransactionAggregate(bankRepo, clientRepo, accountRepo, transactionRepo);

    aggregate.transferMoney("from", "to", new BigDecimal("124"), Currency.EUR);
  }

  @Test(expected = NotEnoughMoneyException.class)
  public void testTransaction_differentBanks_notEnoughMoney() throws GenericException, NotEnoughMoneyException, NotFoundException {
    BankRepository bankRepo = new MockBankRepository() {
      @Override
      public Bank getById(String id) throws GenericException {
        if (id.equals("bank1")) {
          return Bank.builder().externalTransferFee(2.1).build();
        }
        return super.getById(id);
      }
    };
    ClientRepository clientRepo = new MockClientRepository() {
      @Override
      public Client getById(String id) throws GenericException {
        if (id.equals("client1")) {
          return Client.builder().isCompany(false).build();
        }
        return super.getById(id);
      }
    };
    AccountRepository accountRepo = new MockAccountRepository() {
      @Override
      public Account getById(String id) throws GenericException {
        if (id.equals("from")) {
          return Account.builder().id("from").bankId("bank1").clientId("client1").currency(Currency.EUR).amount(new BigDecimal("102")).build();
        } else if (id.equals("to")) {
          return Account.builder().id("to").bankId("bank2").clientId("client2").currency(Currency.EUR).amount(BigDecimal.ONE).build();
        }
        return super.getById(id);
      }
    };
    TransactionRepository transactionRepo = new MockTransactionRepository();
    DefaultBankTransactionAggregate aggregate = new DefaultBankTransactionAggregate(bankRepo, clientRepo, accountRepo, transactionRepo);

    aggregate.transferMoney("from", "to", new BigDecimal("100"), Currency.EUR);
  }

  private static void assertEqualsBigDecimal(BigDecimal expected, BigDecimal actual) {
    assertEquals(expected.setScale(2, RoundingMode.HALF_UP), actual.setScale(2, RoundingMode.HALF_UP));
  }

  // according to the task mockito/easymock are not allowed to use, that is why those ugly mocks exist

  private static class MockTransactionRepository implements TransactionRepository {

    public List<Transaction> list() throws GenericException {
      throw new RuntimeException("unexpected call");
    }

    public void create(Transaction transaction) throws GenericException {
      throw new RuntimeException("unexpected call");
    }

    public boolean delete(String id) throws GenericException {
      throw new RuntimeException("unexpected call");
    }

    public List<Transaction> list(String clientId, Instant fromTime, Instant toTime) throws GenericException {
      throw new RuntimeException("unexpected call");
    }

    @Override
    public int deleteTrasactionsRelatedtoAccount(String accountId) throws GenericException {
      throw new RuntimeException("unexpected call");
    }
  }

  private static class MockAccountRepository implements AccountRepository {

    public List<Account> accounts() throws GenericException {
      throw new RuntimeException("unexpected call");
    }

    public List<Account> accountsForClient(String clientId) throws GenericException {
      throw new RuntimeException("unexpected call");
    }

    public List<Account> accountsForBank(String bankId) throws GenericException {
      throw new RuntimeException("unexpected call");
    }

    public void create(Account account) throws GenericException {
      throw new RuntimeException("unexpected call");
    }

    public void update(Account account) throws GenericException {
      throw new RuntimeException("unexpected call");
    }

    public Account getById(String id) throws GenericException {
      throw new RuntimeException("unexpected call");
    }

    public boolean delete(String id) throws GenericException {
      throw new RuntimeException("unexpected call");
    }
  }

  private static class MockClientRepository implements ClientRepository {
    public List<Client> list() throws GenericException {
      throw new RuntimeException("unexpected call");
    }

    public void create(Client client) throws GenericException {
      throw new RuntimeException("unexpected call");
    }

    public void update(Client client) throws GenericException {
      throw new RuntimeException("unexpected call");
    }

    public Client getById(String id) throws GenericException {
      throw new RuntimeException("unexpected call");
    }

    public boolean delete(String id) throws GenericException {
      throw new RuntimeException("unexpected call");
    }
  }

  private static class MockBankRepository implements BankRepository {

    public List<Bank> list() throws GenericException {
      throw new RuntimeException("unexpected call");
    }

    public void create(Bank bank) throws GenericException {
      throw new RuntimeException("unexpected call");
    }

    public void update(Bank bank) throws GenericException {
      throw new RuntimeException("unexpected call");
    }

    public Bank getById(String id) throws GenericException {
      throw new RuntimeException("unexpected call");
    }

    public boolean delete(String id) throws GenericException {
      throw new RuntimeException("unexpected call");
    }
  }

}
