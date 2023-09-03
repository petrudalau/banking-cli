package org.intexsoft.adapter.out.mysql;

import lombok.extern.log4j.Log4j2;
import org.intexsoft.domain.entity.Client;
import org.intexsoft.domain.exception.GenericException;
import org.intexsoft.domain.repository.ClientRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class MysqlClientRepository implements ClientRepository {
  //TODO configurable
  private static String connectionUrl = "jdbc:mysql://localhost:3306/banking";
  private static String user = "root";
  private static String password = "admin";

  @Override
  public List<Client> list() throws GenericException {
    try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)){
      PreparedStatement ps = conn.prepareStatement("SELECT id, name, company FROM client");
      ResultSet resultSet = ps.executeQuery();
      List<Client> result = new ArrayList<>();
      while (resultSet.next()) {
        result.add(Client.builder()
            .id(resultSet.getString(1))
            .name(resultSet.getString(2))
            .isCompany(resultSet.getBoolean(3))
            .build());
      }
      return result;
    } catch (SQLException e) {
      log.error("Failed to retrieve vlient by id.", e);
      throw new GenericException(e.getMessage());
    }
  }

  @Override
  public void create(Client client) throws GenericException {
    try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)){
      PreparedStatement ps = conn.prepareStatement("INSERT INTO client(id, name, company) VALUES (?,?,?)");
      ps.setString(1, client.getId());
      ps.setString(2, client.getName());
      ps.setBoolean(3, client.getIsCompany());
      ps.executeUpdate();
    } catch (SQLException e) {
      log.error("Failed to create client.", e);
      throw new GenericException(e.getMessage());
    }
  }

  @Override
  public void update(Client client) throws GenericException {
    try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)){
      PreparedStatement ps = conn.prepareStatement("UPDATE client SET name=?, company=? WHERE id=?");
      ps.setString(1, client.getName());
      ps.setBoolean(2, client.getIsCompany());
      ps.setString(3, client.getId());
      ps.executeUpdate();
    } catch (SQLException e) {
      log.error("Failed to update client.", e);
      throw new GenericException(e.getMessage());
    }
  }

  @Override
  public Client getById(String id) throws GenericException {
    try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)){
      PreparedStatement ps = conn.prepareStatement("SELECT name, company FROM client WHERE id = ?");
      ps.setString(1, id);
      ResultSet resultSet = ps.executeQuery();
      if (resultSet.next()) {
        return Client.builder()
            .id(id)
            .name(resultSet.getString(1))
            .isCompany(resultSet.getBoolean(2))
            .build();
      } else {
        return null;
      }
    } catch (SQLException e) {
      log.error("Failed to retrieve client by id.", e);
      throw new GenericException(e.getMessage());
    }
  }

  @Override
  public boolean delete(String id) throws GenericException {
    try (Connection conn = DriverManager.getConnection(connectionUrl, user, password)){
      PreparedStatement ps = conn.prepareStatement("DELETE FROM client WHERE id=?");
      ps.setString(1, id);
      return ps.executeUpdate() > 0;
    } catch (SQLException e) {
      log.error("Failed to delete client.", e);
      throw new GenericException(e.getMessage());
    }
  }
}
