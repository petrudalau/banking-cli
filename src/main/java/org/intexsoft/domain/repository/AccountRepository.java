package org.intexsoft.domain.repository;

import org.intexsoft.domain.entity.Account;
import org.intexsoft.domain.exception.GenericException;

import java.util.List;

public interface AccountRepository {
  List<Account> list() throws GenericException;
  List<Account> list(String clientId) throws GenericException;
  void create(Account client) throws GenericException;
  void update(Account client) throws GenericException;
  Account getById(String id) throws GenericException;
  boolean delete(String id) throws GenericException;
}
