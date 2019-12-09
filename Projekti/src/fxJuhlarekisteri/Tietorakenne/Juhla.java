package fxJuhlarekisteri.Tietorakenne;
import java.io.*;
import java.util.Comparator;

import fi.jyu.mit.ohj2.Mjonot;
import fxJuhlarekisteri.Kanta.Tietue;

/**
 * @author Sampo Kupiainen & Sami Järvinen
 * @version 12.6.2019
 */
public class Juhla implements Cloneable, Tietue {
    private int        tunnusNro      = 1;
    private static int seuraavaNro    = 1;
    private String     paivamaara     = "";
    private String     katuosoite     = "";
    private String     postinumero    = "";
    private String     postiosoite    = "";
    private String     juhlanTyyppi   = "";

    /** 
     * Juhlien vertailija 
     */ 
    public static class Vertailija implements Comparator<Juhla> { 
        private int k;  
         
        @SuppressWarnings("javadoc") 
        public Vertailija(int k) { 
            this.k = k; 
        } 
         
        @Override 
        public int compare(Juhla juhla1, Juhla juhla2) { 
            return juhla1.getAvain(k).compareToIgnoreCase(juhla2.getAvain(k)); 
        } 
    } 
    
    
    /** 
     * Antaa k:n kentän sisällön merkkijonona 
     * @param k monenenko kentän sisältö palautetaan 
     * @return kentän sisältö merkkijonona 
     */ 
    public String getAvain(int k) { 
        switch ( k ) { 
        case 0: return "" + tunnusNro; 
        case 1: return "" + paivamaara; 
        case 2: return "" + katuosoite; 
        case 3: return "" + postinumero; 
        case 4: return "" + postiosoite; 
        case 5: return "" + juhlanTyyppi; 

        default: return "Äääliö"; 
        } 
    } 
    
    
    /**
     * @return kenttien määrä
     */
    @Override
    public int getKenttia() {
        return 6;
    }
    
    
    /**
     * @return ekan kentän indeksi
     */
    @Override
    public int ekaKentta() {
        return 1;
    }
    
    
    /**
      * Palauttaa juhlan päivämäärän
      * @return Juhlan päivämäärä
      * @example
      */
    public String getPaivamaara() {
        return paivamaara;
    }


    /**
     * Antaa k:n kentän sisällön merkkijonona
     * @param k monenenko kentän sisältö palautetaan
     * @return kentän sisältö merkkijonona
     */
    @Override
    public String anna(int k) {
        switch ( k ) {
        case 0: return "" + tunnusNro;
        case 1: return "" + paivamaara;
        case 2: return "" + katuosoite;
        case 3: return "" + postinumero;
        case 4: return "" + postiosoite;
        case 5: return "" + juhlanTyyppi;
    
        default: return "Huutia";
        }
    }


    /**
     * Asettaa k:n kentän arvoksi parametrina tuodun merkkijonon arvon
     * @param k kuinka monennen kentän arvo asetetaan
     * @param jono jonoa joka asetetaan kentän arvoksi
     * @return null jos asettaminen onnistuu, muuten vastaava virheilmoitus.
     */
    @Override
    public String aseta(int k, String jono) {
        String tjono = jono.trim();
        StringBuffer sb = new StringBuffer(tjono);
        switch ( k ) {
        case 0:
            setTunnusNro(Mjonot.erota(sb, '§', getTunnusNro()));
            return null;
        case 1:
            paivamaara.toString();
            if ( !(tjono.matches("......") || paivamaara.matches("[0-9]+") && paivamaara.contains("."))) return "Päivämäärän syntaksi on väärä";
            paivamaara = tjono;
            return null;
        case 2:
             katuosoite = tjono;
            return null;
        case 3:
            postinumero = tjono;
            int pituus = tjono.length();
           if ( pituus < 5 ) return "Postinumero liian lyhyt";
           if ( pituus > 5 ) return "Postinumero liian pitkä";
            postinumero.toString();
            if (postinumero.matches("[0-9]+" )== !true) return "Sisältää kirjaimia";
            return null;
        case 4:
            postiosoite = tjono;
            return null;
        case 5:
            juhlanTyyppi = tjono;
            return null;
        default:
            return "Huutia";
        }
    }


    /**
     * Palauttaa k:tta juhlan kenttää vastaavan kysymyksen
     * @param k kuinka monennen kentän kysymys palautetaan (0-alkuinen)
     * @return k:netta kenttää vastaava kysymys
     */
    @Override
    public String getKysymys(int k) {
        switch ( k ) {
        case 0: return "Tunnusnumero";
        case 1: return "Päivämäärä";
        case 2: return "Katuosoite";
        case 3: return "Postinumero";
        case 4: return "Postiosoite";
        case 5: return "Juhlan tyyppi";
        
        default: return "Huutia";
        }
    }


    /**
     * Apumetodi, jolla saadaan täyetettyä testiarvot juhlalle
     * @param apupostinro apupostinro
     */
    public void testiVastaaJuhla(String apupostinro) {
        paivamaara = "20.05.";
        juhlanTyyppi = "Syntymäpäivät";
        katuosoite = "Ahdinkatu 18 A 4";
        postinumero = apupostinro;
        postiosoite = "Palokka";
    }


    /**
     * Apumetodi, jolla saadaan täytettyä testiarvot juhlalle.
     * postinumero arvotaan, jotta kahdella juhlalla� ei olisi
     * samoja postinumeroita.
     */
    public void testiVastaaJuhla() {
        String apupostinro = testiArvoPostinro();
        testiVastaaJuhla(apupostinro);
    }


    /**
     * Tulostetaan juhlan tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(String.format("%02d", tunnusNro, 3));
        out.println(paivamaara);
        out.println(katuosoite + " " + postinumero + " " + postiosoite);
        out.println(juhlanTyyppi);      
    }


    /**
     * Tulostetaan juhlan tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
         tulosta(new PrintStream(os));
    }


    /**
     * Antaa juhlalle seuraavan rekisterinumeron.
     * @return juhlan uusi tunnusNro
     * @example
     * <pre name="test">
     *   Juhla juhla1 = new Juhla();
     *   juhla1.getTunnusNro() === 0;
     *   juhla1.rekisteroi();
     *   Juhla juhla2 = new Juhla();
     *   juhla2.rekisteroi();
     *   int n1 = juhla1.getTunnusNro();
     *   int n2 = juhla2.getTunnusNro();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return tunnusNro;
    }


    /**
     * Palauttaa julan tunnusnumeron.
     * @return juhlan tunnusnumero
     */
    public int getTunnusNro() {
        return tunnusNro;
    }


    /**
     * Asettaa tunnusnumeron ja samalla varmistaa että
     * seuraava numero on aina suurempi kuin tähän mennessä suurin.
     * @param nr asetettava tunnusnumero
     */
    private void setTunnusNro(int nr) {
        tunnusNro = nr;
        if (tunnusNro >= seuraavaNro) seuraavaNro = tunnusNro + 1;
    }

    
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
     * @param rivi juhlan tieto
     */
    public void  parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        for (int k = 0; k < getKenttia(); k++)
            aseta(k, Mjonot.erota(sb, '|'));
    }


    /**
      * Tehdään identtinen klooni juhlasta
      * @return Object kloonattu juhla
      * @example
      * <pre name="test">
      * #THROWS CloneNotSupportedException 
      *   Juhla juhla = new Juhla();
      *   juhla.parse("20.5.  |  Hannikaisenkatu 11   | Jkl");
      *   Juhla kopio = juhla.clone();
      *   kopio.toString() === juhla.toString();
      *   juhla.parse("   21.6.  |  Viinala 22   | Hki");
      *   kopio.toString().equals(juhla.toString()) === false;
      * </pre>
      */
     @Override
     public Juhla clone() throws CloneNotSupportedException {
         Juhla uusi;
         uusi = (Juhla) super.clone();
         return uusi;
     }


    /**
     * Tutkii onko juhlan tiedot samat kuin parametrina tuodun juhlan tiedot
     * @param juhla johon verrataan
     * @return true jos kaikki tiedot samat, false muuten
     */
    public boolean equals(Juhla juhla) {
        if ( juhla == null ) return false;
        for (int k = 0; k < getKenttia(); k++)
            if ( !anna(k).equals(juhla.anna(k)) ) return false;
        return true;
    }


    /**
     *  Alustetaan juhlan merkkijono-attribuutit tyhjiksi jonoiksi ja tunnusnro = 0
     */
    public Juhla() {
        //Toistaiseksi ei tarvita
    }
    
    

    /**
     * Arvotaan satunnainen postinumero   
     * @return satunnainen postinumero
     */
    public static String testiArvoPostinro() {
        String apupostinro = String.format("%05d",rand(40000,49999));
        return apupostinro;
    }


     @Override
     public boolean equals(Object juhla) {
         if ( juhla instanceof Juhla ) return equals((Juhla)juhla);
         return false;
     }


     @Override
     public int hashCode() {
         return tunnusNro;
     }

        
     /**
      * Kerää juhlan tiedot String-taulukkoon
      * @return Juhlan tiedot String-taulukossa
      */
     public String[] keraaTiedot() {
         String[] Juhlia = new String[5];
         Juhlia[0] = paivamaara;
         Juhlia[1] = juhlanTyyppi;
         Juhlia[2] = katuosoite;
         Juhlia[3] = postinumero;
         Juhlia[4] = postiosoite;
         return Juhlia;
     }
    
      
    /**
     * Arvotaan satunnainen kokonaisluku välille [ala,yla]
     * @param ala arvonnan alaraja
     * @param yla arvonnan yläraja
     * @return satunnainen luku väliltä [ala,yla]
     */
    public static int rand(int ala, int yla) {
        double n = (yla-ala)*Math.random() + ala;
        return (int)Math.round(n);
    }
    

    /**
     * Testiohjelma juhlalle.
     * @param args ei käytössä
     */
    public static void main(String args[]) {
        Juhla juhla = new Juhla(), juhla2 = new Juhla();
        juhla.rekisteroi();
        juhla2.rekisteroi();
        
        juhla.tulosta(System.out);
        juhla.testiVastaaJuhla();
        juhla.tulosta(System.out);

        juhla2.tulosta(System.out);
        juhla2.testiVastaaJuhla();
        juhla2.tulosta(System.out);
    }
}