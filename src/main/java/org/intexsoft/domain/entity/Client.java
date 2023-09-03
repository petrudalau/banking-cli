package org.intexsoft.domain.entity;

import lombok.Builder;
import lombok.Data;

// FIXME split name into firstName+lastName+orgName
@Data
@Builder(toBuilder = true)
public class Client {
  private String id;
  private String name;
  private Boolean isCompany;
}
