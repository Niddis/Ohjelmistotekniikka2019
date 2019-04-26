# Ohjelmistotekniikka 2019
Tämä on Helsingin yliopiston *Ohjelmistotekniikka* -kurssin harjoitustyö.

## Opintojen seurantasovellus
Sovelluksen käyttäjät voivat pitää kirjaa käymistään kursseista.
### Dokumentaatio
[Käyttöohje](../master/dokumentointi/kaytto_ohje.md)

[Vaatimusmäärittely](../master/dokumentointi/vaatimusmaarittely.md)

[Työaikakirjanpito](../master/dokumentointi/tyoaikakirjanpito.md)

[Arkkitehtuurikuvaus](../master/dokumentointi/arkkitehtuuri.md)

### Releaset

[Viikko 5](https://github.com/Niddis/Ohjelmistotekniikka2019/releases/tag/viikko5)

### Komentorivitoiminnot
#### Testaus
Testit suoritetaan komennolla
```
mvn test
```
Testikattavuusraportti luodaan komennolla
```
mvn test jacoco:report
```
Kattavuusraporttia voi tarkastella avaamalla selaimella tiedoston *target/site/jacoco/index.html*

#### Ohjelman suorittaminen
Ohjelma suoritetaan komennolla
```
mvn compile exec:java -Dexec.mainClass=studytrackerapp.Main
```
#### Suoritettavan jarin generointi
Komento
```
mvn package
```
generoi hakemistoon *target* suoritettavan jar-tiedoston *StudyTrackerApp-1.0-SNAPSHOT.jar*

#### Checkstyle
Tiedostoon [checkstyle.xml](../master/checkstyle.xml) määritellyt tarkistukset suoritetaan komennolla
```
mvn jxr:jxr checkstyle:checkstyle
```
Mahdolliset virheet selviävät avaamalla selaimella tiedoston *target/site/checkstyle.html*

#### JavaDoc
JavaDoc generoidaan komennolla
```
mvn javadoc:javadoc
```
JavaDocia voi tarkastella avaamalla selaimella tiedoston *target/site/apidocs/index.html*