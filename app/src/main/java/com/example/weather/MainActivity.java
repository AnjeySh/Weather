package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText edField;
    private Button startB;
    private TextView finTV;
    private TextView flTV;
    private TextView pressTV;
    private ImageView pic;
    private TextView hotTV;

    private TextView humTV;
    private TextView descriptionTV;
    private TextView windSpeedTV;
    private TextView windDirectionTV;

    private String lon, lat;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edField = findViewById(R.id.etField_1);
        startB = findViewById(R.id.bStart);
        finTV = findViewById(R.id.tvBot);
        flTV = findViewById(R.id.tvFlLike);
        pressTV = findViewById(R.id.tvPress);
        humTV = findViewById(R.id.tvHumidity);
        pic = findViewById(R.id.imageKing);
        hotTV = findViewById(R.id.tvHOT);
        descriptionTV = findViewById(R.id.tvDescription);
        windSpeedTV = findViewById(R.id.tvWindSpeed);
        windDirectionTV = findViewById(R.id.tvWindDerect);

        startB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edField.getText().toString().trim().equals("")){
                    Toast.makeText(MainActivity.this, R.string.noCity, Toast.LENGTH_SHORT).show();
                } else {
                    String city = edField.getText().toString();
                    String key = "178fc7586357ee374ed24e382b03df3f";
                    String url = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+key+"&units=metric&lang=ru";
                    new getURLData().execute(url);
                }
            }
        });
    }
    private class getURLData extends AsyncTask<String,String,String>{
        protected void onPreExecute(){
            super.onPreExecute();
            finTV.setText(R.string.wait);
        }
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line=reader.readLine())!=null){
                    buffer.append(line).append("\n");
                }
                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connection!=null)
                    connection.disconnect();
                try {
                    if(reader!=null)
                        reader.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
            return null;
        }
        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            try {
                JSONObject obj = new JSONObject(result);
                JSONArray objArr = obj.getJSONArray("weather");
                finTV.setText(" "+obj.getJSONObject("main").getDouble("temp"));
                flTV.setText("  "+obj.getJSONObject("main").getDouble("feels_like"));
                pressTV.setText("   "+obj.getJSONObject("main").getDouble("pressure"));
                humTV.setText("   "+obj.getJSONObject("main").getDouble("humidity")+" %");
                windSpeedTV.setText("   "+obj.getJSONObject("wind").getDouble("speed")+" м/с");
                windDirectionTV.setText("   "+obj.getJSONObject("wind").getDouble("deg"));
                JSONObject jsObj = objArr.getJSONObject(0);
                descriptionTV.setText(" "+jsObj.getString("description"));
                if(((int) obj.getJSONObject("main").getDouble("temp"))>20){
                    pic.setVisibility(View.VISIBLE);
                    hotTV.setVisibility(View.VISIBLE);
                } else {
                    pic.setVisibility(View.INVISIBLE);
                    hotTV.setVisibility(View.INVISIBLE);
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }
    }
}