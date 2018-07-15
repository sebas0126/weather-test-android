package sebas0126.weather_app;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import sebas0126.weather_app.utilities.Util;

import sebas0126.weather_app.controllers.LocationController;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_LOCATION = 99;
    private LocationController locationCtrl;

    private LinearLayout lytLinear;
    private FrameLayout lytFrame;
    private ConstraintLayout lytMain;

    private TextView txtTemperature,
    txtPrecipitation,
    txtHumidity,
    txtWeather;

    private ImageView imgWeather;

    private FloatingActionButton btnFloat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lytLinear = findViewById(R.id.lytLinear);
        lytFrame = findViewById(R.id.lytFrame);
        lytMain = findViewById(R.id.lytMain);

        txtHumidity = findViewById(R.id.txtHumidity);
        txtPrecipitation = findViewById(R.id.txtPrecipitation);
        txtTemperature = findViewById(R.id.txtTemperature);
        txtWeather = findViewById(R.id.txtWeather);

        imgWeather = findViewById(R.id.imgWeather);

        btnFloat = findViewById(R.id.btnFloat);

        locationCtrl = new LocationController(this, new LocationController.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                try {
                    JSONObject obj = new JSONObject(output).getJSONObject("currently");

                    String humidity = Util.toPercentage(obj.getDouble("humidity")),
                            precipProbability = Util.toPercentage(obj.getDouble("precipProbability")),
                            temperature = Util.convertTemperature(obj.getDouble("temperature")),
                            weather = obj.getString("summary"),
                            icon = obj.getString("icon");

                    txtHumidity.setText(humidity + "%");
                    txtPrecipitation.setText(precipProbability + "%");
                    txtTemperature.setText(temperature + " ºC");
                    txtWeather.setText(weather);

                    imgWeather.setImageResource(getWeatherImage(icon));

                    lytMain.setBackgroundResource(getBackground(icon));

                    lytFrame.setVisibility(View.GONE);
                    lytLinear.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private int getBackground(final String weather){
        switch (weather){
            case "clear-day":
                return R.drawable.clear_day;
            case "clear-night":
                return R.drawable.clear_night;
            case "rain":
                return R.drawable.rain;
            case "snow":
                return R.drawable.snow;
            case "fog":
                return R.drawable.fog;
            case "cloudy":
                return R.drawable.cloudy;
            case "partly-cloudy-day":
                return R.drawable.partly_cloudy_day;
            case "partly-cloudy-night":
                return R.drawable.partly_cloudy_night;
            default:
                return R.drawable.cloudy;
        }
    }

    private int getWeatherImage(final String weather){
        switch (weather){
            case "clear-day":
                return R.drawable.img_15;
            case "clear-night":
                return R.drawable.img_12;
            case "rain":
                return R.drawable.img_04;
            case "snow":
                return R.drawable.img_10;
            case "fog":
                return R.drawable.img_13;
            case "cloudy":
                return R.drawable.img_17;
            case "partly-cloudy-day":
                return R.drawable.img_07;
            case "partly-cloudy-night":
                return R.drawable.img_16;
            default:
                return R.drawable.img_05;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        requestPermission();
    }

    public void refresh(View v){
        lytLinear.setVisibility(View.GONE);
        lytFrame.setVisibility(View.VISIBLE);
        requestPermission();
    }

    public void requestPermission() {
        btnFloat.setVisibility(View.GONE);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }else{
            if(!locationCtrl.requestPosition()){
                btnFloat.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if(!locationCtrl.requestPosition()){
                        btnFloat.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(this, getText(R.string.msg_gps), Toast.LENGTH_SHORT).show();
                    requestPermission();
                }
                return;
            }
        }
    }


}
