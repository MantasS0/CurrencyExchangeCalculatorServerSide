package com.company;

public class ExchangeRate {
    private Integer id;
    private String currency;
    private Double exchangeRate;

    public ExchangeRate(String currency, Double exchangeRate) {
        this.currency = currency;
        this.exchangeRate = exchangeRate;
    }

    public ExchangeRate(int id, String currency, Double exchangeRate) {
        this.id = id;
        this.currency = currency;
        this.exchangeRate = exchangeRate;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
