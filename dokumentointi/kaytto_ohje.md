# Käyttöohje

Lataa tiedosto sta_v2.jar. Jos haluat käyttää tekstikäyttöliittymää, joudut kopioimaan sovelluksen lähdekoodin ja käynnistämään ohjelman esim. komentoriviltä.

## Konfigurointi

Ohjelman käynnistyshakemistossa täytyy olla konfiguraatiotiedosto config.properties, johon on määritelty käytettävän sqlite-tietokannan osoite sekä valmiin kurssilistan nimi. Tiedoston muoto on seuraavanlainen:
```
databaseAddress=jdbc:sqlite:sta.db
courseFile=courses.txt
```
### Tietokanta
Sovellus käyttää tietojen tallentamiseen sqlite3-tietokantaa. Jos käynnistyshakemistossa ei ole olemassa olevaa tietokantaa, sovellus luo uuden tietokannan.

### Kurssilista

Käynnistyshakemistoon voi lisätä .txt-tiedoston, johon on lisätty kursseja riveittäin seuraavassa muodossa
```
kussin nimi;pakollisuustieto (1 tai 0);opintopisteet
```
jossa 1 tarkoittaa pakollista kurssia ja 0 valinnaista. Eli esim. 
```
Ohte;1;5
```
Tällöin käyttäjä voi uutta kurssia lisätessään valita kurssin suoraan listasta.

## Sovelluksen käynnistäminen

Sovellus käynnistetään komennolla
```
java -jar sta_v2.jar
```

## Graafinen käyttöliittymä

### Kirjautuminen sisään

Sovellus avautuu kirjautumisnäkymään.
Kirjautuminen tapahtuu kirjoittamalla olemassa oleva käytäjätunnus ja siihen liittyvä salasana syötekenttiin ja painamalla *kirjaudu* -painiketta.

### Uuden käyttäjän luominen

Kirjautumisnäkymästä pääsee luomaan uuden käyttäjän painamalla *luo uusi tunnus* -painiketta.
Uuden käyttäjän luominen tapahtuu kirjoittamalla käyttäjän nimen, käyttäjätunnuksen ja salasanan syötekenttiin ja painamalla *Luo uusi käyttäjä* -painiketta. Kaikki kentät ovat pakollisia ja niihin liittyvät seuraavat vaatimukset:
* nimen on oltava 2-50 merkkiä pitkä (alussa tai lopussa olevia välilyöntejä ei lasketa)
* käyttäjätunnuksen on oltava 2-10 merkkiä pitkä (alussa tai lopussa olevia välilyöntejä ei lasketa) ja uniikki (jos sovellus ilmoittaa, että käyttäjätunnus on jo olemassa, käyttäjän on keksittävä uusi tunnus)
* salasanan on oltava 5-15 merkkiä pitkä (alussa tai lopussa olevia välilyöntejä ei lasketa)
* salasana annetaan kaksi kertaa ja molempien kenttien on vastattava toisiaan

Jos käyttäjän luominen onnistuu, palataan kirjautumisnäkymään. Toinen vaihtoehto on painaa *takaisin* -painiketta, jolloin käyttäjän luominen keskeytyy ja palataan kirjautumisnäkymään.

### Kurssien tarkastelu ja järjestäminen

Onnistuneen kirjautumisen jälkeen siirrytään sovelluksen päänäkymään, jossa näkyvät kirjautuneen käyttäjän kaikki kurssit. Kurssit on oletuarvoisesti järjestetty kurssin nimen mukaan ja käyttäjä voi muuttaa kurssien järjestystä valitsemalla yläriviltä mieleisensä järjestysvaihtoehdon ja painamalla *järjestä* -painiketta: 
* "nimi" järjestää kurssit nimen perusteella aakkosjärjestykseen
* "suoritettu" näyttää ensin suoritetut kurssit ja sitten suorittamattomat
* "pakollinen" näyttää ensin pakolliset kurssit ja sitten valinnaiset
* "pisteet" järjestää kurssit opintopisteiden mukaan pienimmästä suurimpaan

### Kurssin poistaminen

Kurssin voi poistaa painamalla kurssin perässä olevaa *poista* -painiketta. Poistaminen on pysyvää eikä kurssia saa takaisin muuten kuin luomalla sen uudestaan.

### Kurssin muokkaaminen

Kurssin tietoja voi päivittää painamalla kurssin perässä olevaa *muokkaa* -painiketta. Sovellus siirtyy kurssin muokkausnäkymään, jossa näkyvät kurssin tämänhetkiset tiedot. Kurssin nimeä ja opintopisteitä voi muokata kirjoittamalla uudet tiedot syötekenttiin ottaen huomioon seuraavat rajoitteet:
* nimen on oltava 2-30 merkkiä pitkä (alussa tai lopussa olevia välilyötejä ei lasketa)
* opintopisteiden on oltava kokonaisluku väliltä 0-100

Kurssin suoritus- ja pakollisuustietoa voi muokata valitsemalla "kyllä" tai "ei". Tietojen tallennus tapahtuu painamalla *tallenna muutokset* -painiketta. Onnistuneen tallenuksen jälkeen siirrytään takaisin päänäkymään. Muokkausnäkymästä pääsee päänäkymään myös *takaisin* -painikkeella.

### Uuden kurssin lisääminen

Uuden kurssin lisääminen tapahtuu painamalla päänäkymässä *lisää kurssi* -painiketta, josta siirrytään uuden kurssin luomisnäkymään. Kurssin lisääminen onnistuu joko valitsemalla pudotusvalikosta haluamansa kurssin ja painamalla *valitse kurssi* -painiketta, tai vaihtoehtoisesti kirjoittamalla kurssin nimen ja opintopisteet syötekenttiin, valitsemalla "kyllä" tai "ei" kohdasta *onko kurssi pakollinen?" sekä painamalla lopuksi "luo kurssi* -painiketta. Uuden kurssin suoritustieto on oletusarvoisesti "suorittamatta". Kaikki kentät ovat pakollisia ja niihin liittyvät seuraavat rajoitteet:
* nimen on oltava 2-30 merkkiä pitkä (alussa tai lopussa olevia välilyötejä ei lasketa)
* opintopisteiden on oltava kokonaisluku väliltä 0-100

Onnistuneen kurssin valitsemisen/luomisen jälkeen siirrytään takaisin päänäkymään. Luomisnäkymästä pääsee päänäkymään myös *takaisin* -painikkeella.


### Ulos kirjautuminen

Ulos kirjautuminen tapahtuu painamalla päänäkymässä olevaa *kirjaudu ulos* -painiketta. Tällöin sovellus palaa kirjautumisnäkymään.

## Tekstikäyttöliittymä

### Yleisesti

Joka kerta kun olet kirjoittanut jotain konsoliin eli antanut syötteen, paina enter päästäksesi eteenpäin. Tekstikäyttöliittymässä ei voi muuttaa kurssien järjestystä tai valita kursseja valmiista listasta.

### Kirjautuminen sisään

Sovelluksen aloitusnäkymässä kirjautuminen tapahtuu syöttämällä konsoliin ensin numero 1. Seuraavaksi syötetään olemassa oleva käyttäjätunnus ja lopuksi salasana.

### Uuden käyttäjän luominen

Sovelluksen aloitusnäkymässä syötetään konsoliin numero 2. Tämän jälkeen syötetään uuden käyttäjän nimi, itse keksitty käyttäjätunnus ja salasana. Lopuksi täytyy vielä kirjautua sisään, jotta pääsee tarkastelemaan ja muokkaamaan kursseja.

### Kirjautuminen ulos

Sovelluksen aloitusnäkymässä syötetään konsoliin numero 3. Uloskirjautuminen tapahtuu automaattisesti myös sovelluksen sulkeutuessa.

### Uuden kurssin luominen

Sovelluksen aloitusnäkymässä syötetään konsoliin numero 4. Seuraavaksi syötetään kurssin nimi, numero 1 tai 0 riippuen siitä onko kurssi pakollinen vai valinnainen, sekä kurssista saatavien opintopisteiden määrä. Uusi kurssi on oletusarvoisesti "suorittamatta".

### Omien kurssien listaus

Sovelluksen aloitusnäkymässä syötetään konsoliin numero 5. Tulosteen lopussa ilmoitetaan suoritettujen kurssien opintopisteiden yhteismäärä.

### Kurssin poistaminen

Sovelluksen aloitusnäkymässä syötetään konsoliin numero 6. Sovellus tulostaa listan kaikista sisään kirjautuneen käyttäjän kursseista ja jokaisen kurssin alussa oleva numero on kyseisen kurssin id. Poistettava kurssi valitaan syöttämällä konsoliin sen id. Kurssi poistetaan pysyvästi, eikä sitä voi enää palauttaa muuten kuin luomalla kyseisen kurssin uudelleen.

### Kurssin tietojen päivittäminen

Sovelluksen aloitusnäkymässä syötetään konsoliin numero 7. Sovellus tulostaa listan kaikista sisään kirjautuneen käyttäjän kursseista ja jokaisen kurssin alussa oleva numero on kyseisen kurssin id. Muokattava kurssi valitaan syöttämällä konsoliin sen id. Seuraavaksi valitaan haluttu muokkaustoiminto:
* jos halutaan muuttaa kurssin nimeä, syötetään konsoliin numero 2 ja annetaan sitten uusi nimi
* jos halutaan vaihtaa kurssin suoritustieto suorittamattomasta suoritetuksi tai päinvastoin, syötetään konsoliin numero 1
* jos halutaan vaihtaa kurssin pakollisuustieto pakollisesta valinnaiseksi tai päinvastoin, syötetään konsoliin numero 3
* jos halutaan muuttaa kurssin opintopisteistä, syötetään konsoliin numero 4 ja annetaan sitten uudet opintopisteet

### Sovelluksesta poistuminen

Sovelluksen aloitusnäkymässä syötetään konsoliin kirjain x (pienellä kirjoitettuna).