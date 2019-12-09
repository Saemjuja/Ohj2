package fxJuhlarekisteri.Tietorakenne;
 
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import fxJuhlarekisteri.SailoException;

import java.util.*;

/**
 * Rekisterin ruokavaliot, joka osaavat mm. lisätä uuden valion
 * @author Sampo Kupiainen & Sami Järvinen
 * @version 12.6.2019
 */
public class Ruokavaliot implements Iterable<Ruokavalio> {

	private boolean muutettu = false;
    private String tiedostonPerusNimi = "";

    /**
     *  Taulukko ruokavalioista
     */
    private final List<Ruokavalio> alkiot = new ArrayList<Ruokavalio>();


    /**
     * Ruokavalioiden alustaminen
     */
    public Ruokavaliot() {
        // Ei tarvitse mitään
    }


    /**
     * Lisää uuden valion tietorakenteeseen.
     * @param ruo lisättävä ruokavalio
     */
    public void lisaa(Ruokavalio ruo) {
        alkiot.add(ruo);
        muutettu = true;
    }
    
    
    /**
     * @param ruokavalio lisättävän ruokavalion viite
     * @throws SailoException jos tietorakenne täynnä
     * @example
     * <pre name="test">
     * #THROWS SailoException,CloneNotSupportedException
     * #PACKAGEIMPORT
     * Ruokavaliot ruokavaliot = new Ruokavaliot();
     * Ruokavalio har1 = new Ruokavalio(), har2 = new Ruokavalio();
     * har1.rekisteroi(); har2.rekisteroi();
     * ruokavaliot.getLkm() === 0;
     * ruokavaliot.korvaaTaiLisaa(har1); ruokavaliot.getLkm() === 1;
     * ruokavaliot.korvaaTaiLisaa(har2); ruokavaliot.getLkm() === 2;
     * Ruokavalio har3 = har1.clone();
     * har3.aseta(2,"kkk");
     * Iterator<Ruokavalio> i2=ruokavaliot.iterator();
     * i2.next() === har1;
     * ruokavaliot.korvaaTaiLisaa(har3); ruokavaliot.getLkm() === 2;
     * i2=ruokavaliot.iterator();
     * Ruokavalio h = i2.next();
     * h === har3;
     * h == har3 === true;
     * h == har1 === false;
     * </pre>
     */
    public void korvaaTaiLisaa(Ruokavalio ruokavalio) throws SailoException {
        int id = ruokavalio.getTunnusNro();
        for (int i = 0; i < getLkm(); i++) {
            if (alkiot.get(i).getTunnusNro() == id) {
                alkiot.set(i, ruokavalio);
                muutettu = true;
                return;
            }
        }
        lisaa(ruokavalio);
    }
    
    
    /**
     * Lukee ruokavaliot tiedostosta.
     * @param tied tiedoston nimen alkuosa
     * @throws SailoException jos lukeminen ep�onnistuu
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     *  Ruokavaliot valiot = new Ruokavaliot();
     *  Ruokavalio kalja21 = new Ruokavalio(); kalja21.vastaaGluteeniton(2);
     *  Ruokavalio kalja11 = new Ruokavalio(); kalja11.vastaaGluteeniton(1);
     *  Ruokavalio kalja22 = new Ruokavalio(); kalja22.vastaaGluteeniton(2); 
     *  Ruokavalio kalja12 = new Ruokavalio(); kalja12.vastaaGluteeniton(1); 
     *  Ruokavalio kalja23 = new Ruokavalio(); kalja23.vastaaGluteeniton(2); 
     *  String tiedNimi = "testikela";
     *  File ftied = new File(tiedNimi+".dat");
     *  ftied.delete();
     *  valiot.lueTiedostosta(tiedNimi); #THROWS SailoException
     *  valiot.lisaa(kalja21);
     *  valiot.lisaa(kalja11);
     *  valiot.lisaa(kalja22);
     *  valiot.lisaa(kalja12);
     *  valiot.lisaa(kalja23);
     *  valiot.tallenna();
     *  valiot = new Ruokavaliot();
     *  valiot.lueTiedostosta(tiedNimi);
     *  Iterator<Ruokavalio> i = valiot.iterator();
     *  i.next().toString() === kalja21.toString();
     *  i.next().toString() === kalja11.toString();
     *  i.next().toString() === kalja22.toString();
     *  i.next().toString() === kalja12.toString();
     *  i.next().toString() === kalja23.toString();
     *  i.hasNext() === false;
     *  valiot.lisaa(kalja23);
     *  valiot.tallenna();
     *  ftied.delete() === true;
     *  File fbak = new File(tiedNimi+".bak");
     *  fbak.delete() === true;
     * </pre>
     */
    public void lueTiedostosta(String tied) throws SailoException {
        setTiedostonPerusNimi(tied);
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {

            String rivi;
            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Ruokavalio ruo = new Ruokavalio();
                ruo.parse(rivi);
                lisaa(ruo);
            }
            muutettu = false;

        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
    }
    
    
    /**
     * Luetaan aikaisemmin annetun nimisest� tiedostosta
     * @throws SailoException jos tulee poikkeus
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }


    /**
     * Tallentaa ruokavaliot tiedostoon.
     * @throws SailoException jos talletus ep�onnistuu
     */
    public void tallenna() throws SailoException {
        if ( !muutettu ) return;

        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete();
        ftied.renameTo(fbak);

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            for (Ruokavalio ruo : this) {
                fo.println(ruo.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }

        muutettu = false;
    }
    

    /**
     * Palauttaa rekisterin ruokavalioiden lukum��r�n
     * @return lukumaarat
     */
    public int getLkm() {
        return alkiot.size();
    }
    
    
    /**
     * Asettaa tiedoston perusnimen ilman tarkenninta
     * @param tied tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String tied) {
        tiedostonPerusNimi = tied;
    }
    
    
    /**
     * Palauttaa tiedoston nimen, jota k�ytet��n tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }
    
    
    /**
     * Palauttaa tiedoston nimen, jota k�ytet��n tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return tiedostonPerusNimi + ".dat";
    }
    
    
    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }


    /**
     * Iteraattori kaikkien ruokavalioiden läpikäymiseen
     * @return ruokavalioiteraattori
     * 
     * @example
     * <pre name="test">
     * #PACKAGEIMPORT
     * #import java.util.*;
     * 
     *  Ruokavaliot ruokavaliot = new Ruokavaliot();
     *  Ruokavalio kalja21 = new Ruokavalio(2); ruokavaliot.lisaa(kalja21);
     *  Ruokavalio kalja11 = new Ruokavalio(2); ruokavaliot.lisaa(kalja11);
     * 
     *  Iterator<Ruokavalio> i2=ruokavaliot.iterator();
     *  i2.next() === kalja21;
     *  i2.next() === kalja11;
     *  
     *  int n = 0;
     *  int jnrot[] = {2,1};
     *  
     *  for ( Ruokavalio ruo:ruokavaliot ) { 
     *    ruo.getJuhlaNro() === jnrot[n]; n++;  
     *  }
     *  
     *  n === 5;
     *  
     * </pre>
     */
    @Override
    public Iterator<Ruokavalio> iterator() {
        return alkiot.iterator();
    }


    /**
     * Haetaan kaikki juhlan ruokavaliot
     * @param tunnusnro juhlan tunnusnumero jolle ruokavalioita haetaan
     * @return tietorakenne jossa viiteet löydetteyihin harrastuksiin
     * @example
     * <pre name="test">
     * #import java.util.*;
     * 
     *  Ruokavaliot ruokavaliot = new Ruokavaliot();
     *  Ruokavalio kalja21 = new Ruokavalio(2); ruokavaliot.lisaa(kalja21);
     *  Ruokavalio kalja23 = new Ruokavalio(2); ruokavaliot.lisaa(kalja23);
     *  
     *  List<Ruokavalio> loytyneet;
     *  loytyneet = ruokavaliot.annaRuokavaliot(3);
     *  loytyneet.size() === 0; 
     *  loytyneet = ruokavaliot.annaRuokavaliot(1);
     *  loytyneet.size() === 2; 
     * </pre> 
     */
    public List<Ruokavalio> annaRuokavaliot(int tunnusnro) {
        List<Ruokavalio> loydetyt = new ArrayList<Ruokavalio>();
        for (Ruokavalio ruo : alkiot)
            if (ruo.getJuhlanNro() == tunnusnro) loydetyt.add(ruo);
        return loydetyt;
    }
    /**
     * Poistaa valitun ruokavalion
     * @param ruokavalio poistettava
     * @return tosi jos löytyi poistettava tietue 
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     *  Ruokavaliot valiot = new Ruokavaliot();
     *  Ruokavalio kalja21 = new Ruokavalio(); kalja21.vastaaGluteeniton(2);
     *  Ruokavalio kalja11 = new Ruokavalio(); kalja11.vastaaGluteeniton(1);
     *  Ruokavalio kalja22 = new Ruokavalio(); kalja22.vastaaGluteeniton(2); 
     *  Ruokavalio kalja12 = new Ruokavalio(); kalja12.vastaaGluteeniton(1); 
     *  Ruokavalio kalja23 = new Ruokavalio(); kalja23.vastaaGluteeniton(2); 
     *  valiot.lisaa(kalja21);
     *  valiot.lisaa(kalja11);
     *  valiot.lisaa(kalja22);
     *  valiot.lisaa(kalja12);
     *  valiot.poista(kalja23) === false ; valiot.getLkm() === 4;
     *  valiot.poista(kalja11) === true;   valiot.getLkm() === 3;
     *  List<Ruokavalio> h = valiot.annaRuokavaliot(1);
     *  h.size() === 1; 
     *  h.get(0) === kalja12;
     * </pre>
     */
    public boolean poista(Ruokavalio ruokavalio) {
        boolean ret = alkiot.remove(ruokavalio);
        if (ret) muutettu = true;
        return ret;
    }

    
    /**
     * Poistaa kaikki tietyn tietyn juhlan ruokavaliot
     * @param tunnusNro viite siihen, mihin liittyvät tietueet poistetaan
     * @return montako poistettiin 
     * @example
     * <pre name="test">
     *  Ruokavaliot valiot = new Ruokavaliot();
     *  Ruokavalio kalja21 = new Ruokavalio(); kalja21.vastaaGluteeniton(2);
     *  Ruokavalio kalja11 = new Ruokavalio(); kalja11.vastaaGluteeniton(1);
     *  Ruokavalio kalja22 = new Ruokavalio(); kalja22.vastaaGluteeniton(2); 
     *  Ruokavalio kalja12 = new Ruokavalio(); kalja12.vastaaGluteeniton(1); 
     *  Ruokavalio kalja23 = new Ruokavalio(); kalja23.vastaaGluteeniton(2); 
     *  valiot.lisaa(kalja21);
     *  valiot.lisaa(kalja11);
     *  valiot.lisaa(kalja22);
     *  valiot.lisaa(kalja12);
     *  valiot.lisaa(kalja23);
     *  valiot.poistaJuhlanRuokavaliot(2) === 3;  valiot.getLkm() === 2;
     *  valiot.poistaJuhlanRuokavaliot(3) === 0;  valiot.getLkm() === 2;
     *  List<Ruokavalio> h = valiot.annaRuokavaliot(2);
     *  h.size() === 0; 
     *  h = valiot.annaRuokavaliot(1);
     *  h.get(0) === kalja11;
     *  h.get(1) === kalja12;
     * </pre>
     */
    public int poistaJuhlanRuokavaliot(int tunnusNro) {
        int n = 0;
        for (Iterator<Ruokavalio> it = alkiot.iterator(); it.hasNext();) {
            Ruokavalio har = it.next();
            if ( har.getJuhlanNro() == tunnusNro ) {
                it.remove();
                n++;
            }
        }
        if (n > 0) muutettu = true;
        return n;
    }



    /**
     * Testiohjelma ruokavalioille
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Ruokavaliot valiot = new Ruokavaliot();
        Ruokavalio kaljaeeniton1 = new Ruokavalio(1);
        kaljaeeniton1.vastaaGluteeniton(2);
        Ruokavalio kaljaeeniton2 = new Ruokavalio(2);
        kaljaeeniton2.vastaaGluteeniton(1);
            
        valiot.lisaa(kaljaeeniton1);
        valiot.lisaa(kaljaeeniton2);

        System.out.println("============= Ruokavalioiden testi =================");

        List<Ruokavalio> ruokavaliot2 = valiot.annaRuokavaliot(2);

        for (Ruokavalio ruo : ruokavaliot2) {
            System.out.print(ruo.getJuhlanNro() + " ");
            ruo.tulosta(System.out);
        }
    }  
}