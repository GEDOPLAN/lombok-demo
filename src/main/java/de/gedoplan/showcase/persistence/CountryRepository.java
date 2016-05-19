package de.gedoplan.showcase.persistence;

import de.gedoplan.showcase.entity.Country;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Transactional
public class CountryRepository
{
  @Inject
  private EntityManager entityManager;

  public void persist(Country country)
  {
    this.entityManager.persist(country);
  }

  public List<Country> findAll()
  {
    return this.entityManager.createQuery("select c from Country c", Country.class).getResultList();
  }
}
