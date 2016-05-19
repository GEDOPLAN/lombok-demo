package de.gedoplan.showcase;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import de.gedoplan.showcase.entity.Continent;
import de.gedoplan.showcase.entity.Country;

import java.util.List;

import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

//CHECKSTYLE:OFF

/**
 * Test der Persistence-Fuktionalität bzgl. der Entity Country.
 *
 * @author dw
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CountryTest extends TestBase
{
  public static Country   testCountryCA   = new Country("CA", "Canada", "1", null, 34_482_779, Continent.NORTH_AMERICA);
  public static Country   testCountryCN   = new Country("CN", "China", "86", null, 1_331_460_000, Continent.ASIA);
  public static Country   testCountryDE   = new Country("DE", "Germany", "49", "D", 81_879_976, Continent.EUROPE);
  public static Country   testCountryIT   = new Country("IT", "Italy", "39", "I", 60_221_210, Continent.EUROPE);
  public static Country   testCountryUS   = new Country("US", "United States of America", "1", null, 305_548_183, Continent.NORTH_AMERICA);
  public static Country   testCountryVN   = new Country("VN", "Vietnam", "84", null, 87_840_000, Continent.ASIA);
  public static Country   testCountryYU   = new Country("YU", "Yugoslavia", null, null, 0, Continent.EUROPE, true);
  public static Country[] testCountries   = { testCountryCA, testCountryCN, testCountryDE, testCountryIT, testCountryUS, testCountryVN, testCountryYU };
  public static Country[] testCountriesAS = { testCountryCN, testCountryVN };

  @Test
  public void test_00_clearData()
  {
    this.log.info("----- test_00_clearData -----");

    this.entityManager.createQuery("delete from Country c").executeUpdate();
  }

  @Test
  public void test_01_insertData() throws Exception
  {
    this.log.info("----- test_01_insertData -----");

    for (Country country : testCountries)
    {
      this.entityManager.persist(country);

      this.log.info("Inserted: " + country);
    }
  }

  /**
   * Test: Ändern eines Einzeleintrags.
   */
  @Test
  public void test_03_update()
  {
    this.log.info("----- test_03_update -----");

    Country testCountry = testCountryIT;
    String isoCode = testCountry.getIsoCode();

    Country country = this.entityManager.find(Country.class, isoCode);
    long populationOld = country.getPopulation();
    long populationNew = populationOld + 2;

    try
    {
      this.log.info("Change population: " + populationOld + " -> " + populationNew);

      // Eintrag ändern
      country.setPopulation(populationNew);
      this.entityManager.getTransaction().commit();

      // Test: Eintrag in DB verändert?
      this.entityManager.clear();
      country = this.entityManager.find(Country.class, isoCode);
      this.log.info("Changed entry: " + country);
      Assert.assertEquals("Population", populationNew, country.getPopulation());
    }
    finally
    {
      // Geänderten Eintrag wieder restaurieren
      try
      {
        EntityTransaction tx = this.entityManager.getTransaction();
        if (!tx.isActive())
        {
          tx.begin();
        }

        country = this.entityManager.find(Country.class, isoCode);
        country.setPopulation(populationOld);

        tx.commit();
      }
      catch (Exception ignore)
      {
      }
    }
  }

  /**
   * Test: Löschen eines Einzeleintrags.
   */
  @Test
  public void test_04_remove()
  {
    this.log.info("----- test_04_remove -----");

    Country testCountry = testCountryCA;
    String isoCode = testCountry.getIsoCode();

    Country country = this.entityManager.getReference(Country.class, isoCode);

    try
    {
      // Eintrag löschen
      this.entityManager.remove(country);
      this.entityManager.getTransaction().commit();

      this.log.info("Removed country with isoCode " + isoCode);

      // Test: Ist der Eintrag aus der DB gelöscht?
      this.entityManager.clear();
      Assert.assertNull("Country", this.entityManager.find(Country.class, isoCode));
    }
    finally
    {
      // Gelöschten Eintrag wieder restaurieren
      try
      {
        EntityTransaction tx = this.entityManager.getTransaction();
        if (!tx.isActive())
        {
          tx.begin();
        }

        this.entityManager.persist(country);

        tx.commit();
      }
      catch (Exception ignore)
      {
      }
    }
  }

  /**
   * Test: Finden eines Einzeleintrags anhand seines carCode (Normale Query mit Positions-Parameter).
   */
  @Test
  public void test_05_findByCarCode()
  {
    this.log.info("----- test_05_findByCarCode -----");

    Country testCountry = testCountryDE;
    String carCode = testCountry.getCarCode();

    TypedQuery<Country> query = this.entityManager.createQuery("select c from Country c where c.carCode=?1", Country.class);
    query.setParameter(1, carCode);
    Country country = query.getSingleResult();

    this.log.info("Found: " + country);

    assertThat(country, is(testCountry));
  }

  /**
   * Test: Finden eines Einzeleintrags anhand seines phonePrefix (Named Query mit benanntem Parameter).
   */
  @Test
  public void test_06_findByPhonePrefix()
  {
    this.log.info("----- test_06_findByPhonePrefix -----");

    Country testCountry = testCountryDE;
    String phonePrefix = testCountry.getPhonePrefix();

    TypedQuery<Country> query = this.entityManager.createNamedQuery("Country_findByPhonePrefix", Country.class);
    query.setParameter("phonePrefix", phonePrefix);
    Country country = query.getSingleResult();

    this.log.info("Found: " + country);

    assertThat(country, is(testCountry));
  }

  @Test
  public void test_07_findAll()
  {
    this.log.info("----- test_07_findAll -----");

    TypedQuery<Country> query = this.entityManager.createQuery("select x from Country x", Country.class);
    List<Country> countries = query.getResultList();
    for (Country country : countries)
    {
      this.log.info("Found: " + country);
    }

    assertThat(countries.size(), is(testCountries.length));
    assertThat(countries, hasItems(testCountries));
  }

  /**
   * Test: Asiatische Länder finden (dynamisch erzeugte Named Query).
   */
  @Test
  public void test_08_findAsia()
  {
    this.log.info("----- test_08_findAsia -----");

    TypedQuery<Country> newQuery = this.entityManager.createQuery("select c from Country c where c.continent=:continent", Country.class);
    entityManagerFactory.addNamedQuery("Country_findByContinent", newQuery);

    Continent continent = Continent.ASIA;

    TypedQuery<Country> query = this.entityManager.createNamedQuery("Country_findByContinent", Country.class);
    query.setParameter("continent", continent);
    List<Country> countries = query.getResultList();
    for (Country country : countries)
    {
      this.log.info("Found: " + country);
    }

    assertThat(countries.size(), is(testCountriesAS.length));
    assertThat(countries, hasItems(testCountriesAS));
  }
}
