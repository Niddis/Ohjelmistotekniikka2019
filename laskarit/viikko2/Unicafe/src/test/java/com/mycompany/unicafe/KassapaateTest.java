package com.mycompany.unicafe;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class KassapaateTest {
    
    Kassapaate kassa;
    Maksukortti kortti;
    
    public KassapaateTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        kassa = new Kassapaate();
        kortti = new Maksukortti(1000);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void kassanSaldoOnAlussaOikein() {
        assertEquals(100000, kassa.kassassaRahaa());
    }
    
    @Test
    public void myytyjenLounaidenMääräOnAlussaOikein() {
        int myytyjaLounaita = kassa.maukkaitaLounaitaMyyty()+kassa.edullisiaLounaitaMyyty();
        assertEquals(0, myytyjaLounaita);
    }
    
    @Test
    public void syoEdullisestiKateisostollaKasvattaaKassanRahamaaraaKunMaksuOnRiittava() {
        kassa.syoEdullisesti(240);
        assertEquals(100240, kassa.kassassaRahaa());
    }
    
    @Test
    public void syoMaukkaastiKateisostollaKasvattaaKassanRahamaaraaKunMaksuOnRiittava() {
        kassa.syoMaukkaasti(400);
        assertEquals(100400, kassa.kassassaRahaa());
    }
    
    @Test
    public void syoEdullisestiKateisostollaPalauttaaVaihtorahatOikein() {
        assertEquals(60, kassa.syoEdullisesti(300));
    }
    
    @Test
    public void syoMaukkaastiKateisostollaPalauttaaVaihtorahatOikein() {
        assertEquals(100, kassa.syoMaukkaasti(500));
    }
    
    @Test
    public void myytyjenEdullistenLounaidenMaaraKasvaaYhdellaJosMaksuOnRiittava() {
        kassa.syoEdullisesti(240);
        assertEquals(1, kassa.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void myytyjenMaukkaidenLounaidenMaaraKasvaaYhdellaJosMaksuOnRiittava() {
        kassa.syoMaukkaasti(400);
        assertEquals(1, kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void syoEdullisestiEiMuutaKassanRahamaaraaJosMaksuEiOleRiittava() {
        kassa.syoEdullisesti(200);
        assertEquals(100000, kassa.kassassaRahaa());
    }
    
    @Test
    public void syoMaukkaastiEiMuutaKassanRahamaaraaJosMaksuEiOleRiittava() {
        kassa.syoMaukkaasti(300);
        assertEquals(100000, kassa.kassassaRahaa());
    }
    
    @Test
    public void syoEdullisestiPalauttaaKaikkiRahatJosMaksuEiOleRiittava() {
        assertEquals(200, kassa.syoEdullisesti(200));
    }
    
    @Test
    public void syoMaukkaastiPalauttaaKaikkiRahatJosMaksuEiOleRiittava() {
        assertEquals(300, kassa.syoMaukkaasti(300));
    }
    
    @Test
    public void myytyjenEdullistenLounaidenMaaraEiKasvaJosMaksuEiOleRiittava() {
        kassa.syoEdullisesti(200);
        assertEquals(0, kassa.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void myytyjenMaukkaidenLounaidenMaaraEiKasvaJosMaksuEiOleRiittava() {
        kassa.syoMaukkaasti(300);
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void syoEdullisestiPienentaaKortinSummaaJosRahaaOnRiittavasti() {
        kassa.syoEdullisesti(kortti);
        assertEquals(760, kortti.saldo());
    }
    
    @Test
    public void syoMaukkaastiPienentaaKortinSummaaJosRahaaOnRiittavasti() {
        kassa.syoMaukkaasti(kortti);
        assertEquals(600, kortti.saldo());
    }
    
    @Test
    public void syoEdullisestiPalauttaaTrueJosRahaaOnKortillaRiittavasti() {
        assertEquals(true, kassa.syoEdullisesti(kortti));
    }
    
    @Test
    public void syoMaukkaastiPalauttaaTrueJosRahaaOnKortillaRiittavasti() {
        assertEquals(true, kassa.syoMaukkaasti(kortti));
    }
    
    @Test
    public void myytyjenEdullistenLounaidenMaaraKasvaaYhdellaJosRahaaOnKortillaRiittavasti() {
        kassa.syoEdullisesti(kortti);
        assertEquals(1, kassa.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void myytyjenMaukkaidenLounaidenMaaraKasvaaYhdellaJosRahaaOnKortillaRiittavasti() {
        kassa.syoMaukkaasti(kortti);
        assertEquals(1, kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void syoEdullisestiEiMuutaKortinSummaaJosRahaaEiOleRiittavasti() {
        kassa.syoMaukkaasti(kortti);
        kassa.syoMaukkaasti(kortti);
        kassa.syoEdullisesti(kortti);
        assertEquals(200, kortti.saldo());
    }
    
    @Test
    public void syoMaukkaastiEiMuutaKortinSummaaJosRahaaEiOleRiittavasti() {
        kassa.syoMaukkaasti(kortti);
        kassa.syoMaukkaasti(kortti);
        kassa.syoMaukkaasti(kortti);
        assertEquals(200, kortti.saldo());
    }
    
    @Test
    public void myytyjenEdullistenLounaidenMaaraEiKasvaJosRahaaEiOleKortillaRiittavasti() {
        kassa.syoMaukkaasti(kortti);
        kassa.syoMaukkaasti(kortti);
        kassa.syoEdullisesti(kortti);
        assertEquals(0, kassa.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void myytyjenMaukkaidenLounaidenMaaraEiKasvaJosRahaaEiOleKortillaRiittavasti() {
        kassa.syoEdullisesti(kortti);
        kassa.syoEdullisesti(kortti);
        kassa.syoEdullisesti(kortti);
        kassa.syoMaukkaasti(kortti);
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void syoEdullisestiPalauttaaFalseJosRahaaEiOleRiittavasti() {
        kassa.syoMaukkaasti(kortti);
        kassa.syoMaukkaasti(kortti);
        assertEquals(false, kassa.syoEdullisesti(kortti));
    }
    
    @Test
    public void syoMaukkaastiPalauttaaFalseJosRahaaEiOleRiittavasti() {
        kassa.syoEdullisesti(kortti);
        kassa.syoEdullisesti(kortti);
        kassa.syoEdullisesti(kortti);
        assertEquals(false, kassa.syoMaukkaasti(kortti));
    }
    
    @Test
    public void syoEdullisestiEiMuutaKassanRahamaaraaJosMaksetaanKortilla() {
        kassa.syoEdullisesti(kortti);
        assertEquals(100000, kassa.kassassaRahaa());
    }
    
    @Test
    public void syoMaukkaastiEiMuutaKassanRahamaaraaJosMaksetaanKortilla() {
        kassa.syoMaukkaasti(kortti);
        assertEquals(100000, kassa.kassassaRahaa());
    }
    
    @Test
    public void rahanLataaminenKasvattaaKortillaOlevaaSummaa() {
        kassa.lataaRahaaKortille(kortti, 100);
        assertEquals(1100, kortti.saldo());
    }
    
    @Test
    public void rahanLataaminenKasvattaaKassassaOlevaaSummaa() {
        kassa.lataaRahaaKortille(kortti, 100);
        assertEquals(100100, kassa.kassassaRahaa());
    }
    
    @Test
    public void negatiivinenSummaEiKasvataKortillaOlevaaSummaa() {
        kassa.lataaRahaaKortille(kortti, -100);
        assertEquals(1000, kortti.saldo());
    }
    
    @Test
    public void negatiivinenSummaEiKasvataKassassaOlevaaSummaa() {
        kassa.lataaRahaaKortille(kortti, -100);
        assertEquals(100000, kassa.kassassaRahaa());
    }
}
