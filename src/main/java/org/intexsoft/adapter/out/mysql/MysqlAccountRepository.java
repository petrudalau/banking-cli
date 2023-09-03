package org.intexsoft.adapter.out.mysql;

import lombok.extern.log4j.Log4j2;
import org.intexsoft.domain.entity.Account;
import org.intexsoft.domain.entity.Currency;
import org.intexsoft.domain.exception.GenericException;
import org.intexsoft.domain.repository.AccountRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class MysqlAccountRepository implements AccountRepository {
  //TODO configurable
  private static String connectionUrl = "jdbc:mysql://localhost:3306/banking";
  private static String user = "root";
  private static String password = "admin";

  @Override
  public List<Account> list() throws GenericException {
    try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)){
      PreparedStatement ps = conn.prepareStatement("SELECT id, clientId, bankId, currency, amount FROM account");
      ResultSet resultSet = ps.executeQuery();
      List<Account> result = new ArrayList<>();
      while (resultSet.next()) {
        result.add(Account.builder()
            .id(resultSet.getString(1))
            .clientId(resultSet.getString(2))
            .bankId(resultSet.getString(3))
            .currency(Currency.valueOf(resultSet.getString(4)))
            .amount(resultSet.getBigDecimal(5))
            .build());
      }
      return result;
    } catch (SQLException e) {
      log.error("Failed to retrieve client by id.", e);
      throw new GenericException(e.getMessage());
    }
  }

  @Override
  public List<Account> list(String clientId) throws GenericException {
    try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)){
      PreparedStatement ps = conn.prepareStatement("SELECT id, bankId, currency, amount FROM account WHERE clientId=?");
      ps.setString(1, clientId);
      ResultSet resultSet = ps.executeQuery();
      List<Account> result = new ArrayList<>();
      while (resultSet.next()) {
        result.add(Account.builder()
            .id(resultSet.getString(1))
            .clientId(clientId)
            .bankId(resultSet.getString(2))
            .currency(Currency.valueOf(resultSet.getString(3)))
            .amount(resultSet.getBigDecimal(4))
            .build());
      }
      return result;
    } catch (SQLException e) {
      log.error("Failed to retrieve client by id.", e);
      throw new GenericException(e.getMessage());
    }
  }

  @Override
  public void create(Account account) throws GenericException {
    try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)){
      PreparedStatement ps = conn.prepareStatement("INSERT INTO account(id, clientId, bankId, currency, amount) VALUES (?,?,?,?,?)");
      ps.setString(1, account.getId());
      ps.setString(2, account.getClientId());
      ps.setString(3, account.getBankId());
      ps.setString(4, account.getCurrency().getCode());
      ps.setBigDecimal(5, account.getAmount());
      ps.executeUpdate();
    } catch (SQLException e) {
      log.error("Failed to create account.", e);
      throw new GenericException(e.getMessage());
    }
  }

  @Override
  public void update(Account account) throws GenericException {
    try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)){
      PreparedStatement ps = conn.prepareStatement("UPDATE account SET clientId=?, bankId=?, currency=?, amount=? WHERE id=?");
      ps.setString(1, account.getClientId());
      ps.setString(2, account.getBankId());
      ps.setString(3, account.getCurrency().getCode());
      ps.setBigDecimal(4, account.getAmount());
      ps.setString(5, account.getId());
      ps.executeUpdate();
    } catch (SQLException e) {
      log.error("Failed to update account.", e);
      throw new GenericException(e.getMessage());
    }
  }

  @Override
  public Account getById(String id) throws GenericException {
    try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)){
      PreparedStatement ps = conn.prepareStatement("SELECT clientId, bankId, currency, amount FROM account WHERE id = ?");
      ps.setString(1, id);
      ResultSet resultSet = ps.executeQuery();
      if (resultSet.next()) {
        return Account.builder()
            .id(id)
            .clientId(resultSet.getString(1))
            .bankId(resultSet.getString(2))
            .currency(Currency.valueOf(resultSet.getString(3)))
            .amount(resultSet.getBigDecimal(4))
            .build();
      } else {
        return null;
      }
    } catch (SQLException e) {
      log.error("Failed to retrieve account by id.", e);
      throw new GenericException(e.getMessage());
    }
  }

  @Override
  public boolean delete(String id) throws GenericException {
    try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)){
      PreparedStatement ps = conn.prepareStatement("DELETE FROM account WHERE id=?");
      ps.setString(1, id);
      return ps.executeUpdate() > 0;
    } catch (SQLException e) {
      log.error("Failed to delete account.", e);
      throw new GenericException(e.getMessage());
    }
  }
}
