package fxJuhlarekisteri.Tietorakenne;

import fi.jyu.mit.ohj2.WildChars;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import fxJuhlarekisteri.SailoException; 

/**
 * @author Sampo Kupiainen & Sami Järvinen
 * @version 11.6.2019
 */
public class Juhlat implements Iterable<Juhla> {
    private static final int MAX_Juhlia   = 10;
    private boolean muutettu = false;
    private int              lkm           = 0;
    private String kokoNimi = "";
    private String tiedostonPerusNimi = "juhlat";
    private Juhla            alkiot[]      = new Juhla[MAX_Juhlia];

    
    /**
     * Oletusmuodostaja
     */
    public Juhlat() {
        // Attribuuttien oma alustus riittää
    }


    /**
     * Lisää uuden juhlan tietorakenteeseen.  
     * @param juhla Juhlan listaus
     */
    public void lisaa(Juhla juhla){
        if (lkm >= alkiot.length) alkiot = Arrays.copyOf(alkiot, lkm+20);
        alkiot[lkm] = juhla;
        lkm++;
        muutettu = true;
    }

    /**
     * @param juhla lisättävän juhlan viite
     * @throws SailoException jos tietorakenne on jo täynnä
     */
    public void korvaaTaiLisaa(Juhla juhla) throws SailoException {
        int id = juhla.getTunnusNro();
        for (int i = 0; i < lkm; i++) {
            if ( alkiot[i].getTunnusNro() == id ) {
                alkiot[i] = juhla;
                muutettu = true;
                return;
            }
        }
        lisaa(juhla);
    }
    
    
    /**
     * Palauttaa viitteen i:teen juhlaan.
     * @param i monennenko juhlan viite halutaan
     * @return viite juhlaan, jonka indeksi on i
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella  
     */
    public Juhla anna(int i) throws IndexOutOfBoundsException {
        if ( i < 0 || lkm <= i ) throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }
    
    
    /**
     * Lukee juhlien tiedot tiedostosta. 
     * @param tied tiedoston perusnimi
     * @throws SailoException jos lukeminen ep�onnistuu
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     * 
     *  Juhlat juhlat = new Juhlat();
     *  Juhla juhla1 = new Juhka(), juhla2 = new Juhla();
     *  juhla1.vastaaJuhla();
     *  juhla2.vastaaJuhla();
     *  String hakemisto = "testijuhlat";
     *  String tiedNimi = hakemisto+"/nimet";
     *  File ftied = new File(tiedNimi+".dat");
     *  File dir = new File(hakemisto);
     *  dir.mkdir();
     *  ftied.delete();
     *  juhlat.lueTiedostosta(tiedNimi); #THROWS SailoException
     *  juhlat.lisaa(juhla1);
     *  juhlat.lisaa(juhla2);
     *  juhlat.tallenna();
     *  juhlat = new Juhlat();            // Poistetaan vanhat luomalla uusi
     *  juhlat.lueTiedostosta(tiedNimi);  // johon ladataan tiedot tiedostosta.
     *  Iterator<Juhla> i = juhlat.iterator();
     *  i.next() === juhla1;
     *  i.next() === juhla22;
     *  i.hasNext() === false;
     *  juhlat.lisaa(juhla2);
     *  juhlat.tallenna();
     *  ftied.delete() === true;
     *  File fbak = new File(tiedNimi+".bak");
     *  fbak.delete() === true;
     *  dir.delete() === true;
     * </pre>
     */
    public void lueTiedostosta(String tied) throws SailoException {
        setTiedostonPerusNimi(tied);
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
            kokoNimi = fi.readLine();
            if ( kokoNimi == null ) throw new SailoException("Rekisterin nimi puuttuu");
            String rivi = fi.readLine();
            if ( rivi == null ) throw new SailoException("Maksimikoko puuttuu");

            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Juhla juhla = new Juhla();
                juhla.parse(rivi);
                lisaa(juhla);
            }
            muutettu = false;
        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonPerusNimi() + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
    }

    
    /**
     * Luetaan annetun nimisest� tiedostosta
     * @throws SailoException jos tulee poikkeus
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }
    
    
    /**
     * Tallentaa juhlat tiedostoon.  
     * @throws SailoException jos talletus epäonnistuu
     */
    public void tallenna() throws SailoException {
        if ( !muutettu ) return;

        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete();
        ftied.renameTo(fbak);

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            fo.println(getKokoNimi());
            fo.println(alkiot.length);
            for (Juhla juhla : this) {
                fo.println(juhla.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }
        muutettu = false;
    }

    
    /**
     * Palauttaa Rekisterin koko nimen
     * @return Rekisterin koko nimi merkkijononna
     */
    public String getKokoNimi() {
        return kokoNimi;
    }
    
    
    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }
    
    
    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return tiedostonPerusNimi + ".dat";
    } 
    
    
    /**
     * Palauttaa tiedoston nimen ilman tiedostopäätettä.
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }

    
    /**
     * Asettaa tiedoston perusnimen ilman tarkenninta
     * @param nimi tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String nimi) {
        tiedostonPerusNimi = nimi;
    }
      
    
    /**
     * Palauttaa juhlien lukumäärän
     * @return juhlien lukumäärä
     */
    public int getLkm() {
        return lkm;
    }
    
    
    /** 
     * Luokka juhlien iteroimiseksi. 
     */ 
    public class JuhlatIterator implements Iterator<Juhla> { 
        private int kohdalla = 0; 

        
        /** 
         * Onko olemassa vielä seuraavaa juhlaa 
         * @see java.util.Iterator#hasNext() 
         * @return true jos on viel� juhlia
         */ 
        @Override 
        public boolean hasNext() { 
            return kohdalla < getLkm(); 
        } 
    
    
        /** 
         * Annetaan seuraava juhla 
         * @return seuraava 
         * @throws NoSuchElementException jos seuraava alkiota ei en�� ole 
         * @see java.util.Iterator#next() 
         */ 
        @Override 
        public Juhla next() throws NoSuchElementException { 
            if ( !hasNext() ) throw new NoSuchElementException("Ei oo"); 
            return anna(kohdalla++); 
        } 
         
         
        /** 
         * Tuhoamista ei ole toteutettu 
         * @throws UnsupportedOperationException aina 
         * @see java.util.Iterator#remove() 
         */ 
        @Override 
        public void remove() throws UnsupportedOperationException { 
            throw new UnsupportedOperationException("Me ei poisteta"); 
        } 
    } 
         
         
    /** 
     * Palautetaan iteraattori juhlista. 
     * @return j�sen iteraattori 
     */ 
    @Override 
    public Iterator<Juhla> iterator() { 
        return new JuhlatIterator(); 
    } 
         
         
    /**  
     * Palauttaa "taulukossa" hakuehtoon vastaavien juhlienviitteet  
     * @param hakuehto hakuehto  
     * @param k etsittävän kentän indeksi   
     * @return tietorakenteen löytyneistä jäsenistä
     */  
    public Collection<Juhla> etsi(String hakuehto, int k) {  
        String ehto = "*"; 
                if ( hakuehto != null && hakuehto.length() > 0 ) ehto = hakuehto; 
                int hk = k; 
                 if ( hk < 0 ) hk = 1;
                 List<Juhla> loytyneet = new ArrayList<Juhla>(); 
                 for (Juhla juhla : this) { 
                     if (WildChars.onkoSamat(juhla.anna(hk), ehto)) loytyneet.add(juhla);   
                 } 
                 Collections.sort(loytyneet, new Juhla.Vertailija(hk));
                 return loytyneet; 
             }
    /** 
     * Poistaa juhlan jolla on valittu tunnusnumero  
     * @param id poistettavan jäsenen tunnusnumero 
     * @return 1 jos poistettiin, 0 jos ei löydy 
     * @example 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Juhlat juhlat = new Juhlat(); 
     * Juhla juhla1 = new Juhla(), juhla2 = new Juhla(), juhla3 = new Juhla(); 
     * juhla1.rekisteroi(); juhla2.rekisteroi(); juhla3.rekisteroi(); 
     * int id1 = juhla1.getTunnusNro(); 
     * juhlat.lisaa(juhla1); juhlat.lisaa(juhla2); juhlat.lisaa(juhla3); 
     * juhlat.poista(id1+1) === 1; 
     * juhlat.annaId(id1+1) === null; juhlat.getLkm() === 2; 
     * juhlat.poista(id1) === 1; juhlat.getLkm() === 1; 
     * juhlat.poista(id1+3) === 0; juhlat.getLkm() === 1; 
     * </pre> 
     *  
     */ 
    public int poista(int id) { 
        int ind = etsiId(id); 
        if (ind < 0) return 0; 
        lkm--; 
        for (int i = ind; i < lkm; i++) 
            alkiot[i] = alkiot[i + 1]; 
        alkiot[lkm] = null; 
        muutettu = true; 
        return 1; 
    } 
    /** 
     * Etsii juhlan id:n perusteella 
     * @param id tunnusnumero, jonka mukaan etsitään 
     * @return löytyneen jäsenen indeksi tai -1 jos ei löydy 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Juhlat juhlat = new Juhlat(); 
     * Juhla juhla1 = new Juhla(), juhla2 = new Juhla(), juhla3 = new Juhla(); 
     * juhla1.rekisteroi(); juhla2.rekisteroi(); juhla3.rekisteroi(); 
     * int id1 = juhla1.getTunnusNro(); 
     * juhlat.lisaa(juhla1); juhlat.lisaa(juhla2); juhlat.lisaa(juhla3); 
     * juhlat.etsiId(id1+1) === 1; 
     * juhlat.etsiId(id1+2) === 2; 
     * </pre> 
     */ 
    public int etsiId(int id) { 
        for (int i = 0; i < lkm; i++) 
            if (id == alkiot[i].getTunnusNro()) return i; 
        return -1; 
    } 




    /**
     * Testiohjelma juhlille
     * @param args ei käytössä
     */
    public static void main(String args[]){
        Juhlat juhlat = new Juhlat();

        Juhla juhla1 = new Juhla(), juhla2 = new Juhla();
        juhla1.rekisteroi();
        juhla1.testiVastaaJuhla();
        juhla2.rekisteroi();
        juhla2.testiVastaaJuhla();

        juhlat.lisaa(juhla1);
        juhlat.lisaa(juhla2);

        System.out.println("============= Juhlat testi =================");

        for (int i = 0; i < juhlat.getLkm(); i++) {
            Juhla juhla = juhlat.anna(i);
            System.out.println("Juhlan nro: " + i);
            juhla.tulosta(System.out);
        }
    }
}