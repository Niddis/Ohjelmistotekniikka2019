# Arkkitehtuurikuvaus
## Sovelluslogiikka

<img src="https://raw.githubusercontent.com/Niddis/Ohjelmistotekniikka2019/master/dokumentointi/kuvat/luokkakaavio.png">

## Tietojen pysyväistallennus
### Päätoiminnallisuudet
#### Käyttäjän kirjautuminen

Kun käyttäjä antaa käyttöliittymässä komennon "2" ja syöttää sitten käyttäjätunnuksen ja salasanan, sovelluksen kontrolli etenee seuraavasti:

<img src="https://raw.githubusercontent.com/Niddis/Ohjelmistotekniikka2019/master/dokumentointi/kuvat/userLogin.png">

Käyttöliittymä kutsuu sovelluslogiikkaluokan *Service* metodia *login*, joka saa parametreikseen käyttäjätunnuksen ja salasanan. Service kutsuu seuraavaksi *SqlUserDao*:a, joka etsii käyttäjän tietokannasta käyttäjätunnuksen perusteella. Jos käyttäjä löydetään ja annettu salasana vastaa tietokannasta löytyvää salasanaa, sisäänkirjautuminen onnistuu ja käyttäjälle ilmoitetaan asiasta.