package org.intexsoft.domain.repository;

import org.intexsoft.domain.entity.Bank;
import org.intexsoft.domain.exception.GenericException;

import java.util.List;

public interface BankRepository {
  List<Bank> list() throws GenericException;
  void create(Bank bank) throws GenericException;
  void update(Bank bank) throws GenericException;
  Bank getById(String id) throws GenericException;
  boolean delete(String id) throws GenericException;
}
