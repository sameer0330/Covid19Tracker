package com.example.covid_19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
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

public class IndiaStats extends AppCompatActivity {
    TextView tvCases,tvDeaths,tvActive,tvRecovered,tvCritical,tvNewCAse,tvDeathstoday,tvcasepermil,tvdeathspermil,tvtests,tvtestspermil;
    ScrollView scrollView;
    PieChart pc;
    SimpleArcLoader sal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_india_stats);
        getSupportActionBar().setTitle("IndiaStats");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tvCases=(TextView)findViewById(R.id.tvCases);
        tvRecovered=(TextView)findViewById(R.id.tvRecovered);
        tvDeaths=(TextView)findViewById(R.id.tvDeaths);
        tvNewCAse=(TextView)findViewById(R.id.tvNewCases);
        tvActive=(TextView)findViewById(R.id.tvActiveCases);
        tvDeathstoday=(TextView)findViewById(R.id.tvDeathsToday);
        tvCritical=findViewById(R.id.tvCritical);
        tvcasepermil=findViewById(R.id.tvCasesPerMil);
        tvdeathspermil=findViewById(R.id.tvDeathsPerMil);
        tvtests=findViewById(R.id.tvTests);
        tvtestspermil=findViewById(R.id.tvTestsPerMil);

        scrollView=findViewById(R.id.scrollStats);
        pc=(PieChart)findViewById(R.id.piechart);
        sal=findViewById(R.id.loader);

        fetch();
    }

    private void fetch() {
        String url="https://corona.lmao.ninja/V2/countries/India";
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.toString());
                    tvCases.setText(jsonObject.getString("cases"));
                    tvActive.setText(jsonObject.getString("active"));
                    tvDeaths.setText(jsonObject.getString("deaths"));
                    tvNewCAse.setText(jsonObject.getString("todayCases"));
                    tvRecovered.setText(jsonObject.getString("recovered"));
                    tvDeathstoday.setText(jsonObject.getString("todayDeaths"));
                    tvCritical.setText(jsonObject.getString("critical"));
                    tvtests.setText(jsonObject.getString("tests"));
                    tvtestspermil.setText(jsonObject.getString("testsPerOneMillion"));
                    tvcasepermil.setText(jsonObject.getString("casesPerOneMillion"));
                    tvdeathspermil.setText(jsonObject.getString("deathsPerOneMillion"));

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

}
