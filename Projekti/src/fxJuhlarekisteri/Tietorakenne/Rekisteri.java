package fxJuhlarekisteri.Tietorakenne;

import java.util.List;
import java.io.File;
import fxJuhlarekisteri.SailoException;
import java.util.Collection; 

/**
 * @author Sampo Kupiainen & Sami Järvinen
 * @version 11.6.2019
 * Testien alustus
 * @example
 * <pre name="testJAVA">
 * #import fxJuhlarekisteri.SailoException;
 *  private Rekisteri rekisteri;
 *  private Juhla juhla1;
 *  private Juhla juhla2;
 *  private int jid1;
 *  private int jid2;
 *  private Ruokavalio kalja21;
 *  private Ruokavalio kalja11;
 *  private Ruokavalio kalja22; 
 *  private Ruokavalio kalja12; 
 *  private Ruokavalio kalja23;
 *  
 *  @SuppressWarnings("javadoc")
 *  public void alustaRekisteri() {
 *    rekisteri = new Rekisteri();
 *    juhla1 = new Juhla(); juhla1.testiVastaaJuhla(); juhla1.rekisteroi();
 *    juhla2 = new Juhla(); juhla2.testiVastaaJuhla(); juhla2.rekisteroi();
 *    jid1 = juhla1.getTunnusNro();
 *    jid2 = juhla2.getTunnusNro();
 *    kalja21 = new Ruokavalio(jid2); kalja21.vastaaGluteeniton(jid2);
 *    kalja11 = new Ruokavalio(jid1); kalja11.vastaaGluteeniton(jid1);
 *    kalja22 = new Ruokavalio(jid2); kalja22.vastaaGluteeniton(jid2); 
 *    kalja12 = new Ruokavalio(jid1); kalja12.vastaaGluteeniton(jid1); 
 *    kalja23 = new Ruokavalio(jid2); kalja23.vastaaGluteeniton(jid2);
 *    try {
 *    rekisteri.lisaa(juhla1);
 *    rekisteri.lisaa(juhla2);
 *    rekisteri.lisaa(kalja21);
 *    rekisteri.lisaa(kalja11);
 *    rekisteri.lisaa(kalja22);
 *    rekisteri.lisaa(kalja12);
 *    rekisteri.lisaa(kalja23);
 *    } catch ( Exception e) {
 *       System.err.println(e.getMessage());
 *    }
 *  }
 * </pre>
 */
public class Rekisteri {
    private static Juhlat juhlat = new Juhlat();
    private static Ruokavaliot ruokavaliot = new Ruokavaliot();


    /**
     * Poistaa juhlista ja ruokavalioista ne joilla on nro. Kesken.
     * @param nro numero
     * @return montako jäsentä poistettiin
     */
    public int poista(@SuppressWarnings("unused") int nro) {
      return 0;
    }


    /**
     * Lisää rekisterin uuden juhlan
     * @param juhla lisättävä juhla
     * @throws SailoException jos lisäystä ei voida tehdä
     * @example
     * <pre name="test">
     * #THROWS SailoException  
     *  alustaRekisteri();
     *  rekisteri.etsi("*",0).size() === 10;
     *  rekisteri.lisaa(juhla1);
     *  rekisteri.etsi("*",0).size() === 11;
     */
    public void lisaa(Juhla juhla) throws SailoException {
        juhlat.lisaa(juhla);
    }
    

    /**
     * @param juhla lisättävän juhlan viite
     * @throws SailoException jos tietorakenne täynnä
     * @example
     * <pre name="test">
     * #THROWS SailoException  
     *  alustaRekisteri();
     *  rekisteri.etsi("*",0).size() === 13;
     *  rekisteri.korvaaTaiLisaa(juhla1);
     *  rekisteri.etsi("*",0).size() === 13;
     * </pre>
     */
    public void korvaaTaiLisaa(Juhla juhla) throws SailoException { 
        juhlat.korvaaTaiLisaa(juhla); 
    }
    
    
    /**
     * Ottaa ruokavalion omistukseen ja tarkistaa, onko samalla numerolla jo ruokavalio. Jos ei, lisää uuden.
     * @param ruokavalio lisättävän ruokavalion viite
     * @throws SailoException jos tietorakenne täynnä
     */ 
    public void korvaaTaiLisaa(Ruokavalio ruokavalio) throws SailoException { 
        ruokavaliot.korvaaTaiLisaa(ruokavalio); 
    } 
    
    
    /**
     * Lisätään uusi ruokavalio rekisteriin
     * @param har lisättävä ruokavalio
     * @throws SailoException jos tulee ongelmia
     */
    public void lisaa(Ruokavalio har) throws SailoException {
        ruokavaliot.lisaa(har);
    }


    /**  
     * Palauttaa "taulukossa" hakuehtoon vastaavien juhlien viitteet  
     * @param hakuehto hakuehto   
     * @param k etsittävän kentän indeksi   
     * @return juhlien viitteet
     * @throws SailoException Jos jotakin menee väärin 
     * @example 
     * <pre name="test">
     *   #THROWS CloneNotSupportedException, SailoException
     *   alustaRekisteri();
     *   // TODO: tee testit kun etsi on korjattu
     * </pre>
     */  
    public Collection<Juhla> etsi(String hakuehto, int k) throws SailoException {  
        return juhlat.etsi(hakuehto, k);  
    }  
    
    
    /**
     * Haetaan kaikki juhlan ruokavaliot
     * @param juhla asd
     * @return valiot
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.util.*;
     * 
     *  alustaRekisteri();
     *  Juhla juhla3 = new Juhla();
     *  juhla3.rekisteroi();
     *  rekisteri.lisaa(juhla3);
     *  
     *  List<Ruokavalio> loytyneet;
     *  loytyneet = rekisteri.annaRuokavaliot(juhla3);
     *  loytyneet.size() === 0; 
     *  loytyneet = rekisteri.annaRuokavaliot(juhla1);
     *  loytyneet.size() === 2; 
     *  loytyneet.get(0) == kalja11 === true;
     *  loytyneet.get(1) == kalja12 === true;
     *  loytyneet = rekisteri.annaRuokavaliot(juhla2);
     *  loytyneet.size() === 3; 
     *  loytyneet.get(0) == kalja21 === true;
     * </pre> 
     */
    public List<Ruokavalio> annaRuokavaliot(Juhla juhla) {
        return ruokavaliot.annaRuokavaliot(juhla.getTunnusNro());
    }
    
    
    /**
     * Asettaa tiedostojen perusnimet
     * @param nimi uusi nimi
     */
    public void setTiedosto(String nimi) {
        File dir = new File(nimi);
        dir.mkdirs();
        String hakemistonNimi = "";
        if ( !nimi.isEmpty() ) hakemistonNimi = nimi +"/";
        juhlat.setTiedostonPerusNimi(hakemistonNimi + "juhlat");
        ruokavaliot.setTiedostonPerusNimi(hakemistonNimi + "ruokavaliot");
    }
    
    
    /**
     * Lukee rekisterin tiedot tiedostosta
     * @param nimi jota käytetään lukemisessa
     * @throws SailoException jos lukeminen epäonnistuu
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.*;
     * #import java.util.*;
     *   
     *  String hakemisto = "testioletus";
     *  File dir = new File(hakemisto);
     *  File ftied  = new File(hakemisto+"/juhlat.dat");
     *  File fhtied = new File(hakemisto+"/ruokavaliot.dat");
     *  dir.mkdir();  
     *  ftied.delete();
     *  fhtied.delete();
     *  rekisteri = new Rekisteri(); // tiedostoja ei ole, tulee poikkeus 
     *  rekisteri.lueTiedostosta(hakemisto); #THROWS SailoException
     *  alustaRekisteri();
     *  rekisteri.setTiedosto(hakemisto); // nimi annettava koska uusi poisti sen
     *  rekisteri.tallenna(); 
     *  rekisteri = new Rekisteri();
     *  rekisteri.lueTiedostosta(hakemisto);
     *  Collection<Juhla> kaikki = rekisteri.etsi("",-1); 
     *  Iterator<Juhla> it = kaikki.iterator();
     *  it.next() === juhla1;
     *  it.next() === juhla2;
     *  it.hasNext() === false;
     *  List<Ruokavalio> loytyneet = rekisteri.annaRuokavaliot(juhla1);
     *  Iterator<Ruokavalio> ih = loytyneet.iterator();
     *  ih.next() === kalja11;
     *  ih.next() === kalja12;
     *  ih.hasNext() === false;
     *  loytyneet = rekisteri.annaRuokavaliot(juhla2);
     *  ih = loytyneet.iterator();
     *  ih.next() === kalja21;
     *  ih.next() === kalja22;
     *  ih.next() === kalja23;
     *  ih.hasNext() === false;
     *  rekisteri.lisaa(juhla2);
     *  rekisteri.lisaa(kalja23);
     *  rekisteri.tallenna(); // tekee molemmista .bak
     *  ftied.delete()  === true;
     *  fhtied.delete() === true;
     *  File fbak = new File(hakemisto+"/juhlat.bak");
     *  File fhbak = new File(hakemisto+"/ruokavaliot.bak");
     *  fbak.delete() === true;
     *  fhbak.delete() === true;
     *  dir.delete() === true;
     * </pre>
     */
    public void lueTiedostosta(String nimi) throws SailoException {
        juhlat = new Juhlat();
        ruokavaliot = new Ruokavaliot();

        setTiedosto(nimi);
        juhlat.lueTiedostosta();
        ruokavaliot.lueTiedostosta();
    }
    
    
    /**
     * Tallenttaa rekisterin tiedot tiedostoon.  
     * Vaikka juhlien tallettamien ep�onistuisi, niin yritet��n silti tallettaa
     * ruokavalioita ennen poikkeuksen heitt�mist�.
     * @throws SailoException jos tallettamisessa ongelmia
     */
    public void tallenna() throws SailoException {
        String virhe = "";
        try {
            juhlat.tallenna();
        } catch ( SailoException ex ) {
            virhe = ex.getMessage();
        }

        try {
            ruokavaliot.tallenna();
        } catch ( SailoException ex ) {
            virhe += ex.getMessage();
        }
        if ( !"".equals(virhe) ) throw new SailoException(virhe);
    }
    /**
     * Poistaa jäsenistöstä ja harrasteista jäsenen tiedot 
     * @param juhla asd
     * @return montako jäsentä poistettiin
     * @example
     * <pre name="test">
     * #THROWS Exception
     *   alustaKerho();
     *   kerho.etsi("*",0).size() === 2;
     *   kerho.annaHarrastukset(aku1).size() === 2;
     *   kerho.poista(aku1) === 1;
     *   kerho.etsi("*",0).size() === 1;
     *   kerho.annaHarrastukset(aku1).size() === 0;
     *   kerho.annaHarrastukset(aku2).size() === 3;
     * </pre>
     */
    public int poista(Juhla juhla) {
        if ( juhla == null ) return 0;
        int ret = juhlat.poista(juhla.getTunnusNro()); 
       ruokavaliot.poistaJuhlanRuokavaliot(juhla.getTunnusNro()); 
        return ret; 
    }


    /** 
     * Poistaa tämän harrastuksen 
     * @param ruokavalio poistettava ruokavalio 
     * @example
     * <pre name="test">
     * #THROWS Exception
     *   alustaKerho();
     *   kerho.annaHarrastukset(aku1).size() === 2;
     *   kerho.poistaHarrastus(pitsi11);
     *   kerho.annaHarrastukset(aku1).size() === 1;
     */ 
    public void poistaRuokavalio(Ruokavalio ruokavalio) { 
        ruokavaliot.poista(ruokavalio); 
    } 


    /**
     * Testiohjelma juhlista
     * @param args ei käytössä
     */
    @SuppressWarnings("hiding")
    public static void main(String args[]) {
        
        Rekisteri rekisteri = new Rekisteri();

        try {

            Juhla juhla1 = new Juhla(), juhla2 = new Juhla();
            juhla1.rekisteroi();
            juhla1.testiVastaaJuhla();
            juhla2.rekisteroi();
            juhla2.testiVastaaJuhla();
            
            rekisteri.lisaa(juhla1);
            rekisteri.lisaa(juhla2);
            int id1 = juhla1.getTunnusNro();
            int id2 = juhla2.getTunnusNro();
            Ruokavalio glut21 = new Ruokavalio(id1); glut21.vastaaGluteeniton(id2); rekisteri.lisaa(glut21);
            Ruokavalio glut23 = new Ruokavalio(id2); glut23.vastaaGluteeniton(id2); rekisteri.lisaa(glut23);
            System.out.println("============= Rekisterin testi =================");

            Collection<Juhla> juhlat = rekisteri.etsi("", -1);
            int i = 0;
            for (Juhla juhla: juhlat) {
                System.out.println("Juhla paikassa: " + i);
                juhla.tulosta(System.out);
                List<Ruokavalio> loytyneet = rekisteri.annaRuokavaliot(juhla);
                for (Ruokavalio ruokavalio : loytyneet)
                    ruokavalio.tulosta(System.out);
                i++;
            }

        } catch ( SailoException ex ) {
            System.out.println(ex.getMessage());
        }
    }
}