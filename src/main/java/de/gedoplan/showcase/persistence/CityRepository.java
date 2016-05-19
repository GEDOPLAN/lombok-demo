package de.gedoplan.showcase.persistence;

import de.gedoplan.baselibs.persistence.repository.SingleIdEntityRepository;
import de.gedoplan.showcase.entity.City;

import javax.transaction.Transactional;

@Transactional
public class CityRepository extends SingleIdEntityRepository<Integer, City>
{
}
