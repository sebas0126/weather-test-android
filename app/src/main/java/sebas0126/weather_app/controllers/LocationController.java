package sebas0126.weather_app.controllers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import sebas0126.weather_app.R;
import sebas0126.weather_app.services.WeatherBgService;

public class LocationController {

    public interface AsyncResponse {
        void processFinish(String output);
    }

    public LocationController.AsyncResponse delegate = null;

    private Activity mainActivity;

    public LocationController(Activity activity, LocationController.AsyncResponse delegate){
        mainActivity = activity;
        this.delegate = delegate;
    }

    public boolean requestPosition() {
        LocationManager locationManager = (LocationManager) mainActivity.getSystemService(Context.LOCATION_SERVICE);

        if (locationManager != null) {
            if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                Toast.makeText(mainActivity, mainActivity.getString(R.string.msg_gps), Toast.LENGTH_LONG).show();
                return false;
            }
        }

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                String latitude = "" + location.getLatitude();
                String longitude = "" + location.getLongitude();

                getWeather(latitude, longitude);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        if (ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
        return true;
    }

    private void getWeather(String latitude, String longitude){
        new WeatherBgService(new WeatherBgService.AsyncResponse() {

            @Override
            public void processFinish(String output) {
                Log.w("RESULT", output);
                delegate.processFinish(output);
            }
        }).execute(mainActivity.getString(R.string.weather_service), latitude, longitude);
    }

}
