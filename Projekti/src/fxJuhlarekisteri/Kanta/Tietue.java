package fxJuhlarekisteri.Kanta;

/**
 * Rajapinta tietueelle johon voidaan taulukon avulla rakentaa
 * "attribuutit".
 * @author Sampo Kupiainen & Sami Järvinen
 * @version 11.6.2019
 * @example
 */
public interface Tietue {
    /**
     * @return tietueen kenttien lukumäärä
     * @example
     * <pre name="test">
     *   #import juhla.Ruokavalio;
     *   Ruokavalio har = new Ruokavalio();
     *   har.getKenttia() === 3;
     * </pre>
     */
    
    
    public abstract int getKenttia();
    /**
     * @return ensimmäinen käyttäjän syötettävän kentän indeksi
     * @example
     * <pre name="test">
     *   Ruokavalio har = new Ruokavalio();
     *   har.ekaKentta() === 2;
     * </pre>
     */
    
    
    public abstract int ekaKentta();
    /**
     * @param k minkä kentän kysymys halutaan
     * @return valitun kentän kysymysteksti
     * @example
     * <pre name="test">
     *   Ruokavalio har = new Ruokavalio();
     *   har.getKysymys(2) === "ala";
     * </pre>
     */
    
    
    public abstract String getKysymys(int k);
    /**
     * @param k Minkä kentän sisältö halutaan
     * @return valitun kentän sisältö
     * @example
     * <pre name="test">
     *   Ruokavalio har = new Ruokavalio();
     *   har.parse("   1   |  1  |   Kala   ");
     *   har.anna(0) === "1";   
     *   har.anna(1) === "1";   
     *   har.anna(2) === "Kalastus";   
     * </pre>
     */
    
    
    public abstract String anna(int k);
   
    /**
     * Asetetaan valitun kentän sisältö.  Mikäli asettaminen onnistuu,
     * palautetaan null, muutoin virheteksti.
     * @param k minkä kentän sisältö asetetaan
     * @param s asetettava sisältö merkkijonona
     * @return null jos ok, muuten virheteksti
     * @example
     * <pre name="test">
     *   Ruokavalio har = new Ruokavalio();
     *   har.aseta(3,"1940")  === null;
     *   har.aseta(4,"20")    === null;
     * </pre>
     */
    
    
    public abstract String aseta(int k, String s);
    /**
     * Tehdään identtinen klooni tietueesta
     * @return kloonattu tietue
     * @throws CloneNotSupportedException jos kloonausta ei tueta
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException
     *   Ruokavalio har = new Ruokavalio();
     *   har.parse("   2   |  10  |   Kalastus  | 1949 | 22 t ");
     *   Object kopio = har.clone();
     *   kopio.toString() === har.toString();
     *   har.parse("   1   |  11  |   Uinti  | 1949 | 22 t ");
     *   kopio.toString().equals(har.toString()) === false;
     *   kopio instanceof Harrastus === true;
     * </pre>
     */
    
    
    public abstract Tietue clone() throws CloneNotSupportedException;
    /**
     * Palauttaa tietueen tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return tietue tolppaeroteltuna merkkijonona
     * @example
     * <pre name="test">
     *   Ruokavalio harrastus = new Ruokavalio();
     *   ruokavalio.parse("   2   |  10  |   Kalastus  | 1949 | 22 t ");
     *   ruokavalio.toString()    =R= "2\\|10\\|Kalastus\\|1949\\|22.*";
     * </pre>
     */
    
    
    @Override
    public abstract String toString();
}
