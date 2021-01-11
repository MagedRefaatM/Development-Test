package com.example.developmenttest.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.developmenttest.R;
import com.example.developmenttest.model.entities.Random;
import com.example.developmenttest.model.network.GetDataService;
import com.example.developmenttest.model.network.RetrofitClientInstance;
import com.example.developmenttest.presenter.Presenter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.skydoves.progressview.ProgressView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment {

    private ProgressView firstValueView;
    private ProgressView secondValueView;
    private ProgressView thirdValueView;

    private final Presenter presenter = new Presenter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main , container ,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        housingBundleData();
        drawCharts();
    }

    private void housingBundleData () {

        Bundle bundle = getArguments();

        firstValueView = getView().findViewById(R.id.first_value_progress_view);
        firstValueView.getHighlightView().setColor(Color.parseColor(bundle.getString(getString(R.string.progress_one_color))));
        firstValueView.setLabelText(bundle.getString(getString(R.string.first_value)));

        secondValueView = getView().findViewById(R.id.second_value_progress_view);
        secondValueView.getHighlightView().setColor(Color.parseColor(bundle.getString(getString(R.string.progress_two_color))));
        secondValueView.setLabelText(bundle.getString(getString(R.string.second_value)));


        thirdValueView = getView().findViewById(R.id.third_value_progress_view);
        thirdValueView.getHighlightView().setColor(Color.parseColor(bundle.getString(getString(R.string.progress_three_color))));
        thirdValueView.setLabelText(bundle.getString(getString(R.string.third_value)));

        updateTheValues();
    }

    private void drawCharts() {
        LineChart RSRPChart = getView().findViewById(R.id.first_data_chart);
        LineChart RSRQChart = getView().findViewById(R.id.second_data_chart);
        LineChart SINRChart = getView().findViewById(R.id.third_data_chart);

        List<Entry> lineEntries1 = getDataSet();
        List<Entry> lineEntries2 = getDataSet();
        List<Entry> lineEntries3 = getDataSet();

        setupChart(RSRPChart, prepareLineDataSets(lineEntries1, getString(R.string.first_chart_line1), getString(R.string.first_chart_line2), getString(R.string.first_chart_line3)));
        setupChart(RSRQChart, prepareLineDataSets(lineEntries2, getString(R.string.second_chart_line1), getString(R.string.second_chart_line2), getString(R.string.second_chart_line3)));
        setupChart(SINRChart, prepareLineDataSets(lineEntries3, getString(R.string.third_chart_line1), getString(R.string.third_chart_line2), getString(R.string.third_chart_line3)));
    }

    private LineData prepareLineDataSets(List<Entry> lineEntries, String lineName1, String lineName2, String lineName3) {
        List<ILineDataSet> dataSets = new ArrayList<>();

        LineDataSet P = new LineDataSet(lineEntries, lineName1);
        LineDataSet S1 = new LineDataSet(lineEntries, lineName2);
        LineDataSet S2 = new LineDataSet(lineEntries, lineName3);

        setupSingleLineDataSet(P, getResources().getColor(R.color.petroleum, getContext().getTheme()));
        setupSingleLineDataSet(S1, Color.RED);
        setupSingleLineDataSet(S2, Color.GREEN);

        dataSets.add(P);
        dataSets.add(S1);
        dataSets.add(S2);

        return new LineData(dataSets);
    }

    private void setupSingleLineDataSet(LineDataSet dataSet, Integer color) {
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setHighlightEnabled(false);
        dataSet.setLineWidth(2);
        dataSet.setColor(color);
        dataSet.setCircleColor(color);
        dataSet.setCircleRadius(6);
        dataSet.setDrawValues(false);
        dataSet.setCircleHoleRadius(3);
        dataSet.setDrawHighlightIndicators(false);
    }

    private void setupChart(LineChart chart, LineData lineData){
        chart.setDrawMarkers(false);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.animateY(1000);
        chart.getXAxis().setGranularity(1.0f);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisRight().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setData(lineData);
    }

    private List<Entry> getDataSet() {
        List<Entry> lineEntries = new ArrayList<>();
        lineEntries.add(new Entry());
        lineEntries.add(new Entry((float) 22.28, -140));
        lineEntries.add(new Entry((float) 22.38, -120));
        lineEntries.add(new Entry((float) 22.48, -100));
        lineEntries.add(new Entry((float) 22.58, -80));
        lineEntries.add(new Entry((float) 33.08, -60));
        return lineEntries;
    }

    private void updateTheValues() {
        final Handler handler = new Handler();
        final int delay = 2000;

        handler.postDelayed(new Runnable() {
            public void run() {
                getRandomFromAPI();
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    private void getRandomFromAPI() {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        Call<Random> call = service.getRandoms();

        call.enqueue(new Callback<Random>() {
            @Override
            public void onResponse(Call<Random> call, Response<Random> response) {
                Random apiRandom = response.body();

                firstValueView.setLabelText(String.valueOf(apiRandom.getFirstValue()));
                firstValueView.getHighlightView().setColor(Color.parseColor(presenter.getFirstProgressColor(apiRandom.getFirstValue())));

                secondValueView.setLabelText(String.valueOf(apiRandom.getSecondValue()));
                secondValueView.getHighlightView().setColor(Color.parseColor(presenter.getFirstProgressColor(apiRandom.getSecondValue())));

                thirdValueView.setLabelText(String.valueOf(apiRandom.getThirdValue()));
                thirdValueView.getHighlightView().setColor(Color.parseColor(presenter.getSecondProgressColor(apiRandom.getThirdValue())));
            }

            @Override
            public void onFailure(Call<Random> call, Throwable t) {
                Log.e("Retrofit Error", t.getLocalizedMessage());
                Toast.makeText(getContext() , R.string.error , Toast.LENGTH_SHORT).show();
            }
        });
    }
}