
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.DecimalFormat;

public class MapView extends Application {

    private Scene scene;
    private WebView webView;
    private WeatherReport weatherReport;
    Double lat;
    Double lng;
    Label latitudeField;
    Label longitudeField;
    Label countryField;
    Label locationField;
    Label sunriseField;
    Label sunsetField;
    Label weatherField;
    Label tempField;
    Label pressureField;
    Label humidityField;
    Label windSpeedField;

    @Override
    public void start(Stage stage) throws Exception {

        webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        final URL urlGoogleMaps = getClass().getResource("html/MapBox.html");
        webEngine.load(urlGoogleMaps.toExternalForm());

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.TOP_CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(20);
        gridPane.setPadding(new Insets(25, 25, 25, 50));

        Label getWeatherLabel = new Label("Weather Forecast");
        getWeatherLabel.setMaxSize(200,200);
        getWeatherLabel.setScaleX(2);
        getWeatherLabel.setScaleY(4);
        gridPane.add(getWeatherLabel,1,1);
        getWeatherLabel.setPrefSize(150, 20);

        Label latitude = new Label("Latitude:");
        gridPane.add(latitude,0,3);
        latitudeField = new Label();
        gridPane.add(latitudeField, 1, 3);

        Label longitude = new Label("Longitude:");
        gridPane.add(longitude, 0, 4);
        longitudeField = new Label();
        gridPane.add(longitudeField, 1, 4);

        webEngine.getLoadWorker()
                .stateProperty()
                .addListener( (obs, oldValue, newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED) {
                        JSObject jsobj = (JSObject) webView.getEngine().executeScript("window");
                        jsobj.call("initialize");
                        jsobj.call("getJavaToJSBridge" , this);
                    }
                });

        Label country = new Label("Country:");
        gridPane.add(country,0,5);
        countryField = new Label();
        gridPane.add(countryField, 1, 5);

        Label location = new Label("Location:");
        gridPane.add(location,0,6);
        locationField = new Label();
        gridPane.add(locationField, 1, 6);

        Label sunrise = new Label("Sunrise:");
        gridPane.add(sunrise,0,7);
        sunriseField = new Label();
        gridPane.add(sunriseField, 1, 7);

        Label sunset = new Label("Sunset:");
        gridPane.add(sunset,0,8);
        sunsetField = new Label();
        gridPane.add(sunsetField, 1, 8);

        Label weather = new Label("Weather:");
        gridPane.add(weather,0,9);
        weatherField = new Label();
        gridPane.add(weatherField, 1, 9);

        Label temp = new Label("Temperature:");
        gridPane.add(temp,0,10);
        tempField = new Label();
        gridPane.add(tempField, 1, 10);

        Label pressure = new Label("Pressure:");
        gridPane.add(pressure,0,11);
        pressureField = new Label();
        gridPane.add(pressureField, 1, 11);

        Label humidity = new Label("Humidity:");
        gridPane.add(humidity,0,12);
        humidityField = new Label();
        gridPane.add(humidityField, 1, 12);

        Label windSpeed = new Label("WindSpeed:");
        gridPane.add(windSpeed,0,13);
        windSpeedField = new Label();
        gridPane.add(windSpeedField, 1, 13);

        SplitPane sp = new SplitPane();
        final StackPane sp1 = new StackPane();
        sp1.getChildren().add(gridPane);
        final StackPane sp2 = new StackPane();
        sp2.getChildren().add(webView);

        sp.getItems().addAll(sp1, sp2);
        sp.setDividerPositions(0.4f, 0.6f);

        scene = new Scene(sp, 800, 600);
        stage.setTitle("Google Maps");
        stage.setScene(scene);
        stage.show();
    }

    public double roundTwoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.######");
        return Double.valueOf(twoDForm.format(d));
    }

    public void setLatLng(Object lat,Object lng){
        this.lat = (Double)lat;
        this.lng = (Double)lng;
        double latitude = roundTwoDecimals(this.lat);
        double longitude = roundTwoDecimals(this.lng);
        latitudeField.setText(""+latitude);
        longitudeField.setText(""+longitude);
        try {
            String URL = "http://api.openweathermap.org/data/2.5/" +
                    "weather?lat=" + latitudeField.getText() + "&" + "lon=" + longitudeField.getText() + "&type=JSON";
            Client client = Client.create();
            WebResource webResource = client.resource(URL) ;
            ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            } else {
                String json = response.getEntity(String.class);
                System.out.println(json);
                ParseJsonResult(json);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

     private void ParseJsonResult(String json) throws JSONException {

             JSONObject jsonObject = new JSONObject(json);
             weatherReport = new WeatherReport();
             //"coord"
             JSONObject jsonObject_coord = jsonObject.getJSONObject("coord");
             Double result_lon = jsonObject_coord.getDouble("lon");
             Double result_lat = jsonObject_coord.getDouble("lat");

             //"sys"
             JSONObject jsonObject_sys = jsonObject.getJSONObject("sys");
             String result_country = jsonObject_sys.getString("country");
             weatherReport.setTagCountry(result_country);
             countryField.setText(weatherReport.getTagCountry());

             String result_sunrise = jsonObject_sys.getString("sunrise");
             weatherReport.setTagSunrise(result_sunrise);
             sunriseField.setText(weatherReport.getTagSunrise());

             String result_sunset = jsonObject_sys.getString("sunset");
             weatherReport.setTagSunset(result_sunset);
             sunsetField.setText(weatherReport.getTagSunset());

             //"weather"
             String result_weather;
             JSONArray jsonArray_weather = jsonObject.getJSONArray("weather");
             if (jsonArray_weather.length() > 0) {
                 JSONObject jsonObject_weather = jsonArray_weather.getJSONObject(0);
                 String result_id = jsonObject_weather.getString("id");
                 String result_main = jsonObject_weather.getString("main");
                 String result_description = jsonObject_weather.getString("description");
                 String result_icon = jsonObject_weather.getString("icon");

                 result_weather = "weather\tid: " + result_id + "\tmain: " + result_main + "\tdescription: " + result_description + "\ticon: " + result_icon;
                 weatherReport.setTagDescription(result_description);
                 weatherField.setText(weatherReport.getTagDescription());
             } else {
                 result_weather = "weather empty!";
             }

             //"base"
             String result_base = jsonObject.getString("base");

             //"main"
             JSONObject jsonObject_main = jsonObject.getJSONObject("main");
             String result_temp = jsonObject_main.getString("temp");
             weatherReport.setTagTemperature(result_temp);
             tempField.setText(weatherReport.getTagTemperature());

             String result_pressure = jsonObject_main.getString("pressure");
             weatherReport.setTagPressure(result_pressure);
             pressureField.setText(weatherReport.getTagPressure());

             String result_humidity = jsonObject_main.getString("humidity");
             weatherReport.setTagHumidity(result_humidity);
             humidityField.setText(weatherReport.getTagHumidity());

             String result_temp_min = jsonObject_main.getString("temp_min");
             String result_temp_max = jsonObject_main.getString("temp_max");


             //"wind"
             JSONObject jsonObject_wind = jsonObject.getJSONObject("wind");
             String result_speed = jsonObject_wind.getString("speed");
             weatherReport.setTagWindSpeed(result_speed);
             windSpeedField.setText(weatherReport.getTagWindSpeed());


             Double result_deg = jsonObject_wind.getDouble("deg");
             String result_wind = "wind\tspeed: " + result_speed + "\tdeg: " + result_deg;

             //"clouds"
             JSONObject jsonObject_clouds = jsonObject.getJSONObject("clouds");
             int result_all = jsonObject_clouds.getInt("all");

             //"dt"
             int result_dt = jsonObject.getInt("dt");

             //"id"
             int result_id = jsonObject.getInt("id");

             //"name"
             String result_name = jsonObject.getString("name");
         weatherReport.setTagName(result_name);
             locationField.setText(weatherReport.getTagName());

             //"cod"
             int result_cod = jsonObject.getInt("cod");

     }


    public static void main(String[] args) {
        launch(args);
    }

    }





