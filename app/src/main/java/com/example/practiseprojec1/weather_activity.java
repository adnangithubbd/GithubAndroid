package com.example.practiseprojec1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.practiseprojec1.databinding.ActivityWeatherBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class weather_activity extends AppCompatActivity {
    ActivityWeatherBinding binding;
    String _location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityWeatherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _location=binding.search.getText().toString();
                if(_location.length()!=0){
                  try{
                      showData(_location);
                      Toast.makeText(weather_activity.this, _location, Toast.LENGTH_SHORT).show();
                      InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                      imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                  }catch (Exception e){
                      Log.d("issu",e.getMessage());
                  }
                }else{
                     binding.search.setError("Please write some data... ");
                }
                //showData(_location);
            }
        });



    }

    private void showData( String _location) {

        RequestQueue queue = Volley.newRequestQueue(weather_activity.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+_location+"&appid=0e42eac3e750d240d274ec0021553be0";



        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray object=jsonResponse.getJSONArray("weather");
                            JSONObject weatherObject = object.getJSONObject(0);
                            JSONObject main=jsonResponse.getJSONObject("main");
                            double temp_min=main.getDouble("temp_min");
                            double temp_max=main.getDouble("temp_max");
                            binding.temp.setText("Max "+temp_max+"K\n"+"Min "+temp_min+"K");
                            double humidity=main.getDouble("humidity");
                            binding.humidity.setText("Humidity "+humidity);
                            double sea_level=main.getDouble("sea_level");
                            binding.seaLevel.setText(""+sea_level);

                            JSONObject windObj=jsonResponse.getJSONObject("wind");
                            double wind_speed=windObj.getDouble("speed");
                            binding.windSpeed.setText(""+wind_speed);

                            JSONObject sun=jsonResponse.getJSONObject("sys");
                            String country=sun.getString("country");
                            String sub_location=jsonResponse.getString("name");
                            binding.location.setText(sub_location+" , "+country);
                            double sunrise=sun.getDouble("sunrise");
                            double sunset=sun.getDouble("sunset");
                            long stime=(long) sunrise;
                            long etime=(long) sunset;
                            Date date = new Date(stime * 1000);
                            Date date2 = new Date(etime * 1000);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy - MM - dd \n HH:mm:ss\n");
                            String formattedDate = sdf.format(date);
                            String formattedDate2 = sdf.format(date2);
                            binding.sunRise.setText(""+formattedDate);
                            binding.sunSet.setText(""+formattedDate2);
                            long ts=System.currentTimeMillis();

                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd \n HH:mm:ss");
                            Date date12 = new Date();
                            binding.timeDate.setText("Today\n"+dateFormat.format(date12));

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);
                            int day_hour = calendar.get(Calendar.HOUR_OF_DAY);
                            calendar.setTime(date2);
                            int night_hour=calendar.get(Calendar.HOUR_OF_DAY);
                            calendar.setTime(date12);
                            int current=calendar.get(Calendar.HOUR_OF_DAY);

                            if(current>=night_hour&&current<day_hour){
                                binding.mainLayout.setBackgroundResource(R.drawable.night_bg);
                           }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("report","That didn't work!");
            }
        });

        queue.add(stringRequest);
    }
}