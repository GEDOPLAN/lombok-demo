package de.gedoplan.showcase.presentation;

import de.gedoplan.showcase.entity.Country;
import de.gedoplan.showcase.persistence.CountryRepository;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class CountryPresenter implements Serializable
{
  private static final long serialVersionUID = 1L;

  @Inject
  private CountryRepository countryRepository;

  private Country           currentCountry   = new Country();

  public Country getCurrentCountry()
  {
    return this.currentCountry;
  }

  public void insertCurrentCountry()
  {
    this.countryRepository.persist(this.currentCountry);
  }

  public List<Country> getAllCountries()
  {
    return this.countryRepository.findAll();
  }
}
