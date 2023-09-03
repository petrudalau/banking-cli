package org.intexsoft.domain.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
public class Account {
  private String id;
  private String clientId;
  private String bankId;
  private BigDecimal amount;
  private Currency currency;
}
