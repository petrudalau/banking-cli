package org.intexsoft.domain.repository;

import org.intexsoft.domain.entity.Client;
import org.intexsoft.domain.exception.GenericException;

import java.util.List;

public interface ClientRepository {
  List<Client> list() throws GenericException;
  void create(Client client) throws GenericException;
  void update(Client client) throws GenericException;
  Client getById(String id) throws GenericException;
  boolean delete(String id) throws GenericException;
}
