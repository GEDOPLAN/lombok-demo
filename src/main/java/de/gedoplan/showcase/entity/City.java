package de.gedoplan.showcase.entity;

import de.gedoplan.baselibs.persistence.entity.GeneratedIntegerIdEntity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Access(AccessType.FIELD)
@Getter
@Setter
public class City extends GeneratedIntegerIdEntity
{
  private String name;

  private int    population;

  private int    area;

  public City()
  {
  }

  public City(String title, int population, int area)
  {
    this.name = title;
    this.population = population;
    this.area = area;
  }
}
