package com.company;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.TreeMap;

public class API {


    private static final String sURL = "https://api.exchangeratesapi.io/latest?base="; //just a string

    public API() {
    }

    public static TreeMap<String,Double> getRatesFromAPI(String forCurrency) {
        try {
            // Connect to the URL using java's native library
            URL url = new URL(API.sURL + forCurrency);
            URLConnection request = url.openConnection();
            request.connect();

            // Convert to a JSON object to print data
            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
            JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object.
            JsonObject rates = rootobj.get("rates").getAsJsonObject(); // selecting the rates

            TreeMap<String,Double> ratesFromAPI = new TreeMap<String, Double>();

            for (Map.Entry<String, JsonElement> entry : rates.entrySet()) {
                ratesFromAPI.put(entry.getKey(),entry.getValue().getAsDouble());

            }
            return ratesFromAPI;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}

