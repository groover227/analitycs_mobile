package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    String myResponse;

    AnyChartView anyChartView;

    String[] months = {"Мужчин", "Женщин"};
    int[] earrings = {60, 40};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView mTextViewResult = findViewById(R.id.textView);

        OkHttpClient client = new OkHttpClient();

        String url = "http://10.0.2.2:5000/api";

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    myResponse = response.body().string();

                    JsonObject jsonObject = new JsonParser().parse(myResponse).getAsJsonObject();
                    JsonObject clients_data = jsonObject.getAsJsonObject("clients_data");
                    JsonObject percentage_data = clients_data.getAsJsonObject("percentage_data");

                    Integer male_percantage= percentage_data.get("male_percantage").getAsInt();
                    Integer female_percentage = percentage_data.get("female_percantage").getAsInt();

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTextViewResult.setText("Мужчин (%): " + male_percantage + "   Женщин(%): " + female_percentage);
                        }
                    });

                }
            }
        });


        anyChartView = findViewById(R.id.any_chart_view);

        setupPieChart();
    }

    public void setupPieChart() {
        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();

        for (int i=0; i < months.length; i++) {
            dataEntries.add(new ValueDataEntry(months[i], earrings[i]));
        }

        pie.data(dataEntries);
        anyChartView.setChart(pie);

    }
}