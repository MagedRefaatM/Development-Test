package com.example.developmenttest.view.main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.developmenttest.R;
import com.example.developmenttest.data.LocalDataGetter;
import com.example.developmenttest.model.entities.Random;
import com.example.developmenttest.model.network.GetDataService;
import com.example.developmenttest.model.network.RetrofitClientInstance;
import com.example.developmenttest.presenter.Presenter;
import com.example.developmenttest.view.fragment.MainFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NotifyOnParsingComplete {

    Presenter presenter;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog = ProgressDialog.show(MainActivity.this,
                getString(R.string.dialog_title),
                getString(R.string.dialog_message),
                true);

        presenter = new Presenter(this);

        getRandomFromAPI();
    }

    private void getRandomFromAPI() {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        Call<Random> call = service.getRandoms();

        call.enqueue(new Callback<Random>() {
            @Override
            public void onResponse(Call<Random> call, Response<Random> response) {
                Random apiRandom = response.body();

                presenter.setRSRP(apiRandom.getFirstValue());
                presenter.setRSRQ(apiRandom.getSecondValue());
                presenter.setSINR(apiRandom.getThirdValue());

                LocalDataGetter dataGetter = new LocalDataGetter();
                dataGetter.parsingJSON(MainActivity.this, presenter.getOnParsingComplete());
            }

            @Override
            public void onFailure(Call<Random> call, Throwable t) {
                dialog.dismiss();
                Log.e("Retrofit Error", t.getLocalizedMessage());
                Toast.makeText(MainActivity.this , R.string.error , Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void parseComplete() {
        dialog.dismiss();
        transitionToFragment();
    }

    private void transitionToFragment() {
        MainFragment mainFragment = new MainFragment();

        Bundle bundle = new Bundle();

        bundle.putString("RSRP" , String.valueOf(presenter.getRSRP()));
        bundle.putString("RSRQ" , String.valueOf(presenter.getRSRQ()));
        bundle.putString("SINR" , String.valueOf(presenter.getSINR()));

        bundle.putString("progressOneColor", presenter.getFirstProgressColor(presenter.getRSRP()));
        bundle.putString("progressTwoColor", presenter.getSecondProgressColor(presenter.getRSRQ()));
        bundle.putString("progressThreeColor", presenter.getThirdProgressColor(presenter.getSINR()));

        mainFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_host, mainFragment).commit();
    }
}