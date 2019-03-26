package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(100);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }
    
    @Test
    public void konstruktoriAsettaaSaldonOikein() {
        assertEquals(100, kortti.saldo());
    }
    
    @Test
    public void rahanLataaminenKasvattaaSaldoaOikein() {
        kortti.lataaRahaa(250);
        assertEquals(350, kortti.saldo());
    }
    
    @Test
    public void rahanOttaminenPienentääSaldoaOikeinJosRahaaOnTarpeeksi() {
        kortti.otaRahaa(50);
        assertEquals(50, kortti.saldo());
    }
    
    @Test
    public void rahanOttaminenEiMuutaSaldoaJosRahaaEiOleTarpeeksi() {
        kortti.otaRahaa(150);
        assertEquals(100, kortti.saldo());
    }
    
    @Test
    public void otaRahaaPalauttaaTrueJosRahaaOnTarpeeksi() {
        assertEquals(true, kortti.otaRahaa(50));
    }
    
    @Test
    public void otaRahaaPalauttaaFalseJosRahaaEiOleTarpeeksi() {
        assertEquals(false, kortti.otaRahaa(150));
    }
    
    @Test
    public void toStringNäyttääSadonOikein() {
        assertEquals("saldo: 1.0", kortti.toString());
    }
     
}
