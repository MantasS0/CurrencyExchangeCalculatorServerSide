package com.company;

import javax.swing.*;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        Database.makeDBConnection();

//        TreeMap<String, ExchangeRate> eurExchangeMap = new TreeMap<String, ExchangeRate>();
//
//        ExchangeRate usdExchangeRate = new ExchangeRate("USD", 0.5);
//        eurExchangeMap.put("USD", usdExchangeRate);
//
//        ExchangeRate gbpExchangeRate = new ExchangeRate("GBP", 2.0);
//        eurExchangeMap.put("GBP", gbpExchangeRate);
//        Currency currencyEUR = new Currency("eur_rates", "EUR", "Euro", eurExchangeMap);
//        currencyEUR.createData();
//
//        usdExchangeRate.setExchangeRate(0.25);
//        currencyEUR.getExchangeRates().put("USD", usdExchangeRate);
//        currencyEUR.updateData();

        DAO.generateAllLocalResources();


    }

    private static void loadResources() {

    }


}
