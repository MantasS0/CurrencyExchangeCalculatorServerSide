package com.company;

import java.sql.*;
import java.util.ArrayList;
import java.util.Currency;
import java.util.TreeMap;

public class Database {


    protected static Connection dbConnection = null; // prisijungimas prie duombazes
    protected static PreparedStatement dbPrepareStatement = null; // uzklausu siuntimui
    protected static Statement dbStatement = null;

    private static String dbUser = "root";
    private static String dbPassword = "";
    private static String dbHost = "localhost:3306";
    private static String dbName = "exchange_rate_db";


    public Database() {
    }

    private static boolean checkIfDBExists() {
        /* Patikriname ar irasytas JDBC driveris darbui su mysql */
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Database.dbConnection = DriverManager.getConnection("jdbc:mysql://" + dbHost + "/?user=" + dbUser + "&password=" + dbPassword);
            // Connection connection = <your java.sql.Connection>
            ResultSet resultSet = dbConnection.getMetaData().getCatalogs();

            //iterate each catalog in the ResultSet
            while (resultSet.next()) {
                // Get the database name, which is at position 1
                String databaseName = resultSet.getString(1);
                if (databaseName.equals(Database.dbName)) {
                    return true;
                }
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void makeDBConnection() {
        if (Database.checkIfDBExists()) {
            System.out.println("Database exists");
            try {
                // DriverManager: The basic service for managing a set of JDBC drivers.
                Database.dbConnection = DriverManager.getConnection("jdbc:mysql://" + dbHost + "/" + dbName + "?useSSL=true", dbUser, dbPassword);
                System.out.println("Database connection successful");
                Database.checkTables();

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DAO.updateTableValues();
            }
        } else {
            System.out.println("Database does not exist");
            try {
                String createDatabaseStatement = "CREATE DATABASE " + Database.dbName + " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                dbStatement = dbConnection.createStatement();
                dbStatement.executeUpdate(createDatabaseStatement);
                System.out.println("Created new Database \"" + Database.dbName + "\"...");
                String useDatabase = "USE " + Database.dbName + ";";
                dbStatement.executeUpdate(useDatabase);
                Database.checkTables();
                DAO.insertRates();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void checkTables() {
        try {
            ArrayList<String> tables = DAO.getTableNames();
            DatabaseMetaData metadata = dbConnection.getMetaData();
            for (int i = 0; i < tables.size(); i++) {
                ResultSet rs = metadata.getTables(null, null, tables.get(i), null);
                if (!rs.next()) {
                    createTable(tables.get(i));
                    DAO.insertRates(tables.get(i));
                }
            }
            System.out.println("Table check successful");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTable(String tableName) {
        String createTableStatement = null;
        if (tableName.contains("_rates")) {
            createTableStatement = "CREATE TABLE `" + Database.dbName + "`.`" + tableName + "` ( `id` INT UNSIGNED NOT NULL AUTO_INCREMENT , `currency` VARCHAR(5) NOT NULL , `sold_rate` DOUBLE UNSIGNED NOT NULL , `buy_rate` DOUBLE UNSIGNED NOT NULL , `updated` TIMESTAMP on update CURRENT_TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP , PRIMARY KEY (`id`)) ENGINE = InnoDB;";

        } else if (tableName.equals("transactions")) {
            createTableStatement = "CREATE TABLE `" + Database.dbName + "`.`transactions` ( `id` INT UNSIGNED NOT NULL AUTO_INCREMENT , `sold_currency` VARCHAR(5) NOT NULL , `sold_amount` DOUBLE UNSIGNED NOT NULL , `bought_currency` VARCHAR(5) NOT NULL , `bought_amount` DOUBLE UNSIGNED NOT NULL , `exchange_rate` DOUBLE UNSIGNED NOT NULL , `time_stamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP , PRIMARY KEY (`id`)) ENGINE = InnoDB;";
        }

        if (createTableStatement != null) {
            try {
                dbStatement = dbConnection.createStatement();
                dbStatement.executeUpdate(createTableStatement);
                System.out.println("Table \"" + tableName + "\" created...");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



}
