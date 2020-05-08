package com.example.covid_19tracker;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView tvCases,tvRecovered,tvDeaths,tvTodayDeaths,tvTodayCases,tvAffectedCountries,tvActive;
    ScrollView scrollView;
    PieChart pc;
    SimpleArcLoader sal;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.navigation_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId())
        {
            case R.id.prevention:
                startActivity(new Intent(getApplicationContext(),prevention.class));
                break;
            case R.id.faqs:
                startActivity(new Intent(getApplicationContext(),faq.class));
                break;
            default:return false;
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvCases=(TextView)findViewById(R.id.tvCases);
        tvRecovered=(TextView)findViewById(R.id.tvRecovered);
        tvDeaths=(TextView)findViewById(R.id.tvDeaths);
        tvTodayCases=(TextView)findViewById(R.id.tvNewCases);
        tvActive=(TextView)findViewById(R.id.tvActiveCases);
        tvAffectedCountries=(TextView)findViewById(R.id.tvAffectedCountries);
        tvTodayDeaths=(TextView)findViewById(R.id.tvDeathsToday);

        scrollView=findViewById(R.id.scrollStats);
        pc=(PieChart)findViewById(R.id.piechart);
        sal=findViewById(R.id.loader);

        fecthData();
    }

    private void fecthData() {
        String url="https://corona.lmao.ninja/V2/all";
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.toString());
                    tvCases.setText(jsonObject.getString("cases"));
                    tvActive.setText(jsonObject.getString("active"));
                    tvDeaths.setText(jsonObject.getString("deaths"));
                    tvAffectedCountries.setText(jsonObject.getString("affectedCountries"));
                    tvTodayCases.setText(jsonObject.getString("todayCases"));
                    tvRecovered.setText(jsonObject.getString("recovered"));
                    tvTodayDeaths.setText(jsonObject.getString("todayDeaths"));
                    //tvCases.setText(jsonObject.getString("cases"));

                    pc.addPieSlice(new PieModel("Cases",Integer.parseInt(tvCases.getText().toString()), Color.parseColor("#FFA720")));
                    pc.addPieSlice(new PieModel("Active Cases",Integer.parseInt(tvActive.getText().toString()), Color.parseColor("#2986F6")));
                    pc.addPieSlice(new PieModel("Recovered",Integer.parseInt(tvRecovered.getText().toString()), Color.parseColor("#66BB6A")));
                    pc.addPieSlice(new PieModel("Deaths",Integer.parseInt(tvDeaths.getText().toString()), Color.parseColor("#EF5350")));

                    sal.stop();
                    sal.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    sal.stop();
                    sal.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    public void goTrackIndia(View view) {
        startActivity(new Intent(getApplicationContext(),IndiaStats.class));
    }
}
