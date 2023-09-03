package org.intexsoft.adapter.out.mysql;

import lombok.extern.log4j.Log4j2;
import org.intexsoft.domain.entity.Bank;
import org.intexsoft.domain.exception.GenericException;
import org.intexsoft.domain.repository.BankRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class MysqlBankRepository implements BankRepository {
  //TODO configurable
  private static String connectionUrl = "jdbc:mysql://localhost:3306/banking";
  private static String user = "root";
  private static String password = "admin";
  //

  @Override
  public List<Bank> list() throws GenericException {
    try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)){
      PreparedStatement ps = conn.prepareStatement("SELECT id, name, external_transfer_fee,external_transfer_fee_company FROM bank");
      ResultSet resultSet = ps.executeQuery();
      List<Bank> result = new ArrayList<>();
      while (resultSet.next()) {
        result.add(Bank.builder()
            .id(resultSet.getString(1))
            .name(resultSet.getString(2))
            .externalTransferFee(resultSet.getDouble(3))
            .externalTransferFeeForCompanies(resultSet.getDouble(4))
            .build());
      }
      return result;
    } catch (SQLException e) {
      log.error("Failed to retrieve bank by id.", e);
      throw new GenericException(e.getMessage());
    }
  }

  @Override
  public void create(Bank bank) throws GenericException {
    try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)){
         PreparedStatement ps = conn.prepareStatement("INSERT INTO bank(id, name, external_transfer_fee, external_transfer_fee_company) VALUES (?,?,?,?,?)");
         ps.setString(1, bank.getId());
         ps.setString(2, bank.getName());
         ps.setDouble(3, bank.getExternalTransferFee());
         ps.setDouble(4, bank.getExternalTransferFeeForCompanies());
      ps.executeUpdate();
    } catch (SQLException e) {
      log.error("Failed to create bank.", e);
      throw new GenericException(e.getMessage());
    }
  }

  @Override
  public void update(Bank bank) throws GenericException {
    try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)){
      PreparedStatement ps = conn.prepareStatement("UPDATE bank SET name=?, external_transfer_fee=?, external_transfer_fee_company=? WHERE id=?");
      ps.setString(1, bank.getName());
      ps.setDouble(2, bank.getExternalTransferFee());
      ps.setDouble(3, bank.getExternalTransferFeeForCompanies());
      ps.setString(4, bank.getId());
      ps.executeUpdate();
    } catch (SQLException e) {
      log.error("Failed to update bank.", e);
      throw new GenericException(e.getMessage());
    }
  }

  @Override
  public Bank getById(String id) throws GenericException {
    try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)){
      PreparedStatement ps = conn.prepareStatement("SELECT name, external_transfer_fee, external_transfer_fee_company FROM bank WHERE id = ?");
      ps.setString(1, id);
      ResultSet resultSet = ps.executeQuery();
      if (resultSet.next()) {
        return Bank.builder()
            .id(id)
            .name(resultSet.getString(1))
            .externalTransferFee(resultSet.getDouble(2))
            .externalTransferFeeForCompanies(resultSet.getDouble(3))
            .build();
      } else {
        return null;
      }
    } catch (SQLException e) {
      log.error("Failed to retrieve bank by id.", e);
      throw new GenericException(e.getMessage());
    }
  }

  @Override
  public boolean delete(String id) throws GenericException {
    try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)){
      PreparedStatement ps = conn.prepareStatement("DELETE FROM bank WHERE id=?");
      ps.setString(1, id);
      return ps.executeUpdate() > 0;
    } catch (SQLException e) {
      log.error("Failed to delete bank.", e);
      throw new GenericException(e.getMessage());
    }
  }
}
