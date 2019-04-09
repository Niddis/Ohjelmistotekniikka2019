# Ohjelmistotekniikka 2019
Tämä on Helsingin yliopiston *Ohjelmistotekniikka* -kurssin harjoitustyö.

## Opintojen seurantasovellus
Sovelluksen käyttäjät voivat pitää kirjaa käymistään kursseista.
### Dokumentaatio
[Vaatimusmäärittely](../master/dokumentointi/vaatimusmaarittely.md)

[Työaikakirjanpito](../master/dokumentointi/tyoaikakirjanpito.md)

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
#### Checkstyle
Tiedostoon [checkstyle.xml](../master/checkstyle.xml) määritellyt tarkistukset suoritetaan komennolla
```
mvn jxr:jxr checkstyle:checkstyle
```
Mahdolliset virheet selviävät avaamalla selaimella tiedoston *target/site/checkstyle.html*
