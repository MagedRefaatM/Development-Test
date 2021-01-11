package com.example.developmenttest.data.network;

import android.content.Context;

import com.example.developmenttest.data.local.LocalDataGetter;
import com.example.developmenttest.model.entities.Random;
import com.example.developmenttest.model.network.GetDataService;
import com.example.developmenttest.model.network.RetrofitClientInstance;
import com.example.developmenttest.presenter.Presenter;

import com.example.developmenttest.view.main.NotifyOnCallingFailed;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkDataGetter {

    public NetworkDataGetter(Context context, Presenter presenter, NotifyOnCallingFailed notifyOnCallingFailed) {

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
                dataGetter.parsingJSON(context, presenter.getOnParsingComplete());
            }

            @Override
            public void onFailure(Call<Random> call, Throwable t) {
                presenter.checkCallingFailureState(true, notifyOnCallingFailed, t.getLocalizedMessage());
            }
        });
    }
}