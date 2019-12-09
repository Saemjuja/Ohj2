package fxJuhlarekisteri.Kayttoliittyma;

import javafx.scene.layout.GridPane; 
import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
    
import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.StringGrid;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
  
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode; 
    
import fxJuhlarekisteri.SailoException;
import fxJuhlarekisteri.Tietorakenne.Juhla;
import fxJuhlarekisteri.Tietorakenne.Rekisteri;
import fxJuhlarekisteri.Tietorakenne.Ruokavalio;
    
import static fxJuhlarekisteri.Kayttoliittyma.TietueDialogController.getFieldId; 

/**
 * Luokka rekisterin käyttöliittymän tapahtumien hoitamiseksi.
 * @author Sampo Kupiainen & Sami Järvinen
 * @version 11.6.2019
 */
public class JuhlarekisteriGUIController  implements Initializable {

    @FXML private TextField hakuehto;
    @FXML private ComboBoxChooser<String> cbKentat;
    @FXML private Label labelVirhe;
    @FXML private ScrollPane panelJuhla;
    @FXML private GridPane gridJuhla; 
    @FXML private ListChooser<Juhla> chooserJuhlat;
    @FXML private StringGrid<Ruokavalio> tableRuokavaliot;    

        
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();      
    }

        
        @FXML private void handleHakuehto() {
           hae(0);
        }
        
        @FXML private void handleValioOhje() {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Ruokavalioiden ohje");
            alert.setHeaderText(null);
            alert.setContentText("Ruokavalioiksi kelpaavat: gluteeniton, viinaa, alkoholiton, vegaaninen, laktoositon, karppaus");
            alert.showAndWait();
        }
        
        @FXML private void handleTallenna() {
            tallenna();
        }
        
        
        @FXML private void handleAvaa() {
            avaa();
        }
        
        
        @FXML private void handleTulosta() {
            TulostusController tulostusCtrl = TulostusController.tulosta(null); 
            tulostaValitut(tulostusCtrl.getTextArea()); 
        } 

        
        @FXML private void handleLopeta() {
            tallenna();
            Platform.exit();
        } 

        
        @FXML private void handleUusiJuhla() {
            uusiJuhla();
        }
        
        
        @FXML private void handleMuokkaaJuhla() {
            muokkaa(kentta);
        }
        
        
        @FXML private void handlePoistaJuhla() {
            poistaJuhla();
        }
        
         
        


        @FXML private void handleUusiRuokavalio() {
            uusiRuokavalio(); 
        }
        

        @FXML private void handlePoistaRuokavalio() {
          poistaRuokavalio();
        }
        

       


        @FXML private void handleApua() {
            avustus();
        }
        

        @FXML private void handleTietoja() {
            ModalController.showModal(JuhlarekisteriGUIController.class.getResource("AboutView.fxml"), "Rekisteri", null, "");
        }
        

    //===========================================================================================    
    // Tästä eteenpäin ei käyttöliittymään suoraan liittyvää koodia    
        
        private String rekisterinnimi = "Oletusrekisteri";
        private Rekisteri rekisteri;
        private Juhla juhlanKohdalla;
        private TextField edits[];
        private int kentta = 0;
        private static Ruokavalio apuruokavalio = new Ruokavalio(); 
        private static Juhla apujuhla = new Juhla();  
        
        
        /**
         * Tekee tarvittavat muut alustukset, nyt vaihdetaan GridPanen tilalle yksi iso tekstikentt�, johon voidaan tulostaa juhlien tiedot.
         * Alustetaan myös juhlalistan kuuntelija 
         */
        protected void alusta() {
            chooserJuhlat.clear();
            chooserJuhlat.addSelectionListener(e -> naytaJuhla());
            cbKentat.clear(); 
        for (int k = apujuhla.ekaKentta(); k < apujuhla.getKenttia(); k++) 
             cbKentat.add(apujuhla.getKysymys(k), null); 
         cbKentat.getSelectionModel().select(0); 
        
         edits = TietueDialogController.luoKentat(gridJuhla, apujuhla); 
            
            for (TextField edit: edits)  
                if ( edit != null ) {  
                    edit.setEditable(false);  
                    edit.setOnMouseClicked(e -> { if ( e.getClickCount() > 1 ) muokkaa(getFieldId(e.getSource(),0)); });  
                    edit.focusedProperty().addListener((a,o,n) -> kentta = getFieldId(edit,kentta));
                    edit.setOnKeyPressed( e -> {if ( e.getCode() == KeyCode.F2 ) muokkaa(kentta);}); 
                }    
            // alustetaan ruokavalioiden otsikot 
            int eka = apuruokavalio.ekaKentta(); 
            int lkm = apuruokavalio.getKenttia(); 
            String[] headings = new String[lkm-eka]; 
            for (int i=0, k=eka; k<lkm; i++, k++) headings[i] = apuruokavalio.getKysymys(k); 
            tableRuokavaliot.initTable(headings); 
            tableRuokavaliot.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); 
            tableRuokavaliot.setEditable(false); 
            tableRuokavaliot.setPlaceholder(new Label("Ei vielä ruokavalioita")); 
             
            // Tämä on vielä huono, ei automaattisesti muutu jos kenttiä muutetaan. 
            tableRuokavaliot.setColumnSortOrderNumber(1); 
            tableRuokavaliot.setColumnSortOrderNumber(2); 
            tableRuokavaliot.setColumnWidth(1, 60); 
            tableRuokavaliot.setColumnWidth(2, 60); 
            
            tableRuokavaliot.setOnMouseClicked( e -> { if ( e.getClickCount() > 1 ) muokkaaRuokavaliota(); } );
            tableRuokavaliot.setOnKeyPressed( e -> {if ( e.getCode() == KeyCode.F2 ) muokkaaRuokavaliota();}); 
        }

        /**
         * Näyttää errorit
         * @param virhe virhe
         */
        @SuppressWarnings("unused")
        private void naytaVirhe(String virhe) {
            if ( virhe == null || virhe.isEmpty() ) {
                labelVirhe.setText("");
                labelVirhe.getStyleClass().removeAll("virhe");
                return;
            }
            labelVirhe.setText(virhe);
            labelVirhe.getStyleClass().add("virhe");
        }
        
        
        private void setTitle(String title) {
            ModalController.getStage(hakuehto).setTitle(title);
        }
        
        
    /**
     * Alustaa rekisterin lukemalla sen valitun nimisestä tiedostosta
     * @param nimi tiedosto josta rekisterin tiedot luetaan
     * @return null jos onnistuu, muuten virhe tekstinä
     */
    public String lueTiedosto(String nimi) {
        rekisterinnimi = nimi;
        setTitle("Rekisteri - " + rekisterinnimi);
        try {
            rekisteri.lueTiedostosta(nimi);
            hae(0);
            return null;
        } catch (SailoException e) {
            hae(0);
            String virhe = e.getMessage(); 
            if ( virhe != null ) Dialogs.showMessageDialog(virhe);
            return virhe;
        }
     }

        
        /**
         * Kysytään tiedoston nimi ja luetaan se
         * @return true jos onnistui, false jos ei
         */
        public boolean avaa() {
            String uusinimi = RekisterinNimiController.kysyNimi(null, rekisterinnimi);
            if (uusinimi == null) return false;
            lueTiedosto(uusinimi);
            return true;
        }

        
        /**
         * Tietojen tallennus
         * @return null jos onnistuu, muuten virhe tekstin�
         */
        private String tallenna() {
            try {
                rekisteri.tallenna();
                return null;
            } catch (SailoException ex) {
                Dialogs.showMessageDialog("Tallennuksessa ongelmia! " + ex.getMessage());
                return ex.getMessage();
            }
        }


        /**
         * Tarkistetaan onko tallennus tehty
         * @return true jos saa sulkea sovelluksen, false jos ei
         */
        public boolean voikoSulkea() {
            tallenna();
            return true;
        }
        
        
        /**
         * Näyttää listasta valitun juhlan tiedot, tilapäisesti yhteen isoon edit-kentt��n
         */
        protected void naytaJuhla() {
            juhlanKohdalla = chooserJuhlat.getSelectedObject();

            if (juhlanKohdalla == null) return;

            TietueDialogController.naytaTietue(edits, juhlanKohdalla);
            naytaRuokavaliot(juhlanKohdalla);
        }
          

        /**
         * Hakee juhlien tiedot listaan
         * @param jnr juhlan numero, joka aktivoidaan haun j�lkeen
         */
        protected void hae(int jnr) {
            int jnro = jnr; // jnro jäsenen numero, joka aktivoidaan haun jälkeen
         if ( jnro <= 0 ) { 
             Juhla kohdalla = juhlanKohdalla; 
            if ( kohdalla != null ) jnro = kohdalla.getTunnusNro(); 
         }
       
         int k = cbKentat.getSelectionModel().getSelectedIndex() + apujuhla.ekaKentta(); 
         String ehto = hakuehto.getText(); 
        if (ehto.indexOf('*') < 0) ehto = "*" + ehto + "*"; 
                    
                     chooserJuhlat.clear();
             
                     int index = 0;
                    Collection<Juhla> juhlat;
                     try {
                         juhlat = rekisteri.etsi(ehto, k);
                         int i = 0;
                         for (Juhla juhla:juhlat) {
                             if (juhla.getTunnusNro() == jnro) index = i;
                             chooserJuhlat.add(juhla.getPaivamaara(), juhla);
                             i++;
                         }
                     } catch (SailoException ex) {
                        Dialogs.showMessageDialog("Juhlan hakemisessa ongelmia! " + ex.getMessage());
             }
         chooserJuhlat.setSelectedIndex(index); // tästä tulee muutosviesti joka näyttää jäsenen
     }


        /**
         * Luo uuden juhlan jota aletaan editoimaan 
         */
        protected void uusiJuhla() {
            try {
                Juhla uusi = new Juhla();
                uusi = TietueDialogController.kysyTietue(null, uusi, 1);  
                if ( uusi == null ) return;
                uusi.rekisteroi();
                rekisteri.lisaa(uusi);
                hae(uusi.getTunnusNro());
            } catch (SailoException e) {
                Dialogs.showMessageDialog("Ongelmia uuden luomisessa " + e.getMessage());
                return;
            }
        }
        
        /**
         * Näyttää juhlan ruokavaliot
         * @param juhla
         */
        private void naytaRuokavaliot(Juhla juhla) {
        tableRuokavaliot.clear();
        if ( juhla == null ) return;
        
        List<Ruokavalio> ruokavaliot = rekisteri.annaRuokavaliot(juhla);
        if ( ruokavaliot.size() == 0 ) return;
        for (Ruokavalio har: ruokavaliot)
            naytaRuokavalio(har); 
    }

    /**
     * Näyttää ruokavaliot
     * @param har
     */
    private void naytaRuokavalio(Ruokavalio har) {
        int kenttia = har.getKenttia(); 
        String[] rivi = new String[kenttia-har.ekaKentta()]; 
        for (int i=0, k=har.ekaKentta(); k < kenttia; i++, k++) 
            rivi[i] = har.anna(k); 
        tableRuokavaliot.add(har,rivi);
    }
  

        /**
         * Tekee uuden tyhjän ruokavalion editointia varten
         */
        public void uusiRuokavalio() {
            if ( juhlanKohdalla == null ) return; 
            try {
            Ruokavalio uusi = new Ruokavalio(juhlanKohdalla.getTunnusNro());
            uusi = TietueDialogController.kysyTietue(null, uusi, 0);
            if ( uusi == null ) return;
            uusi.rekisteroi();
            rekisteri.lisaa(uusi);
            naytaRuokavaliot(juhlanKohdalla); 
            tableRuokavaliot.selectRow(1000);  // järjestetään viimeinen rivi valituksi
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Lisääminen epäonnistui: " + e.getMessage());
        }
    }
        
     /**
      * Muokkaa valittua ruokavaliota
      */
    private void muokkaaRuokavaliota() {
        int r = tableRuokavaliot.getRowNr();
        if ( r < 0 ) return; // klikattu ehkä otsikkoriviä
        Ruokavalio har = tableRuokavaliot.getObject();
        if ( har == null ) return;
        int k = tableRuokavaliot.getColumnNr()+har.ekaKentta();
        try {
            har = TietueDialogController.kysyTietue(null, har.clone(), k);
            if ( har == null ) return;
            rekisteri.korvaaTaiLisaa(har); 
            naytaRuokavaliot(juhlanKohdalla); 
            tableRuokavaliot.selectRow(r);  // järjestetään sama rivi takaisin valituksi
        } catch (CloneNotSupportedException  e) { /* clone on tehty */  
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ongelmia lisäämisessä: " + e.getMessage());
        }
    }
        
        /**
         * Hoitaa muokkaamisen
         * @param k
         */
    private void muokkaa(int k) { 
        if ( juhlanKohdalla == null ) return; 
        try { 
            Juhla juhla; 
            juhla = TietueDialogController.kysyTietue(null, juhlanKohdalla.clone(), k); 
            if ( juhla == null ) return; 
            rekisteri.korvaaTaiLisaa(juhla); 
            hae(juhla.getTunnusNro()); 
        } catch (CloneNotSupportedException e) { 
            // 
        } catch (SailoException e) { 
            Dialogs.showMessageDialog(e.getMessage()); 
        } 
    }


        /**
         * @param rekisteri rekisteri jota k�ytet��n t�ss� k�ytt�liittym�ss�
         */
        public void setRekisteri(Rekisteri rekisteri) {
            this.rekisteri = rekisteri;
            naytaJuhla();
        }

        
        /**
         * N�ytet��n ohjelman suunnitelma erillisess� selaimessa.
         */
        private void avustus() {
            Desktop desktop = Desktop.getDesktop();
            try {
                URI uri = new URI("https://tim.jyu.fi/view/kurssit/tie/ohj2/2019k/ht/kupisjxt");
                desktop.browse(uri);
            } catch (URISyntaxException e) {
                return;
            } catch (IOException e) {
                return;
            }
        }

        
        /**
         * Tulostaa juhlan tiedot
         * @param os tietovirta johon tulostetaan
         * @param juhla tulostettava juhla
         */
        public void tulosta(PrintStream os, final Juhla juhla) {
            os.println("----------------------------------------------");
            juhla.tulosta(os);
            os.println("----------------------------------------------");
        List<Ruokavalio> ruokavaliot = rekisteri.annaRuokavaliot(juhla);
        for (Ruokavalio har:ruokavaliot) 
            har.tulosta(os);      
    }
        
        /*
         * Poistetaan listalta valittu juhla
         */
        private void poistaJuhla() {
            Juhla juhla = juhlanKohdalla;
            if ( juhla == null ) return;
            if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko juhla: " + juhla.getPaivamaara(), "Kyllä", "Ei") )
                return;
            rekisteri.poista(juhla);
            int index = chooserJuhlat.getSelectedIndex();
            hae(0);
            chooserJuhlat.setSelectedIndex(index);
        }
        
        
        /**
         * Poistetaan ruokavaliotaulukosta valitulla kohdalla oleva ruokavalio. 
         */
        private void poistaRuokavalio() {
            int rivi = tableRuokavaliot.getRowNr();
            if ( rivi < 0 ) return;
            Ruokavalio ruokavalio = tableRuokavaliot.getObject();
            if ( ruokavalio == null ) return;
            rekisteri.poistaRuokavalio(ruokavalio);
            naytaRuokavaliot(juhlanKohdalla);
            int harrastuksia = tableRuokavaliot.getItems().size(); 
            if ( rivi >= harrastuksia ) rivi = harrastuksia -1;
            tableRuokavaliot.getFocusModel().focus(rivi);
            tableRuokavaliot.getSelectionModel().select(rivi);
        }
        
        
        /**
         * Tulostaa listassa olevat juhlat tekstialueeseen
         * @param text alue johon tulostetaan
         */
        public void tulostaValitut(TextArea text) {
            try (PrintStream os = TextAreaOutputStream.getTextPrintStream(text)) {
                os.println("Tulostetaan kaikki juhlat"); 
                for (Juhla juhla: chooserJuhlat.getObjects()) {   
                    tulosta(os, juhla);
                    os.println("\n\n");
                }
            }
        }
    }
