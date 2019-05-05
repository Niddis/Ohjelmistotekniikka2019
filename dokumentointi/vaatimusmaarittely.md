# Vaatimusmäärittely
## Sovelluksen tarkoitus

Sovelluksen avulla käyttäjät voivat pitää kirjaa käymistään sekä keskeneräisistä yliopistokursseista ja niiden arvosanoista sekä kokonaisopintopistemäärästä. Sovelluksella voi olla useampi käyttäjä, jolloin jokaiselle käyttäjälle näkyvät vain käyttäjän omat kurssit.

## Käyttäjät

Sovelluksen käyttäjillä on kaikilla sama rooli, opiskelija.

## Käyttöliittymäluonnos
### Graafinen käyttöliittymä

Sovelluksessa on viisi erilaista näkymää: ensimmäisenä aukeaa kirjautumisnäkymä, josta on mahdollisuus siirtyä uuden käyttäjän luomisnäkymään tai vaihtoehtoisesti, onnistuneen kirjautumisen yhteydessä, sovelluksen päänäkymään. Päänäkymässä näkyvät käyttäjän omat kurssit ja siinä on mahdollisuus järjestää kursseja nimen yms. mukaan. Siitä käyttäjä voi siirtyä uuden kurssin luomisnäkymään, kurssin muokkausnäkymään tai kirjautua ulos, jolloin siirrytään takaisin kirjautumisnäkymään. Uuden kurssin luomisnäkymässä käyttäjä voi luoda uuden kurssin ja siitä palataan takaisin päänäkymään. Samoin muokkausnäkymässä: muokkausten jälkeen palataan päänäkymään.

### Tekstikäyttöliittymä
Sovelluksen aloitusnäkymässä käyttäjä voi valita seitsemästä vaihtoehdosta: 1) uuden käyttäjän luominen, 2) sisäänkirjautuminen, 3) uloskirjautuminen, 4) uuden kurssin luominen, 5) omien kurssien näyttäminen, 6) kurssin poistaminen ja 7) kurssin muokkaaminen. Lisäksi käyttäjä voi poistua sovelluksesta. Vaihtoehdot 3-7 ovat käytettävissä vain jos käyttäjä on kirjautunut sisään. Jos käyttäjä valitsee vaihtoehdon 6 (kurssin poistaminen), hänelle näytetään lista omista kursseista ja pyydetään valitsemaan poistettava kurssi. Samoin jos käyttäjä valitsee vaihtoehdon 7 (kurssin muokkaaminen), hänelle näytetään lista omista kursseista ja pyydetään valitsemaan muokattava kurssi. Tämän jälkeen käyttäjälle tarjotaan neljä vaihtoehtoa: 1) kurssin nimen päivittäminen, 2) kurssin suoritustiedon päivittäminen, 3) kurssin pakollisuustiedon päivittäminen ja 4) kurssin opintopisteiden päivittäminen.

## Perusversion tarjoama toiminnallisuus

### Ennen kirjautumista
* käyttäjä voi luoda uuden käyttäjän, jolla on nimi, käyttäjätunnus ja salasana
 * käyttäjätunnuksen tulee olla 2-10 merkkiä pitkä ja uniikki
 * nimen tulee olla 2-50 merkkiä pitkä
 * salasanan tulee olla 5-15 merkkiä pitkä
* käyttäjä voi kirjautua järjestelmään
 * kirjautuminen onnistuu, jos käyttäjätunnus ja salasana ovat olemassa ja ne vastaavat tietokannasta löytyvää käyttäjätunnusta ja salasanaa
 * kirjautumisen epäonnistuessa sovellus ilmoittaa virheellisestä käyttäjätunnuksesta tai salasanasta

### Kirjautumisen jälkeen
* käyttäjä näkee lisäämänsä kurssit
 * jokaisesta kurssista näytetään nimi, onko kurssi suoritettu vai suorittamatta, onko kurssi pakollinen vai valinnainen ja opintopistemäärä
* käyttäjä voi järjestää kurssinäkymän nimen, suoritustiedon, pakollisuustiedon tai opintopisteiden mukaan
* käyttäjä voi luoda uuden kurssin
 * kurssin voi luoda joko syöttämällä itse kurssin tiedot (nimi, pakollisuustieto ja opintopisteet) tai valitsemalla kurssin valmiilta listalta (jos lista on olemassa)
 * kurssin nimen tulee olla 1-30 merkkiä pitkä
 * opintopisteiden tulee olla kokonaisluku väliltä 0-100
* käyttäjä voi päivittää kurssin tietoja
 * käyttäjä voi muuttaa kurssin nimeä, suoritustietoa, pakollisuustietoa ja opintopisteitä
* käyttäjä voi poistaa kurssin
* käyttäjä voi kirjautua ulos järjestelmästä

## Jatkokehitysideoita
* sovellukseen voisi lisätä mahdollisuuden omien tietojen hallintaan, esim. salasanan muuttamisen
* salasanojen parempi suojaus
 * nyt salasanat tallennetaan suojaamattomina suoraan tietokantaan
* sovelluksen tekstikäyttöliittymän laajennus
 * nyt tekstikäyttöliittymästä puuttuu mahdollisuus mm. kurssien järjestämiseen ja valmiin kurssilistan käyttämiseen