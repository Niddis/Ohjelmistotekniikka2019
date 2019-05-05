# Arkkitehtuurikuvaus
## Rakenne

Ohjelman rakenne koostuu kolmesta pakkauksesta, jotka muodostavat kolmitasoisen kerrosrakenteen: Pakkaus StudyTrackerApp.ui sisältää sekä tekstikäyttöliittymän että JavaFX:llä toteutetun graafisen käyttöliittymän, StudyTrackerApp.domain sisältää sovelluslogiikan ja StudyTrackerApp.database sisältää tiedon pysyväistallennuksesta vastaavat luokat ja metodit.

## Graafinen käyttöliittymä

Käyttöliittymä koostuu viidestä eri näkymästä:
* sisäänkirjautuminen
* uuden käyttäjän luominen
* kurssien listaus (päänäkymä)
* uuden kurssin lisääminen
* kurssin muokkaaminen

Kaikki näkymät on toteutettu omina Scene-olioinaan, joista vain yksi on kerrallaan näkyvissä (eli sijoitettuna sovelluksen stageen). Käyttöliittymä on oma luokkansa, joka huolehtii vain tiedon esittämisestä. Varsinaisesta sovelluslogiikasta vastaa Service -luokka, jonka metodeja käyttöliittymä kutsuu. Service -luokalla on oma Course -olioista muodostuva listansa, jota käyttöliittymä hyödyntää päänäkymän muuttuessa kurssien lisäämisen, muokkaamisen, poistamisen tai järjestämisen yhteydessä. Käyttöliittymä vastaa myös käyttäjän syötteen validoinnista.

## Sovelluslogiikka

Sovelluksessa on kaksi ydinluokkaa User ja Course, jotka kuvaavat käyttäjiä ja käyttäjien kursseja. 

<img src="https://raw.githubusercontent.com/Niddis/Ohjelmistotekniikka2019/master/dokumentointi/kuvat/Ohte_userAndCourse.png">

Luokasta Service luotu olio vastaa toiminnallisista kokonaisuuksista ja uusien User- ja Course-olioiden luomisesta sekä toimii käyttöliittymän ja SqlUserDao- ja SqlCourseDao -luokkien välissä, niin että käyttöliittymä ei suoraan pääse käsiksi kyseisiin luokkiin tai päinvastoin. Service -luokan tarjoamia metodeja ovat mm.

* boolean login (String username, String password)
* boolean createNewCourse(String name, int compulsory, int points)
* List<Course> listCoursesByUser()
* void sortCoursesList(String sorter)

<img src="https://raw.githubusercontent.com/Niddis/Ohjelmistotekniikka2019/master/dokumentointi/kuvat/luokkakaavio.png">

## Tietojen pysyväistallennus

Pakkauksen studytrackerapp.database luokat SqlUserDao ja SqlCourseDao toteuttavat UserDao- ja CourseDao -rajapinnat ja  vastaavat tietojen tallettamisesta Sqlite -tietokantaan. Dao -luokat injektoidaan sovelluslogiikalle konstruktorikutsun yhteydessä.

### Tietokanta

Luokka Database vastaa tietokantayhteyden muodostamisesta ja tietokannan alustamisesta. Jos sovelluksen käynnistyshakemistossa ei käynnistyshetkellä ole olemassa olevaa tietokantaa, sellainen luodaan. Tietokantataulujen luomisessa käytetään seuraavia lauseita:
```
CREATE TABLE IF NOT EXISTS User (id integer PRIMARY KEY, name varchar(50), username varchar(10), password varchar(15));
CREATE TABLE IF NOT EXISTS Course (id integer PRIMARY KEY, user_id integer, name varchar(50), done integer, compulsory integer, points integer, FOREIGN KEY (user_id) REFERENCES User(id));
```
Tietokannan osoite on määritelty config.properties -tiedostossa.

### Päätoiminnallisuudet
#### Käyttäjän kirjautuminen

Kun käyttäjä syöttää käyttöliittymän kirjautumisnäkymässä käyttäjätunnuksen ja salasanan ja painaa sitten *kirjaudu* -painiketta, sovelluksen kontrolli etenee seuraavasti:

<img src="https://raw.githubusercontent.com/Niddis/Ohjelmistotekniikka2019/master/dokumentointi/kuvat/Ohte_login.png">

Käyttöliittymä kutsuu sovelluslogiikkaluokan *Service* metodia *login*, joka saa parametreikseen käyttäjätunnuksen ja salasanan. Service kutsuu seuraavaksi *UserDao*:a, joka etsii käyttäjän tietokannasta käyttäjätunnuksen perusteella. Jos käyttäjä löydetään ja annettu salasana vastaa tietokannasta löytyvää salasanaa, sisäänkirjautuminen onnistuu ja käyttöliittymä vaihtaa näkymäksi *coursesScene*:n eli sovelluksen päänäkymän.

#### Uuden käyttäjän luominen

Kun käyttäjä syöttää uuden käyttäjän luomisnäkymässä käyttäjätunnuksen joka ei ole käytössä sekä nimen ja salasanan ja painaa *luo uusi käyttäjä* -painiketta, sovelluksen kontrolli etenee seuraavasti:

<img src="https://raw.githubusercontent.com/Niddis/Ohjelmistotekniikka2019/master/dokumentointi/kuvat/Ohte_newUser.png">

Käyttöliittymä kutsuu *Service* -luokan metodia *createNewUser*, joka saa parametreinaan luotavan käyttäjän tiedot. Service kutsuu seuraavaksi *UserDao* -luokan metodia *getAll*, joka palauttaa listan tallennetuista *User* -olioista. Jos parametrina saatua käyttäjätunnusta ei löydy listasta, luodaan uusi User -olio, joka talletetaan kutsumalla UserDao:n metodia *create*. Onnistuneen tallennuksen seurauksena käyttöliittymä vaihtaa näkymäksi *loginScene*:n eli kirjautumisnäkymän.

#### Muut toiminnallisuudet

Muut toiminnallisuudet toimivat samalla periaatteella: käyttöliittymä kutsuu sopivaa Service -luokan metodia, joka puolestaan kutsuu sopivaa UserDao- tai CourseDao -luokan metodia. Saatuaan tiedon onnistuneesta muutoksesta käyttöliittymä päivittää aktiivisen näkymän ja/tai kurssien listauksen.

## Ohjelman rakenteeseen jääneet heikkoudet
### Käyttöliittymä
Graafinen käyttöliittymä koostuu yhdestä luokasta, joka sisältää sekä graafisten elementtien luomisen että kommunikoinnin muiden luokkien kanssa. Vaikka luokka on jaettu useisiin metodeihin, sitä voi olla vaikea tulkita. Käyttöliittymän grafiikan voisi ehkä luoda muulla tapaa, jolloin luokan koko pienentyisi.

### Sovelluslogiikka

Service -luokka sisältää nyt kaksi eri konstruktoria, joista toinen on tekstikäyttöliittymää ja toinen graafista käyttöliittymää varten. Samoin osa metodeista on vain tekstikäyttöliittymän ja osa vain graafisen käyttöliittymän käytössä. Service -luokka olisi ehkä syytä jakaa kahdeksi erilliseksi, eri käyttöliittymiä palvelevaksi luokaksi, jotka mahdollisesti perivät abstraktin luokan. Tai sitten tekstikäyttöliittymää voisi laajentaa niin, että hyödyntäisi suurempaa osaa Service -luokan metodeista.