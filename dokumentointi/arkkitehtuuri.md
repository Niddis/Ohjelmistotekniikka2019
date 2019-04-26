# Arkkitehtuurikuvaus
## Rakenne

Ohjelman rakenne koostuu kolmesta pakkauksesta, jotka muodostavat kolmitasoisen kerrosrakenteen: Pakkaus StudyTrackerApp.ui sisältää sekä tekstikäyttöliittymän että JavaFX:llä toteutetun graafisen käyttöliittymän, StudyTrackerApp.domain sisältää sovelluslogiikan ja StudyTrackerApp.database sisältää tiedon pysyväistallennuksesta vastaavat luokat ja metodit.

## Sovelluslogiikka

Sovelluksessa on kaksi ydinluokkaa User ja Course, joille muut luokat tarjoavat palveluja. 

<img src="https://raw.githubusercontent.com/Niddis/Ohjelmistotekniikka2019/master/dokumentointi/kuvat/luokkakaavio.png">

## Tietojen pysyväistallennus

Pakkauksen studytrackerapp.database luokat SqlUserDao ja SqlCourseDao vastaavat tietojen tallettamisesta Sqlite -tietokantaan.

### Päätoiminnallisuudet
#### Käyttäjän kirjautuminen

Kun käyttäjä antaa käyttöliittymässä komennon "2" ja syöttää sitten käyttäjätunnuksen ja salasanan, sovelluksen kontrolli etenee seuraavasti:

<img src="https://raw.githubusercontent.com/Niddis/Ohjelmistotekniikka2019/master/dokumentointi/kuvat/userLogin.png">

Käyttöliittymä kutsuu sovelluslogiikkaluokan *Service* metodia *login*, joka saa parametreikseen käyttäjätunnuksen ja salasanan. Service kutsuu seuraavaksi *SqlUserDao*:a, joka etsii käyttäjän tietokannasta käyttäjätunnuksen perusteella. Jos käyttäjä löydetään ja annettu salasana vastaa tietokannasta löytyvää salasanaa, sisäänkirjautuminen onnistuu ja käyttäjälle ilmoitetaan asiasta.