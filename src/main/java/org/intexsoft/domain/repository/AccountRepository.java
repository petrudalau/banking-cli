package org.intexsoft.domain.repository;

import org.intexsoft.domain.entity.Account;
import org.intexsoft.domain.exception.GenericException;

import java.util.List;

public interface AccountRepository {
  List<Account> accounts() throws GenericException;
  List<Account> accountsForClient(String clientId) throws GenericException;
  List<Account> accountsForBank(String bankId) throws GenericException;
  void create(Account account) throws GenericException;
  void update(Account account) throws GenericException;
  Account getById(String id) throws GenericException;
  boolean delete(String id) throws GenericException;
}
