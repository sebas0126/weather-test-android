package sebas0126.weather_app.services;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


    public class WeatherBgService extends AsyncTask<String, String, String> {

        public interface AsyncResponse {
            void processFinish(String output);
        }

        public AsyncResponse delegate = null;

        public WeatherBgService(AsyncResponse delegate){
            this.delegate = delegate;
        }

        HttpURLConnection conn;
        StringBuilder result;

        @Override
        protected String doInBackground(String... params) {

            JSONObject retObj = null;

            try {

                String strUrl = params[0] + params[1] + "," + params[2];

                URL url = new URL(strUrl);

                conn = (HttpURLConnection) url.openConnection();

                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                Log.d("NetworkConnect", "result: " + result.toString());

                retObj = new JSONObject(result.toString());


            } catch (Exception e) {
                Log.e("weather", "Error in GetData", e);
            }
            return String.valueOf(retObj);
        }

        @Override
        protected void onPostExecute(String result) {
            delegate.processFinish(result);
        }
    }
