# lombok-demo

Demo for using Lombok

The class Country uses Lombok annotations for getters, setters, equals, hashCode and toString. Country is used as JPA entity.
In the same way City is a demo entity class, but uses a base class for it's generated id, equals, hashCode and toString.

CountryTest and CityTest demonstrate the usage of the "Lombokified" classes Country and City resp. as Java SE unit tests. The test database
is a in-memory H2 database.

The main application ist a war, which can be deployed onto a Java EE 7 server. country.html and city.xhtml are simple JSF views
for inserting new entries into the database represented by the default datasource of the application server used. 
