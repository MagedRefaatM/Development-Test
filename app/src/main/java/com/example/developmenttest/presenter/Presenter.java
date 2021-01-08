package com.example.developmenttest.presenter;

import com.example.developmenttest.view.main.NotifyOnParsingComplete;

import java.util.ArrayList;
import java.util.HashMap;

public class Presenter {

    private double RSRP;
    private double RSRQ;
    private double SINR;

    private static ArrayList<HashMap<String, String>> RSRPItemsList;
    private static ArrayList<HashMap<String, String>> RSRQItemsList;
    private static ArrayList<HashMap<String, String>> SINRItemsList;

    private String progressColor;

    private NotifyOnParsingComplete onParsingComplete;

    public Presenter(NotifyOnParsingComplete onParsingComplete) {
        setOnParsingComplete(onParsingComplete);
    }

    public Presenter() {
    }

    public void setRSRP(double RSRP) {
        this.RSRP = RSRP;
    }

    public void setRSRQ(double RSRQ) {
        this.RSRQ = RSRQ;
    }

    public void setSINR(double SINR) {
        this.SINR = SINR;
    }

    public void setRSRPItemsList(ArrayList<HashMap<String, String>> firstItemsList) {
        RSRPItemsList = firstItemsList;
    }

    public void setRSRQItemsList(ArrayList<HashMap<String, String>> secondItemsList) {
        RSRQItemsList = secondItemsList;
    }

    public void setSINRItemsList(ArrayList<HashMap<String, String>> thirdItemsList) {
        SINRItemsList = thirdItemsList;
    }

    public void setOnParsingComplete(NotifyOnParsingComplete onParsingComplete) {
        this.onParsingComplete = onParsingComplete;
    }

    public double getRSRP() {
        return RSRP;
    }

    public double getRSRQ() {
        return RSRQ;
    }

    public double getSINR() {
        return SINR;
    }

    public ArrayList<HashMap<String, String>> getRSRPItemsList() {
        return RSRPItemsList;
    }

    public ArrayList<HashMap<String, String>> getRSRQItemsList() {
        return RSRQItemsList;
    }

    public ArrayList<HashMap<String, String>> getSINRItemsList() {
        return SINRItemsList;
    }

    public NotifyOnParsingComplete getOnParsingComplete() {
        return onParsingComplete;
    }

    public String getFirstProgressColor(double value){
        for (int innerCounter=0; innerCounter<= getRSRPItemsList().size()-1; innerCounter++){
            if (getRSRPItemsList().get(innerCounter).get("from").equals("Min")){
                if (value <= Double.parseDouble(getRSRPItemsList().get(innerCounter).get("to")))
                    progressColor = getRSRPItemsList().get(innerCounter).get("color");
            } else if (getRSRPItemsList().get(innerCounter).get("to").equals("Max")){
                if (value >= Double.parseDouble(getRSRPItemsList().get(innerCounter).get("from")))
                    progressColor = getRSRPItemsList().get(innerCounter).get("color");
            } else {
                if (value >= Double.parseDouble(getRSRPItemsList().get(innerCounter).get("from")) && value <= Double.parseDouble(getRSRPItemsList().get(innerCounter).get("to")))
                    progressColor = getRSRPItemsList().get(innerCounter).get("color");
            }
        }
        return progressColor;
    }

    public String getSecondProgressColor(double value){
        for (int innerCounter=0; innerCounter<= getRSRQItemsList().size()-1; innerCounter++){
            if (getRSRQItemsList().get(innerCounter).get("from").equals("Min")){
                if (value <= Double.parseDouble(getRSRQItemsList().get(innerCounter).get("to")))
                    progressColor = getRSRQItemsList().get(innerCounter).get("color");
            } else if (getRSRQItemsList().get(innerCounter).get("to").equals("Max")){
                if (value >= Double.parseDouble(getRSRQItemsList().get(innerCounter).get("from")))
                    progressColor = getRSRQItemsList().get(innerCounter).get("color");
            } else {
                if (value >= Double.parseDouble(getRSRQItemsList().get(innerCounter).get("from")) && value <= Double.parseDouble(getRSRQItemsList().get(innerCounter).get("to")))
                    progressColor = getRSRQItemsList().get(innerCounter).get("color");
            }
        }
        return progressColor;
    }

    public String getThirdProgressColor(double value){
        for (int innerCounter=0; innerCounter<= getSINRItemsList().size()-1; innerCounter++){
            if (getSINRItemsList().get(innerCounter).get("from").equals("Min")){
                if (value <= Double.parseDouble(getSINRItemsList().get(innerCounter).get("to")))
                    progressColor = getSINRItemsList().get(innerCounter).get("color");
            } else if (getSINRItemsList().get(innerCounter).get("to").equals("Max")){
                if (value >= Double.parseDouble(getSINRItemsList().get(innerCounter).get("from")))
                    progressColor = getSINRItemsList().get(innerCounter).get("color");
            } else {
                if (value >= Double.parseDouble(getSINRItemsList().get(innerCounter).get("from")) && value <= Double.parseDouble(getSINRItemsList().get(innerCounter).get("to")))
                    progressColor = getSINRItemsList().get(innerCounter).get("color");
            }
        }
        return progressColor;
    }
}