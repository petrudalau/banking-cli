package org.intexsoft.domain.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder(toBuilder = true)
public class Transaction {
  private String id;
  private String fromAccount;
  private String toAccount;
  private BigDecimal transactionFee;
  private Instant transactionTime;
  private Currency currency;
  private BigDecimal amount;
}
