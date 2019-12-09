package fxJuhlarekisteri.Kanta;

/**
 * Tarkistaja joka tarkistaa että jono sisältää vain valittuja merkkejä.
 * Hyväksyy tyhjän jonon.
 * @author Sampo Kupiainen & Sami Järvinen
 * @version 12.6.2019
 *
 */
public class Tarkistaja  {

    /** Numeroita vastaavat merkit */
    public static final String NUMEROT = "0123456789";

    /** Päivämäärään käyvät merkit */
    public static final String PVMNUMEROT = "0123456789.";

    /**
     * Onko jonossa vain sallittuja merkkejä
     * @param jono      tutkittava jono
     * @param sallitut  merkit joita sallitaan
     * @return true jos vain sallittuja, false muuten
     * @example
     * <pre name="test">
     *   onkoVain("12E","12")   === false;
     *   onkoVain("123","1234") === true;
     *   onkoVain("","1234") === true;
     * </pre> 
     */
    public static boolean onkoVain(String jono, String sallitut) {
        for (int i=0; i<jono.length(); i++)
            if ( sallitut.indexOf(jono.charAt(i)) < 0 ) return false;
        return true;
    }

    private final String sallitut;

    /**
     * Luodaan tarkistaja joka hyväksyy sallitut merkit
     * @param sallitut hyväksyttävät merkit
     */
    public Tarkistaja(String sallitut) {
        this.sallitut = sallitut;
    }

    
    /**
     * Tarkistaa että jono sisältää vain numeroita
     * @param jono tutkittava jono
     * @return null jos vain valittujan merkkejä, muuten virheilmoitus
     * @example
     * <pre name="test">
     *   Tarkistaja tar = new Tarkistaja("123");
     *   tar.tarkista("12") === null;
     *   tar.tarkista("14") === "Saa olla vain merkkejä: 123";
     *   tar.tarkista("")   === null;
     * </pre>
     */
    public String tarkista(String jono) {
        if ( onkoVain(jono, sallitut) ) return null;
        return "Saa olla vain merkkejä: " + sallitut;
    }
}