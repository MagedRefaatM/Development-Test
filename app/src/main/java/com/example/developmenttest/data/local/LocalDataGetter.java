package com.example.developmenttest.data.local;

import android.content.Context;

import com.example.developmenttest.R;
import com.example.developmenttest.presenter.Presenter;
import com.example.developmenttest.view.main.NotifyOnParsingComplete;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class LocalDataGetter {

    Presenter presenter = new Presenter();

    public LocalDataGetter() {
    }

    public void parsingJSON(Context context, NotifyOnParsingComplete onParsingComplete) {

        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.legend);

            int size = inputStream.available();
            byte[] buffer = new byte[size];

            inputStream.read(buffer);
            inputStream.close();

            String json = new String(buffer, "UTF-8");

            presenter.setRSRPItemsList(gettingValueObjectList(json, "RSRP"));
            presenter.setRSRQItemsList(gettingValueObjectList(json, "RSRQ"));
            presenter.setSINRItemsList(gettingValueObjectList(json, "SINR"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        onParsingComplete.parseComplete();
    }

    private ArrayList<HashMap<String, String>> gettingValueObjectList(String jsonString, String valueObjectName) {

        ArrayList<HashMap<String, String>> valueList = new ArrayList<>();

        HashMap<String, String> singleValueItem;

        String minimumValue, maximumValue, valueColor;

        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            JSONArray valueArray = jsonObject.getJSONArray(valueObjectName);

            switch (valueObjectName){
                case "RSRP": {
                    for (int counter = 0; counter < valueArray.length(); counter++) {

                        JSONObject jsonObjectInside = valueArray.getJSONObject(counter);

                        if (jsonObjectInside.getString("From").equals("Min")) {

                            minimumValue = "-140";
                            maximumValue = jsonObjectInside.getString("To");
                            valueColor = jsonObjectInside.getString("Color");

                            singleValueItem = new HashMap<>();
                            singleValueItem.put("from", minimumValue);
                            singleValueItem.put("to", maximumValue);
                            singleValueItem.put("color", valueColor);

                            valueList.add(singleValueItem);

                        } else if (jsonObjectInside.getString("To").equals("Max")) {

                            minimumValue = jsonObjectInside.getString("From");
                            maximumValue = "-60";
                            valueColor = jsonObjectInside.getString("Color");

                            singleValueItem = new HashMap<>();
                            singleValueItem.put("from", minimumValue);
                            singleValueItem.put("to", maximumValue);
                            singleValueItem.put("color", valueColor);

                            valueList.add(singleValueItem);

                        } else {
                            minimumValue = jsonObjectInside.getString("From");
                            maximumValue = jsonObjectInside.getString("To");
                            valueColor = jsonObjectInside.getString("Color");

                            singleValueItem = new HashMap<>();
                            singleValueItem.put("from", minimumValue);
                            singleValueItem.put("to", maximumValue);
                            singleValueItem.put("color", valueColor);

                            valueList.add(singleValueItem);
                        }
                    }
                }
                break;

                case "RSRQ": {
                    for (int counter = 0; counter < valueArray.length(); counter++) {

                        JSONObject jsonObjectInside = valueArray.getJSONObject(counter);

                        if (jsonObjectInside.getString("From").equals("Min")) {

                            minimumValue = "-30";
                            maximumValue = jsonObjectInside.getString("To");
                            valueColor = jsonObjectInside.getString("Color");

                            singleValueItem = new HashMap<>();
                            singleValueItem.put("from", minimumValue);
                            singleValueItem.put("to", maximumValue);
                            singleValueItem.put("color", valueColor);

                            valueList.add(singleValueItem);

                        } else if (jsonObjectInside.getString("To").equals("Max")) {

                            minimumValue = jsonObjectInside.getString("From");
                            maximumValue = "0";
                            valueColor = jsonObjectInside.getString("Color");

                            singleValueItem = new HashMap<>();
                            singleValueItem.put("from", minimumValue);
                            singleValueItem.put("to", maximumValue);
                            singleValueItem.put("color", valueColor);

                            valueList.add(singleValueItem);

                        } else {
                            minimumValue = jsonObjectInside.getString("From");
                            maximumValue = jsonObjectInside.getString("To");
                            valueColor = jsonObjectInside.getString("Color");

                            singleValueItem = new HashMap<>();
                            singleValueItem.put("from", minimumValue);
                            singleValueItem.put("to", maximumValue);
                            singleValueItem.put("color", valueColor);

                            valueList.add(singleValueItem);
                        }
                    }
                }
                break;

                case "SINR": {
                    for (int counter = 0; counter < valueArray.length(); counter++) {

                        JSONObject jsonObjectInside = valueArray.getJSONObject(counter);

                        if (jsonObjectInside.getString("From").equals("Min")) {

                            minimumValue = "-10";
                            maximumValue = jsonObjectInside.getString("To");
                            valueColor = jsonObjectInside.getString("Color");

                            singleValueItem = new HashMap<>();
                            singleValueItem.put("from", minimumValue);
                            singleValueItem.put("to", maximumValue);
                            singleValueItem.put("color", valueColor);

                            valueList.add(singleValueItem);

                        } else if (jsonObjectInside.getString("To").equals("Max")) {

                            minimumValue = jsonObjectInside.getString("From");
                            maximumValue = "30";
                            valueColor = jsonObjectInside.getString("Color");

                            singleValueItem = new HashMap<>();
                            singleValueItem.put("from", minimumValue);
                            singleValueItem.put("to", maximumValue);
                            singleValueItem.put("color", valueColor);

                            valueList.add(singleValueItem);

                        } else {
                            minimumValue = jsonObjectInside.getString("From");
                            maximumValue = jsonObjectInside.getString("To");
                            valueColor = jsonObjectInside.getString("Color");

                            singleValueItem = new HashMap<>();
                            singleValueItem.put("from", minimumValue);
                            singleValueItem.put("to", maximumValue);
                            singleValueItem.put("color", valueColor);

                            valueList.add(singleValueItem);
                        }
                    }
                }
                break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return valueList;
    }
}