package de.gedoplan.showcase;

import static org.junit.Assert.assertEquals;

import de.gedoplan.showcase.entity.City;

import java.util.List;

import javax.persistence.TypedQuery;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.unitils.reflectionassert.ReflectionAssert;

//CHECKSTYLE:OFF

/**
 * Test der Persistence-Fuktionalität bzgl. der Entity City.
 *
 * @author dw
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CityTest extends TestBase
{
  public static City   testCity1  = new City("Berlin", 3500000, 892);
  public static City   testCity2  = new City("Bielefeld", 325000, 258);
  public static City   testCity3  = new City("Dortmund", 580000, 280);
  public static City   testCity4  = new City("Mannheim", 315000, 145);
  public static City   testCity5  = new City("Stuttgart", 600000, 207);
  public static City[] testCities = { testCity1, testCity2, testCity3, testCity4, testCity5 };

  @Test
  public void test_00_clearData()
  {
    this.log.info("----- test_00_clearData -----");

    this.entityManager.createNativeQuery("delete from City").executeUpdate();
  }

  @Test
  public void test_01_insertData() throws Exception
  {
    this.log.info("----- test_01_insertData -----");

    for (City city : testCities)
    {
      this.entityManager.persist(city);

      this.log.debug("Inserted: " + city);
    }
  }

  /**
   * Test: Sind die Testdaten korrekt in der DB?
   */
  @Test
  public void test_02_findAll()
  {
    this.log.info("----- test_02_findAll -----");

    TypedQuery<City> query = this.entityManager.createQuery("select x from City x order by x.name", City.class);
    List<City> cities = query.getResultList();

    assertEquals("City count", testCities.length, cities.size());
    for (int i = 0; i < testCities.length; ++i)
    {
      City testCity = testCities[i];
      City city = cities.get(i);

      this.log.debug("Found: " + city);

      /*
       * In einem Test, in dem nur diese Methode läuft, ist testCity.id null. LenientEquals ignoriert null-Werte, so dass in
       * diesem Fall keine Ungleichheit angezeigt wird.
       */
      ReflectionAssert.assertLenientEquals("City", testCity, city);
    }
  }
}
