# Testausdokumentti

Sovellukselle on tehty yksikkö- ja integraatiotestit JUnitilla. Lisäksi sovelluksen järjestelmätasoa on testattu manuaalisesti.

## Yksikkö- ja integraatiotestaus

### Sovelluslogiikka

Sovelluslogiikkaa, eli käytännössä pakkauksen studytrackerapp.domain Service-luokkaa, testataan integraatiotestein, jotka jäljittelevät sovelluksen toimintaa. Testit hyödyntävät olemassa olevia SqlUserDao- ja SqlCourseDao -luokkia ja erillistä testitietokantaa. Luokkia User ja Course ei testata erikseen, mutta suurin osa niiden toiminnallisuudesta tulee testatuksi Service -luokan testien kautta.

### DAO-luokat

SqlUserDao- ja SqlCourseDao -luokat käyttävät samaa testitietokantaa kuin sovelluslogiikan testitkin. Jokaisen testin jälkeen tietokantataulut poistetaan testitietokannasta ja ennen jokaista testiä testitietokanta alustetaan uudelleen.

### Testauskattavuus

Testauksen rivikattavuus on 77 % ja haaraumakattavuus 70 %. Käyttöliittymät on jätetty testauskattavuuden ulkopuolelle.

<img src="https://raw.githubusercontent.com/Niddis/Ohjelmistotekniikka2019/master/dokumentointi/kuvat/Ohte_jacoco.png">

Testauksen ulkopuolelle jäävät Service -luokan toinen konstruktori ja readCourseFile -metodi sekä esim. sellaiset tapaukset, jossa tietokannan muokkaaminen päättyy virheeseen.

## Järjestelmätestaus

### Asennus ja konfigurointi

Sovelluksen jar-tiedostoa on testattu käyttöohjeen kuvaamalla tavalla Linux-ympäristössä. Käynnistyshakemistossa on tällöin ollut käyttöohjeissa kuvattu *config.properties* -tiedosto. Testauksessa on käyty läpi tilanteet, joissa sekä tietokanta että *courses.txt* -tiedosto puuttuvat, jompikumpi on ollut olemassa tai molemmat ovat olleet olemassa sovelluksen käynnistyessä.

### Toiminnallisuudet

Kaikki määrittelydokumentissa ja käyttöohjeessa esitellyt toiminnallisuudet on käyty läpi sekä teksti- että graafisessa käyttöliittymässä. Toimintoja on testattu myös virheellisillä arvoilla, kuten tyhjäksi jätetyillä kentillä ja syöttämällä kokonaisnumeroa vaativiin kenttiin muuta kuin numeroita.

## Sovellukseen jääneet laatuongelmat

* tekstikäyttöliittymässä käyttäjän syötteiden validointia voisi parantaa