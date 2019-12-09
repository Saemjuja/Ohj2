package fxJuhlarekisteri.Tietorakenne;

import fi.jyu.mit.ohj2.Mjonot;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import fxJuhlarekisteri.Kanta.Tietue;


/**
 * @author Sampo Kupiainen & Sami Järvinen
 * @version 12.6.2019
 */
public class Ruokavalio  implements Cloneable, Tietue {
    private String valio;
    private int tunnusNro   = 1;
    private int juhlanNro   = 1;
    private static int seuraavaNro = 1;
    
    
    /**
     * Alustetaan ruokavalio.  Toistaiseksi ei tarvitse tehdä mitään
     */
    public Ruokavalio() {
        // Vielä ei tarvita mitään
    }

    
    /**
     * Alustetaan  tietyn juhlan ruokavalio.  
     * @param juhlanNro juhlan id
     */
    public Ruokavalio(int juhlanNro) {
        this.juhlanNro = juhlanNro;
    }


    /**
     * @return Kenttien lukumäärä
     */
    @Override
    public int getKenttia() {
        return 3;
    }
    
    
    /**
     * @return Palauttaa ensimmäisen kentän, johon käyttäjä voi syöttää, indeksin
     */
    @Override
    public int ekaKentta() {
        return 2;
    }
    
    
    /**
     * @param k kenttä, jonka kysymys halutaan
     * @return valitun kentän kysymysteksti
     */
    @Override
    public String getKysymys(int k) {
        switch (k) {
            case 0:
                return "ID";
            case 1:
                return "Juhlan ID";
            case 2:
                return "Ruokavalio";
            default:
                return "???";
        }
    }
    
    
    /**
     * @param k kenttä, jonka sisältö halutaan
     * @return valitun kentän sisältö
     *  <pre name="test">
     *   Ruokavalio har = new Ruokavalio();
     *   har.parse("   2   |  10  |   Kala  ");
     *   har.anna(0) === "2";   
     *   har.anna(1) === "10";   
     *   har.anna(2) === "Kala";   
     * </pre>
     */
    @Override
    public String anna(int k) {
        switch (k) {
        case 0:
            return "" + tunnusNro;
        case 1:
            return "" + juhlanNro;
        case 2:
            return "" + valio;
        default:
            return "???";
        }
    }
    
    
    /**
     * Asetetaan valitun kentän sisältö.  Mikäli asettaminen onnistuu,
     * palautetaan null, muutoin virheteksti.
     * @param k minkä kentän sisältö asetetaan
     * @return null jos ok, muuten virheteksti
     * @example
     * <pre name="test">
     *   Ruokavalio har = new Ruokavalio();
     *   har.aseta(2,"kissa") === "kissa";
     *   har.aseta(2,"rahka")  === "rahka";
     *   har.aseta(2,"") === "";
     * </pre>
     */
    @Override
    public String aseta(int k, String jono) {
        
        String tjono = jono.trim();
        StringBuffer sb = new StringBuffer(tjono);
        switch (k) {
            case 0:
                setTunnusNro(Mjonot.erota(sb, '$', getTunnusNro()));
                return null;
            case 1:
                juhlanNro = Mjonot.erota(sb, '$', juhlanNro);
                return null;
            case 2:
                String kelvotonValio = "Ruokavalio on tyhjä tai sisältää numeroita";
                String[] sallitut = {"gluteeniton", "viinaa", "alkoholiton", "vegaaninen","laktoositon","karppaus"};
                if (!(Arrays.asList(sallitut).contains(tjono.toLowerCase()))) return kelvotonValio;
                valio = tjono;
                
           
                
                return null;
              
            default:
                return "Väärä kentän indeksi";
        }
    }
    
    
    /**
     * Tehdään identtinen klooni juhlasta
     * @return Object kloonattu juhla
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException 
     *   Ruokavalio har = new Ruokavalio();
     *   har.parse("   2   |  10  |   Kala  ");
     *   Ruokavalio kopio = har.clone();
     *   kopio.toString() === har.toString();
     *   har.parse("   1   |  11  |   Rahka  ");
     *   kopio.toString().equals(har.toString()) === false;
     * </pre>
     */
    @Override
    public Ruokavalio clone() throws CloneNotSupportedException { 
        return (Ruokavalio)super.clone();
    }
    
    
     /**
     * Apumetodi, jolla saadaan täytettyä testiarvot ruokavaliolle
     * @param nro Juhlan viitenumero
     */
    public void vastaaGluteeniton(int nro) {
        juhlanNro = nro;
        valio = "Gluteeniton";  
    }
    
    
    /**
     * Tulostetaan harrastuksen tiedot
     * @param out tietovirta
     */
    public void tulosta(PrintStream out) {
        out.println(valio);
    }
    
    
    /**
     * tulostetaan juhlan tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    

    /**
     * Antaa ruokavaliolle seuraavan rekisterinumeron.
     * @return ruokavalion uusi tunnus_nro
     *  @example
     * <pre name="test">
     *   Ruokavalio kalja1 = new Ruokavalio();
     *   kalja1.getTunnusNro() === 0;
     *   kalja1.rekisteroi();
     *   Ruokavalio kalja2 = new Ruokavalio();
     *   kalja2.rekisteroi();
     *   int n1 = kalja1.getTunnusNro();
     *   int n2 = kalja2.getTunnusNro();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return tunnusNro;
    }


    /**
     * Palautetaan mille juhlalle ruokavalio kuuluu
     * @return juhlan id
     */
    public int getJuhlanNro() {
        return juhlanNro;
    }
    
    
    /**
     * Palautetaan ruokavalion oma id
     * @return ruokavalion id
     */
    public int getTunnusNro() {
        return tunnusNro;
    }
    
    
    /**
     * Asettaa tunnusnumeron ja samalla varmistaa ett�
     * seuraava numero on aina suurempi kuin t�h�n menness� suurin.
     * @param nr asetettava tunnusnumero
     */
    private void setTunnusNro(int nr) {
        tunnusNro = nr;
        if ( tunnusNro >= seuraavaNro ) seuraavaNro = tunnusNro + 1;
    }
    
    
    /**
     * Palauttaa ruokavalion tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return ruokavalio tolppaeroteltuna merkkijonona 
     * @example
     * <pre name="test">
     *   Ruokavalio ruokavalio = new Ruokavalio();
     *   Ruokavalio.parse("   2   |  10  |   Kalastus  ");
     *   Ruokavalio.toString()    === "2|10|Kalastus";
     * </pre>
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("");
        String erotin = "";
        for (int k = 0; k < getKenttia(); k++) {
            sb.append(erotin);
            sb.append(anna(k));
            erotin = "|";
        }
        return sb.toString();
     }

    
    
    /**
     * Selvittää ruokavalion tiedot | erotellusta merkkijonosta.
     * Pitää huolen että seuraavaNro on suurempi kuin tuleva tunnusnro.
     * @param rivi josta ruokavalion tiedot otetaan
     * @example
     * <pre name="test">
     *   Ruokavalio ruokavalio = new Ruokavalio();
     *   ruokavalio.parse("   2   |  10  |   Kala  ");
     *   ruokavalio.getJuhlanNro() === 10;
     *   ruokavalio.toString()    === "2|10|Kala";
     *   
     *   ruokavalio.rekisteroi();
     *   int n = ruokavalio.getTunnusNro();
     *   ruokavalio.parse(""+(n+20));
     *   ruokavalio.rekisteroi();
     *   ruokavalio.getTunnusNro() === n+20+1;
     *   ruokavalio.toString()     === "" + (n+20+1) + "|10|";
     * </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        for (int k = 0; k < getKenttia(); k++)
            aseta(k, Mjonot.erota(sb, '|'));
    }

    
    @Override
    public boolean equals(Object obj) {
        if ( obj == null ) return false;
        return this.toString().equals(obj.toString());
    }

 
   @Override 
   public int hashCode() { 
       return tunnusNro; 
   }
   
   
   /**
    * Testiohjelma rukavaliolle
    * @param args ei käytössä
    */
   public static void main(String[] args) {
       Ruokavalio ruoV = new Ruokavalio();
       ruoV.vastaaGluteeniton(2);
       ruoV.tulosta(System.out);
   }
}
