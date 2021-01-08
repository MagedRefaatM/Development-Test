package com.example.developmenttest.model.entities;

import com.google.gson.annotations.SerializedName;

public class Random {
    @SerializedName("RSRP")
    private short firstValue;

    @SerializedName("RSRQ")
    private short secondValue;

    @SerializedName("SINR")
    private short thirdValue;

    public short getFirstValue() {
        return firstValue;
    }

    public short getSecondValue() {
        return secondValue;
    }

    public short getThirdValue() {
        return thirdValue;
    }
}
