package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView cityTextView,cloudTextView,tempTextView,mintempTextView,maxtempTextview,updatedTextView,sunriseTextView,sunsetTextView,windTextView,pressureTextView,humidityTextView;
    EditText nameText;
    Button check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityTextView= (TextView) findViewById(R.id.cityTextView);
        cloudTextView= (TextView) findViewById(R.id.cloudTextview);
        tempTextView= (TextView) findViewById(R.id.tempTextView);
        mintempTextView= (TextView) findViewById(R.id.minTempTextView);
        maxtempTextview= (TextView) findViewById(R.id.maxTempTextView);
        updatedTextView= (TextView) findViewById(R.id.updatedTextView);
        sunriseTextView= (TextView) findViewById(R.id.sunriseTextView);
        sunsetTextView= (TextView) findViewById(R.id.sunsetTextVIew);
        windTextView= (TextView) findViewById(R.id.windTextView);
        pressureTextView= (TextView) findViewById(R.id.pressureTextView);
        humidityTextView= (TextView) findViewById(R.id.humidityTextView);
        nameText= (EditText) findViewById(R.id.nameText);
        check= (Button) findViewById(R.id.check);
    }


    public void getWeather(View view) {
        DownloadTask task=new DownloadTask();
        task.execute("https://api.openweathermap.org/data/2.5/weather?q="+nameText.getText().toString()+"&appid=60b2a6ddb0cad60c7bbe83f9f7972156&units=metric");
    }

    public class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
//            String result = "";
//            URL url;
//            HttpURLConnection connection = null;
//            try {
//                url = new URL(urls[0]);
//                connection = (HttpURLConnection) url.openConnection();
//                InputStream inputStream = connection.getInputStream();
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                int data = inputStreamReader.read();
//
//                while (data != -1) {
//                    char current = (char) data;
//                    result += current;
//                    data=inputStreamReader.read();
//                }
//                return result;
//            } catch (Exception e) {
//                e.printStackTrace();
//                return null;
//            }
            String object = "";
            URL url;
            HttpURLConnection connection = null;
            try {
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader((inputStream));
                int data = inputStreamReader.read();
                while(data!=-1) {
                    char append = (char) data;
                    object += append;
                    data = inputStreamReader.read();
                }
                return object;
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject=new JSONObject(s);
                JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);
                JSONObject main=jsonObject.getJSONObject("main");
                JSONObject wind=jsonObject.getJSONObject("wind");
                JSONObject sys=jsonObject.getJSONObject("sys");
                Long updatedAt = jsonObject.getLong("dt");
                String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt*1000));
                String temp = main.getString("temp") + "°C";
                String tempMin = "Min Temp: " + main.getString("temp_min") + "°C";
                String tempMax = "Max Temp: " + main.getString("temp_max") + "°C";
                String pressure = main.getString("pressure");
                String humidity = main.getString("humidity");
                Long sunrise = sys.getLong("sunrise");
                Date d = new Date(sunrise*1000);
                String sunr = ""+d;
                sunr = sunr.substring(11, 19);
                Long sunset = sys.getLong("sunset");
                String suns = (""+new Date(sunset*1000)).substring(11,19);
                String windSpeed = wind.getString("speed");
                String weatherDescription = weather.getString("description");
                String address = jsonObject.getString("name") + ", " + sys.getString("country");
                cityTextView.setText(address);
                updatedTextView.setText(updatedAtText);
                cloudTextView.setText(weatherDescription);
                tempTextView.setText(temp);
                mintempTextView.setText(tempMin);
                maxtempTextview.setText(tempMax);
                sunriseTextView.setText(sunr);
                sunsetTextView.setText(suns);
                windTextView.setText(String.valueOf(windSpeed));
                pressureTextView.setText(pressure);
                humidityTextView.setText(humidity);
            }catch (Exception e){
                cityTextView.setText("Not Valid");
                updatedTextView.setText("");
                cloudTextView.setText("");
                tempTextView.setText("");
                mintempTextView.setText("");
                maxtempTextview.setText("");
                sunriseTextView.setText("");
                sunsetTextView.setText("");
                windTextView.setText("");
                pressureTextView.setText("");
                humidityTextView.setText("");
                e.printStackTrace();
            }
        }
    }
}
