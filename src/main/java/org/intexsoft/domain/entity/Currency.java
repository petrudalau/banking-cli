package org.intexsoft.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Currency {
  BYN("BYN", 1),
  USD("USD", 3.2291),
  CNY("CNY", 0.44223),
  EUR("EUR", 3.5113);
  private final String code;
  //FIXME exchange rates instead of weights
  private final double weight;
}
