package de.gedoplan.showcase.presentation;

import de.gedoplan.showcase.entity.City;
import de.gedoplan.showcase.persistence.CityRepository;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class CityPresenter implements Serializable
{
  @Inject
  private CityRepository cityRepository;

  private City           currentCity = new City();

  public City getCurrentCity()
  {
    return this.currentCity;
  }

  public void insertCurrentCity()
  {
    this.cityRepository.persist(this.currentCity);
  }

  public List<City> getAllCities()
  {
    return this.cityRepository.findAll();
  }
}
