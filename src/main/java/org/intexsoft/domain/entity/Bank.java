package org.intexsoft.domain.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Bank {
  private String id;
  private String name;
  private Double externalTransferFee;
  private Double externalTransferFeeForCompanies;
}
