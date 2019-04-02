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
#### Ohjelman suorittaminen
Ohjelma suoritetaan komennolla
```
mvn compile exec:java -Dexec.mainClass=studytrackerapp.Main
```
