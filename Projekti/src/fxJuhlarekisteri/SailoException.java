package fxJuhlarekisteri;
/**
 * @author Sampo Kupiainen ja sami jarvinen
 * @version 1.3.2019
 */

    /**
     * Poikkeusluokka tietorakenteesta aiheutuville poikkeuksille.
   
     */
    public class SailoException extends Exception {
        private static final long serialVersionUID = 1L;


        /**
         * Poikkeuksen muodostaja jolle tuodaan poikkeuksessa
         * käytettävä viesti
         * @param viesti Poikkeuksen viesti
         */
        public SailoException(String viesti) {
            super(viesti);
        }
    }

