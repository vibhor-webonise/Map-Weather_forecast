import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Created by webonise on 25-03-2015.
 */
public class MapViewController {
    @FXML
    HBox myMapView;



    class MyMapView extends Region {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();


        public MyMapView(){

            final URL urlGoogleMaps = getClass().getResource("MapView.html");
            webEngine.load(urlGoogleMaps.toExternalForm());
            webEngine.setJavaScriptEnabled(true);
            myMapView.getChildren().add(webView);
            //getChildren().add(webview);
        }
    }

    /*private void load_mapView() {
        myMapView = new MyMapView();
    }*/

}



