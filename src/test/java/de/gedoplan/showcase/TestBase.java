package de.gedoplan.showcase;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;

/**
 * Basisklasse für JPA-Tests.
 *
 * Diese Klasse hält eine einmalig initialisierte EntityManagerFactory für alle Tests bereit. Zusätzlich wird vor jedem Test ein
 * EntityManager erstellt und eine Transaktion gestartet. Die Transaktion wird nach jedem Test wieder beendet und der
 * EntityManager geschlossen.
 *
 * @author dw
 *
 */
public abstract class TestBase
{
  protected static EntityManagerFactory entityManagerFactory;
  protected EntityManager               entityManager;

  protected Log                         log = LogFactory.getLog(getClass());

  /**
   * EntityManagerFactory liefern.
   *
   * Die EntityManagerFactory wird beim ersten Aufruf erstellt.
   *
   * @return EntityManagerFactory
   */
  protected synchronized EntityManagerFactory getEntityManagerFactory()
  {
    if (entityManagerFactory == null)
    {
      this.log.trace("create entitymanager factory");
      entityManagerFactory = Persistence.createEntityManagerFactory("test");
    }
    return entityManagerFactory;
  }

  /**
   * Test-Vorbereitung: EntityManager öffnen und Transakion starten.
   */
  @Before
  public void before()
  {
    this.log.trace("create entitymanager and start transaction");
    this.entityManager = getEntityManagerFactory().createEntityManager();
    this.entityManager.getTransaction().begin();
  }

  /**
   * Test-Nachbereitung: Transaktion beenden und EntityManager schliessen.
   */
  @After
  public void after()
  {
    try
    {
      EntityTransaction transaction = this.entityManager.getTransaction();
      if (transaction.isActive())
      {
        if (!transaction.getRollbackOnly())
        {
          this.log.trace("commit transaction");
          transaction.commit();
        }
        else
        {
          this.log.trace("rollback transaction");
          transaction.rollback();
        }
      }
    }
    finally
    {
      try
      {
        this.log.trace("close entitymanager");
        this.entityManager.close();
      }
      catch (Exception e) // CHECKSTYLE:IGNORE
      {
        // ignore
      }
    }
  }

}
