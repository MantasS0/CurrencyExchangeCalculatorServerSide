package com.company;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

public class Currency extends Database {

    private String tableName;
    private String nameShort;
    private String nameLong;
    private TreeMap<String, ExchangeRate> exchangeRates = null;


    public Currency(String tableName, String nameShort, String nameLong, TreeMap<String, ExchangeRate> exchangeRates) {
        this.tableName = tableName;
        this.nameShort = nameShort;
        this.nameLong = nameLong;
        this.exchangeRates = exchangeRates;

    }

    @Override
    public void updateData() {
        for (Map.Entry<String, ExchangeRate> currency : this.exchangeRates.entrySet()) {
            //UPDATE `eur_rates` SET `sold_rate` = 5, `buy_rate` = 5 WHERE `eur_rates`.`currency` = "USD" AND `updated` < CURRENT_TIMESTAMP
            try {
                String insertQueryStatement = "UPDATE " + this.tableName + " SET `sold_rate` = ?, `buy_rate` = ? WHERE " + this.tableName + ".`currency` = ? AND `updated` < CURRENT_TIMESTAMP";
                dbPrepareStatement = dbConnection.prepareStatement(insertQueryStatement);
                dbPrepareStatement.setDouble(1, currency.getValue().getExchangeRate());
                dbPrepareStatement.setDouble(2, currency.getValue().getExchangeRate());
                dbPrepareStatement.setString(3, currency.getKey());
                dbPrepareStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void insertRate(Map.Entry<String, ExchangeRate> currency) {
        try {
            String insertQueryStatement = "INSERT INTO " + this.tableName + " (`currency`, `sold_rate`, `buy_rate`) VALUES (?, ?, ?)";
            dbPrepareStatement = dbConnection.prepareStatement(insertQueryStatement);
            dbPrepareStatement.setString(1, currency.getKey());
            dbPrepareStatement.setDouble(2, currency.getValue().getExchangeRate());
            dbPrepareStatement.setDouble(3, currency.getValue().getExchangeRate());
            dbPrepareStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createData() {

        try {
            String selectQueryStatement = "SELECT * from " + this.tableName;
            dbPrepareStatement = dbConnection.prepareStatement(selectQueryStatement);
            ResultSet results = dbPrepareStatement.executeQuery();


            if (results.getFetchSize() == 0) {
//                System.out.println("Cia pasieke");

//                String currencyName = results.getString("currency");
                for (Map.Entry<String, ExchangeRate> currency : this.exchangeRates.entrySet()) {
//                    if (!currency.getKey().equals(currencyName)) {

                            this.insertRate(currency);
//                    }
                }

            }

//            while (results.next()) {
//                /* Gauname rezultatus is duombazes ir issaugome i laikinus darbinius kintamuosius */
//                String currencyName = results.getString("currency");
//                for (Map.Entry<String, ExchangeRate> currency : this.exchangeRates.entrySet()) {
//                    if (!currency.getKey().equals(currencyName)) {
//
//                        try {
//                            String insertQueryStatement = "INSERT INTO " + this.tableName + " (`currency`, `sold_rate`, `buy_rate`) VALUES (?, ?, ?)";
//                            dbPrepareStatement = dbConnection.prepareStatement(insertQueryStatement);
//                            dbPrepareStatement.setString(1, currency.getKey());
//                            dbPrepareStatement.setDouble(2, currency.getValue().getExchangeRate());
//                            dbPrepareStatement.setDouble(3, currency.getValue().getExchangeRate());
//                            dbPrepareStatement.execute();
//
//                        } catch (SQLException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

//    public void getData() {
//        try {
//            String selectQueryStatement = "SELECT * from " + this.tableName;
//            dbPrepareStatement = dbConnection.prepareStatement(selectQueryStatement);
//            ResultSet results = dbPrepareStatement.executeQuery();
//
//            while (results.next()) {
//                /* Gauname rezultatus is duombazes ir issaugome i laikinus darbinius kintamuosius */
//                String currency = results.getString("currency");
//
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public TreeMap<String, ExchangeRate> getExchangeRates() {
        return exchangeRates;
    }

    public void setExchangeRates(TreeMap<String, ExchangeRate> exchangeRates) {
        this.exchangeRates = exchangeRates;
    }

    public String getTableName() {
        return tableName;
    }

    public String getNameShort() {
        return nameShort;
    }

    public String getNameLong() {
        return nameLong;
    }
}
