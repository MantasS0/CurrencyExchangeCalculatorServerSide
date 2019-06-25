package com.company;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class DAO extends Database {

    private static ArrayList<String> tableNames = new ArrayList<String>();

    public DAO() {
    }

    public static void setTableNames(){
        TreeMap<String,Double> ratesFromAPI = new TreeMap<String, Double>();
        ratesFromAPI = API.getRatesFromAPI("EUR");
        for (Map.Entry<String, Double> entry : ratesFromAPI.entrySet()) {
            String name = entry.getKey().toLowerCase() + "_rates";
            getTableNames().add(name);
        }

        getTableNames().add("eur_rates");
        getTableNames().add("transactions");

    }

    public static ArrayList<String> getTableNames() {
        return tableNames;
    }


    public static void updateTableValues() {
        try {
            DatabaseMetaData metadata = dbConnection.getMetaData();
            ResultSet rs = metadata.getTables("exchange_rate_db", null, null, new String[]{"TABLE", "VIEW"});
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                if (tableName.contains("_rates")) {
                    System.out.println("Updating " + tableName + " values...");

                    String tableCurrency = tableName.substring(0, 3).toUpperCase();
                    TreeMap<String, Double> ratesFromAPI = API.getRatesFromAPI(tableCurrency);

                    for (Map.Entry<String, Double> entry : ratesFromAPI.entrySet()) {
                        if (!entry.getKey().equals(tableCurrency)) {
                            try {                                                                                                                           //WHERE DATE_ADD(NOW(), INTERVAL 2 HOUR) > start_time
                                String insertQueryStatement = "UPDATE " + tableName + " SET `sold_rate` = ?, `buy_rate` = ? WHERE " + tableName + ".`currency` = ?";
                                dbPrepareStatement = dbConnection.prepareStatement(insertQueryStatement);                                                       // AND `updated` < DATE_ADD(NOW(), INTERVAL 4 HOUR)
                                dbPrepareStatement.setDouble(1, entry.getValue());
                                dbPrepareStatement.setDouble(2, entry.getValue());
                                dbPrepareStatement.setString(3, entry.getKey());
                                dbPrepareStatement.executeUpdate();

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertRates() {

        try {
            DatabaseMetaData metadata = dbConnection.getMetaData();
            ResultSet rs = metadata.getTables("exchange_rate_db", null, null, new String[]{"TABLE", "VIEW"});
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                if (tableName.contains("_rates")) {
                    System.out.println("Creating new values in " + tableName);

                    String tableCurrency = tableName.substring(0, 3).toUpperCase();
                    TreeMap<String, Double> ratesFromAPI = API.getRatesFromAPI(tableCurrency);

                    for (Map.Entry<String, Double> entry : ratesFromAPI.entrySet()) {
                        if (!entry.getKey().equals(tableCurrency)) {
                            try {
                                String insertQueryStatement = "INSERT INTO " + tableName + " (`currency`, `sold_rate`, `buy_rate`) VALUES (?, ?, ?)";
                                dbPrepareStatement = dbConnection.prepareStatement(insertQueryStatement);
                                dbPrepareStatement.setString(1, entry.getKey());
                                dbPrepareStatement.setDouble(2, entry.getValue());
                                dbPrepareStatement.setDouble(3, entry.getValue());
                                dbPrepareStatement.execute();

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertRates(String tableName) {
                if (tableName.contains("_rates")) {
                    System.out.println("Creating new values in " + tableName);
                    String tableCurrency = tableName.substring(0, 3).toUpperCase();
                    TreeMap<String, Double> ratesFromAPI = API.getRatesFromAPI(tableCurrency);

                    for (Map.Entry<String, Double> entry : ratesFromAPI.entrySet()) {
                        if (!entry.getKey().equals(tableCurrency)) {
                            try {
                                String insertQueryStatement = "INSERT INTO " + tableName + " (`currency`, `sold_rate`, `buy_rate`) VALUES (?, ?, ?)";
                                dbPrepareStatement = dbConnection.prepareStatement(insertQueryStatement);
                                dbPrepareStatement.setString(1, entry.getKey());
                                dbPrepareStatement.setDouble(2, entry.getValue());
                                dbPrepareStatement.setDouble(3, entry.getValue());
                                dbPrepareStatement.execute();

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
    }


}
