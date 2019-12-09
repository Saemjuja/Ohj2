package fxJuhlarekisteri.Tietorakenne;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import fxJuhlarekisteri.Kayttoliittyma.JuhlarekisteriGUIController;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;

/**
 * @author Sami Järvinen & Sampo Kupiainen
 * @version 28.5.2019
 *
 */
public class JuhlarekisteriMain extends Application {
    @Override
    public void start(Stage primaryStage) {
    
        try {
            final FXMLLoader ldr = new FXMLLoader(getClass().getResource("/fxJuhlarekisteri/Kayttoliittyma/JuhlarekisteriGUIView.fxml"));
            final Pane root = (Pane)ldr.load();
            final JuhlarekisteriGUIController rekisteriCtrl = (JuhlarekisteriGUIController)ldr.getController();

            final  Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/fxJuhlarekisteri/Kayttoliittyma/juhlarekisteri.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Juhlarekisteri");
            
            primaryStage.setOnCloseRequest((event) -> {
                    if ( !rekisteriCtrl.voikoSulkea() ) event.consume();
                });
            
            Rekisteri rekisteri = new Rekisteri();
            rekisteriCtrl.setRekisteri(rekisteri);

            primaryStage.show();
            
            Application.Parameters params = getParameters(); 
            if ( params.getRaw().size() > 0 ) 
                rekisteriCtrl.lueTiedosto(params.getRaw().get(0));  
            else
                if ( !rekisteriCtrl.avaa() ) Platform.exit();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * Käynnistetään käyttöliittymä
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        launch(args);
    }
}