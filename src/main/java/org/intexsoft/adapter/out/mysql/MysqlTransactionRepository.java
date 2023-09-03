package org.intexsoft.adapter.out.mysql;

import lombok.extern.log4j.Log4j2;
import org.intexsoft.domain.entity.Currency;
import org.intexsoft.domain.entity.Transaction;
import org.intexsoft.domain.exception.GenericException;
import org.intexsoft.domain.repository.TransactionRepository;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class MysqlTransactionRepository implements TransactionRepository {
  //TODO configurable
  private static String connectionUrl = "jdbc:mysql://localhost:3306/banking";
  private static String user = "root";
  private static String password = "admin";

  @Override
  public List<Transaction> list() throws GenericException {
    try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)){
      PreparedStatement ps = conn.prepareStatement("SELECT id, fromAccount, toAccount, transactionFee, transactionTime, currency, amount FROM transaction");
      ResultSet resultSet = ps.executeQuery();
      List<Transaction> result = new ArrayList<>();
      while (resultSet.next()) {
        result.add(Transaction.builder()
            .id(resultSet.getString(1))
            .fromAccount(resultSet.getString(2))
            .toAccount(resultSet.getString(3))
            .transactionFee(resultSet.getBigDecimal(4))
            .transactionTime(resultSet.getTimestamp(5).toInstant())
            .currency(Currency.valueOf(resultSet.getString(6)))
            .amount(resultSet.getBigDecimal(7))
            .build());
      }
      return result;
    } catch (SQLException e) {
      log.error("Failed to retrieve vlient by id.", e);
      throw new GenericException(e.getMessage());
    }
  }

  @Override
  public List<Transaction> list(String clientId, Instant fromTime, Instant toTime) throws GenericException {
    try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)){
      PreparedStatement ps = conn.prepareStatement("SELECT account.id, fromAccount, toAccount, transactionFee, transactionTime, transaction.currency, transaction.amount FROM transaction INNER JOIN account WHERE transaction.fromAccount = account.id AND account.clientId=? AND transactionTime BETWEEN ? and ?");
      ps.setString(1, clientId);
      ps.setTimestamp(2, Timestamp.from(fromTime));
      ps.setTimestamp(3, Timestamp.from(toTime));
      ResultSet resultSet = ps.executeQuery();
      List<Transaction> result = new ArrayList<>();
      while (resultSet.next()) {
        result.add(Transaction.builder()
            .id(resultSet.getString(1))
            .fromAccount(resultSet.getString(2))
            .toAccount(resultSet.getString(3))
            .transactionFee(resultSet.getBigDecimal(4))
            .transactionTime(resultSet.getTimestamp(5).toInstant())
            .currency(Currency.valueOf(resultSet.getString(6)))
            .amount(resultSet.getBigDecimal(7))
            .build());
      }
      return result;
    } catch (SQLException e) {
      log.error("Failed to retrieve vlient by id.", e);
      throw new GenericException(e.getMessage());
    }
  }

  @Override
  public void create(Transaction transaction) throws GenericException {
    try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)){
      PreparedStatement ps = conn.prepareStatement("INSERT INTO transaction(id, fromAccount, toAccount, transactionFee, transactionTime, currency, amount) VALUES (?,?,?,?,?,?,?)");
      ps.setString(1, transaction.getId());
      ps.setString(2, transaction.getFromAccount());
      ps.setString(3, transaction.getToAccount());
      ps.setBigDecimal(4, transaction.getTransactionFee());
      ps.setTimestamp(5, Timestamp.from(transaction.getTransactionTime()));
      ps.setString(6, transaction.getCurrency().getCode());
      ps.setBigDecimal(7, transaction.getAmount());
      ps.executeUpdate();
    } catch (SQLException e) {
      log.error("Failed to create transaction.", e);
      throw new GenericException(e.getMessage());
    }
  }

  @Override
  public boolean delete(String id) throws GenericException {
    try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)){
      PreparedStatement ps = conn.prepareStatement("DELETE FROM transaction WHERE id=?");
      ps.setString(1, id);
      return ps.executeUpdate() > 0;
    } catch (SQLException e) {
      log.error("Failed to delete transaction.", e);
      throw new GenericException(e.getMessage());
    }
  }
}
