package de.gedoplan.showcase.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Access(AccessType.FIELD)
@NamedQuery(name = "Country_findByPhonePrefix", query = "select c from Country c where c.phonePrefix=:phonePrefix")
@Getter
@Setter
@EqualsAndHashCode(of = "isoCode")
@ToString
public class Country
{
  @Id
  @Column(name = "ISO_CODE", columnDefinition = "char(2)")
  @NotNull
  private String    isoCode;

  @NotNull
  @Size(min = 1)
  private String    name;

  @Column(name = "PHONE_PREFIX", length = 5)
  private String    phonePrefix;

  @Column(name = "CAR_CODE", length = 3)
  private String    carCode;

  private long      population;

  @Enumerated(EnumType.STRING)
  private Continent continent;

  private boolean   expired;

  public Country(String isoCode, String name, String phonePrefix, String carCode, long population, Continent continent, boolean expired)
  {
    this.isoCode = isoCode;
    this.name = name;
    this.phonePrefix = phonePrefix;
    this.carCode = carCode;
    this.population = population;
    this.continent = continent;
    this.expired = expired;
  }

  public Country(String isoCode, String name, String phonePrefix, String carCode, long population, Continent continent)
  {
    this(isoCode, name, phonePrefix, carCode, population, continent, false);
  }

  public Country()
  {
  }

}
