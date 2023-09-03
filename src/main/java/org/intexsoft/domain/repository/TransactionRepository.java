package org.intexsoft.domain.repository;

import org.intexsoft.domain.entity.Transaction;
import org.intexsoft.domain.exception.GenericException;

import java.time.Instant;
import java.util.List;

public interface TransactionRepository {
  List<Transaction> list() throws GenericException;
  void create(Transaction transaction) throws GenericException;
  boolean delete(String id) throws GenericException;
  List<Transaction> list(String clientId, Instant fromTime, Instant toTime) throws GenericException;
  int deleteTrasactionsRelatedtoAccount(String accountId) throws GenericException;
}
