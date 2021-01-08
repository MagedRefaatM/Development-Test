package com.example.developmenttest.model.network;

import com.example.developmenttest.model.entities.Random;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetDataService {

    @GET("random")
    Call<Random> getRandoms();
}