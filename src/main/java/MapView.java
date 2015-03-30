/**
 * Created by webonise on 25-03-2015.
 */

import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.net.URL;

public class MapView extends Application {

    private Scene scene;
    private WebView webView;
    private JerseyClientPostService jerseyClientPostService;
    Double lat;
    Double lng;
    TextField latitudeField;
    TextField longitudeField;
    @Override
    public void start(Stage stage) throws Exception {

        webView = new WebView();
        WebEngine webEngine = webView.getEngine();


        webEngine.getLoadWorker()
                .stateProperty()
                .addListener( (obs, oldValue, newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED) {
                        JSObject jsobj = (JSObject) webView.getEngine().executeScript("window");
                        jsobj.call("initialize");
                        jsobj.call("getJavaToJSBridge" , this);
                    }
                });

        final URL urlGoogleMaps = getClass().getResource("MapView.html");
        webEngine.load(urlGoogleMaps.toExternalForm());

        GridPane pane = new GridPane();
        pane.setAlignment(Pos.TOP_CENTER);
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(25, 25, 25, 25));

        Label latitude = new Label("Latitude:");
        pane.add(latitude,0,1);
        latitudeField = new TextField();
        pane.add(latitudeField, 1, 1);

        Label longitude = new Label("Longitude:");
        pane.add(longitude, 0, 2);
        longitudeField = new TextField();
        pane.add(longitudeField, 1, 2);

        Button getWeatherButton = new Button("Get Weather");
        pane.add(getWeatherButton,1,4);
        getWeatherButton.setPrefSize(100, 20);
        getWeatherButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                String URL ="http://api.openweathermap.org/data/2.5/" +
                        "weather?lat="+latitudeField.getText()+"&"+"lon="+longitudeField.getText()+"'";

                Client client = Client.create();
                WebResource webResource = client.resource(URL);
                ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
                if (response.getStatus() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
                }
                String json = response.getEntity(String.class);
                System.out.println(json);

            }
        });

        SplitPane sp = new SplitPane();
        final StackPane sp1 = new StackPane();
        sp1.getChildren().add(pane);
        final StackPane sp2 = new StackPane();
        sp2.getChildren().add(webView);

        sp.getItems().addAll(sp1, sp2);
        sp.setDividerPositions(0.35f, 0.6f);

        scene = new Scene(sp, 800, 600);
        stage.setTitle("Google Maps");
        stage.setScene(scene);
        stage.show();
    }

    public void setLatLng(Object lat,Object lng){
        this.lat = (Double)lat;
        //System.out.print("\nLat: " + lat);
        latitudeField.setText(""+lat);
        longitudeField.setText(""+lng);

    }

    /*public void setLng(Object lng){
        this.lng = (Double)lng;
        //System.out.print("\nLng: " + lng);
        longitudeField.setText(""+lng);
    }*/

    public static void main(String[] args) {
        launch(args);
    }

   /* private Task createWeatherQueryWorker(String weatherRequest, String unitType){
            return new Task(){
                @Override
            protected Object call() throws Exception{
                    String json = jsonGetRequest(weatherRequest);

                    Platform.runLater(()->
                            webView.getEngine().executeScript(
                                    "populateWeatherData('" + json + "', " +
                                            "'" + unitType + "');")
                    );
                    return true;
                }
            };
    }*/

   /* public String jsonGetRequest(String urlQueryString){
        String json = null;
        try{
            System.out.println("Request" + urlQueryString);
            URL url =  new URL(urlQueryString);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoOutput(true);

            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("charset","UTF-8");
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            //json = streamToString(inputStream);
            json = inputStream.toString();
            System.out.println("Response: " + json);
        }catch(IOException e){
            e.printStackTrace();
        }
        return json;
    }*/


    }

    /*class MyBrowser extends Region {

        HBox toolbar;
        VBox toolbox;

        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        public MyBrowser() {

            final URL urlGoogleMaps = getClass().getResource("MapView.html");
            webEngine.load(urlGoogleMaps.toExternalForm());

            getChildren().add(webView);

            JSObject jsobj = (JSObject) webEngine.executeScript("window");
            jsobj.call("getBridge",this);
        }
    }*/




