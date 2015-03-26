/**
 * Created by webonise on 25-03-2015.
 */

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.URL;

public class MapView extends Application {

    private Scene scene;
    MyBrowser myBrowser;

    @Override
    public void start(Stage stage) throws Exception {

        //Parent root = FXMLLoader.load(getClass().getResource("MapView.fxml"));
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.TOP_CENTER);
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(25, 25, 25, 25));

        Label location = new Label("Location:");
        pane.add(location, 0, 1);
        TextField locationField = new TextField();
        pane.add(locationField, 1, 1);
        Label latitude = new Label("Latitude:");
        pane.add(latitude,0,2);
        TextField latitudeField = new TextField();
        pane.add(latitudeField, 1, 2);
        Label longitude = new Label("Longitude:");
        pane.add(longitude, 0, 3);
        TextField longitudeField = new TextField();
        pane.add(longitudeField, 1, 3);



        myBrowser = new MyBrowser();


        SplitPane sp = new SplitPane();
        final StackPane sp1 = new StackPane();
        sp1.getChildren().add(pane);
        final StackPane sp2 = new StackPane();
        sp2.getChildren().add(myBrowser);

        sp.getItems().addAll(sp1, sp2);
        sp.setDividerPositions(0.35f, 0.6f);

        scene = new Scene(sp, 800, 600);
        stage.setTitle("Google Maps");
        stage.setScene(scene);
        stage.show();

    }



    class MyBrowser extends Region {

        HBox toolbar;

        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        public MyBrowser() {

            final URL urlGoogleMaps = getClass().getResource("MapView.html");
            webEngine.load(urlGoogleMaps.toExternalForm());

            getChildren().add(webView);

        }
    }
        public static void main(String[] args) {
            launch(args);
        }

}