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
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import com.skydoves.progressview.ProgressView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment {

    private ProgressView firstValueView;
    private ProgressView secondValueView;
    private ProgressView thirdValueView;

    private final Presenter presenter = new Presenter();

    private LineChart RSRPChart;
    private LineChart RSRQChart;
    private LineChart SINRChart;

    private Pusher pusher;

    private static final String PUSHER_APP_KEY = "<INSERT_PUSHER_KEY>";
    private static final String PUSHER_APP_CLUSTER = "<INSERT_PUSHER_CLUSTER>";
    private static final String CHANNEL_NAME = "stats";
    private static final String EVENT_NAME = "new_memory_stat";

    private static final float TOTAL_MEMORY = 16.0f;
    private static final float LIMIT_MAX_MEMORY = 12.0f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main , container ,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        housingBundleData();
//        prepareDataChart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pusher.disconnect();
    }

    private void housingBundleData () {

        Bundle bundle = getArguments();

        firstValueView = getView().findViewById(R.id.first_value_progress_view);
        firstValueView.getHighlightView().setColor(Color.parseColor(bundle.getString("progressOneColor")));
        firstValueView.setLabelText(bundle.getString("RSRP"));

        secondValueView = getView().findViewById(R.id.second_value_progress_view);
        secondValueView.getHighlightView().setColor(Color.parseColor(bundle.getString("progressTwoColor")));
        secondValueView.setLabelText(bundle.getString("RSRQ"));


        thirdValueView = getView().findViewById(R.id.third_value_progress_view);
        thirdValueView.getHighlightView().setColor(Color.parseColor(bundle.getString("progressThreeColor")));
        thirdValueView.setLabelText(bundle.getString("SINR"));

        updateTheValues();
    }

    private void prepareDataChart() {

        RSRPChart = getView().findViewById(R.id.first_data_chart);
        RSRQChart = getView().findViewById(R.id.second_data_chart);
        SINRChart = getView().findViewById(R.id.third_data_chart);

        setupCharts();
        setupAxesOfCharts();
        setupDataForCharts();
        setLegendForCharts();

        PusherOptions options = new PusherOptions();
        options.setCluster(PUSHER_APP_CLUSTER);
        pusher = new Pusher("PUSHER_APP_KEY",options);
        Channel channel = pusher.subscribe(CHANNEL_NAME);

        SubscriptionEventListener eventListener = (channel1, event, data) -> addEntry();

        channel.bind(EVENT_NAME, eventListener);
        pusher.connect();
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

    private void setupCharts() {

        RSRPChart.getDescription().setEnabled(false);
        RSRQChart.getDescription().setEnabled(false);
        SINRChart.getDescription().setEnabled(false);

        RSRPChart.setTouchEnabled(true);
        RSRQChart.setTouchEnabled(true);
        SINRChart.setTouchEnabled(true);

        RSRPChart.setPinchZoom(true);
        RSRQChart.setPinchZoom(true);
        SINRChart.setPinchZoom(true);

        RSRPChart.setScaleEnabled(true);
        RSRQChart.setScaleEnabled(true);
        SINRChart.setScaleEnabled(true);

        RSRPChart.setDrawGridBackground(false);
        RSRQChart.setDrawGridBackground(false);
        SINRChart.setDrawGridBackground(false);
    }

    private void setupAxesOfCharts() {
        XAxis xl = RSRPChart.getXAxis();
        XAxis x2 = RSRQChart.getXAxis();
        XAxis x3 = SINRChart.getXAxis();

        xl.setTextColor(Color.WHITE);
        x2.setTextColor(Color.WHITE);
        x3.setTextColor(Color.WHITE);

        xl.setDrawGridLines(false);
        x2.setDrawGridLines(false);
        x3.setDrawGridLines(false);

        xl.setAvoidFirstLastClipping(true);
        x2.setAvoidFirstLastClipping(true);
        x3.setAvoidFirstLastClipping(true);

        xl.setEnabled(true);
        x2.setEnabled(true);
        x3.setEnabled(true);

        YAxis leftAxis1 = RSRPChart.getAxisLeft();
        YAxis leftAxis2 = RSRQChart.getAxisLeft();
        YAxis leftAxis3 = SINRChart.getAxisLeft();

        leftAxis1.setTextColor(Color.WHITE);
        leftAxis2.setTextColor(Color.WHITE);
        leftAxis3.setTextColor(Color.WHITE);

        leftAxis1.setAxisMinimum(Integer.parseInt(presenter.getRSRPItemsList().get(0).get("from")));
        leftAxis2.setAxisMinimum(Integer.parseInt(presenter.getRSRQItemsList().get(0).get("from")));
        leftAxis3.setAxisMinimum(Integer.parseInt(presenter.getSINRItemsList().get(0).get("from")));

        leftAxis1.setAxisMaximum(Integer.parseInt(presenter.getRSRPItemsList().get(6).get("to")));
        leftAxis2.setAxisMaximum(Integer.parseInt(presenter.getRSRQItemsList().get(4).get("to")));
        leftAxis3.setAxisMaximum(Integer.parseInt(presenter.getSINRItemsList().get(7).get("to")));

        leftAxis1.setDrawGridLines(true);
        leftAxis2.setDrawGridLines(true);
        leftAxis3.setDrawGridLines(true);

        leftAxis1.setDrawLimitLinesBehindData(true);
        leftAxis2.setDrawLimitLinesBehindData(true);
        leftAxis3.setDrawLimitLinesBehindData(true);
    }

    private void setupDataForCharts() {
        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        RSRPChart.setData(data);
        RSRQChart.setData(data);
        SINRChart.setData(data);
    }

    private void setLegendForCharts() {

        Legend legend1 = RSRPChart.getLegend();
        Legend legend2 = RSRQChart.getLegend();
        Legend legend3 = SINRChart.getLegend();

        legend1.setForm(Legend.LegendForm.CIRCLE);
        legend2.setForm(Legend.LegendForm.CIRCLE);
        legend3.setForm(Legend.LegendForm.CIRCLE);

        legend1.setTextColor(Color.WHITE);
        legend2.setTextColor(Color.WHITE);
        legend3.setTextColor(Color.WHITE);
    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Memory Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColors(ColorTemplate.VORDIPLOM_COLORS[0]);
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(10f);

        return set;
    }

    private void addEntry() {
        LineData data = RSRPChart.getData();

        if (data != null) {
            ILineDataSet set = data.getDataSetByIndex(0);

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            data.addEntry(new Entry(set.getEntryCount(), (float) presenter.getRSRP()), 0);

            // let the chart know it's data has changed
            data.notifyDataChanged();
            RSRPChart.notifyDataSetChanged();

            // limit the number of visible entries
            RSRPChart.setVisibleXRangeMaximum(15);

            // move to the latest entry
            RSRPChart.moveViewToX(data.getEntryCount());
        }
    }
}