package com.company;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class DAO {

    private static String[] tableNames = {"eur_rates", "usd_rates", "gbp_rates", "transactions"};
    private static HashMap<String, String> currencyLongNames = new HashMap<String, String>() {{
        put("USD", "United States Dollar");
        put("EUR", "Euro");
        put("GBP", "British Pound");
    }};


    private static HashMap<String, Double> ratesForEUR = new HashMap<String, Double>() {{
        put("USD", 1.5);
        put("GBP", 0.5);
    }};

    private static HashMap<String, Double> ratesForGBP = new HashMap<String, Double>() {{
        put("USD", 3.0);
        put("EUR", 1.5);
    }};

    private static HashMap<String, Double> ratesForUSD = new HashMap<String, Double>() {{
        put("EUR", 0.5);
        put("GBP", 0.25);
    }};

    private static HashMap<String, HashMap<String, Double>> currencyRates = new HashMap<String, HashMap<String, Double>>() {{
        put("USD", DAO.ratesForUSD);
        put("GBP", DAO.ratesForGBP);
        put("EUR", DAO.ratesForEUR);
    }};

    public DAO() {
    }

    public static String[] getTableNames() {
        return tableNames;
    }

    public static HashMap<String, String> getCurrencyLongNames() {
        return currencyLongNames;
    }

    public static void generateAllLocalResources() {

        for (int i = 0; i < tableNames.length; i++) {
            if (tableNames[i].contains("_rates")) {
                String shortName = tableNames[i].substring(0, 3).toUpperCase();
                Currency currency = createLocalCurrencyResource(tableNames[i], shortName);

                if (currency != null) {
                    Database.getLocalCurrencyMap().put(shortName, currency);

                    currency.createData();
                }
            }
        }
    }

    public static Currency createLocalCurrencyResource(String tableName, String currencyName) {
        TreeMap<String, ExchangeRate> currencyExchangeMap = new TreeMap<String, ExchangeRate>();

        if (currencyRates.get(currencyName) != null) {

            for (Map.Entry<String, Double> currency : currencyRates.get(currencyName).entrySet()) {

                ExchangeRate currencyExchangeRate = new ExchangeRate(currency.getKey(), currency.getValue());
                currencyExchangeMap.put(currencyName, currencyExchangeRate);

            }
            Currency currency = new Currency(tableName, currencyName, currencyLongNames.get(currencyName), currencyExchangeMap);

            return currency;
        }
        return null;
    }

/*
 *  REDUNDANT.
    private HashMap<String,Double> getRates(String forCurrency){
        return DAO.currencyRates.get(forCurrency);
    }
*/

}
