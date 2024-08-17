package com.asalavei.currencyexchange.api.json;

import java.math.BigDecimal;

public class JsonExchange implements JsonDto {
    private JsonCurrency baseCurrency;
    private JsonCurrency targetCurrency;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal convertedAmount;

    public JsonCurrency getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(JsonCurrency baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public JsonCurrency getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(JsonCurrency targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private JsonCurrency baseCurrency;
        private JsonCurrency targetCurrency;
        private BigDecimal rate;
        private BigDecimal amount;
        private BigDecimal convertedAmount;

        private Builder() {
        }

        public Builder baseCurrency(JsonCurrency baseCurrency) {
            this.baseCurrency = baseCurrency;
            return this;
        }

        public Builder targetCurrency(JsonCurrency targetCurrency) {
            this.targetCurrency = targetCurrency;
            return this;
        }

        public Builder rate(BigDecimal rate) {
            this.rate = rate;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder convertedAmount(BigDecimal convertedAmount) {
            this.convertedAmount = convertedAmount;
            return this;
        }

        public JsonExchange build() {
            JsonExchange jsonExchange = new JsonExchange();

            jsonExchange.setBaseCurrency(baseCurrency);
            jsonExchange.setTargetCurrency(targetCurrency);
            jsonExchange.setRate(rate);
            jsonExchange.setAmount(amount);
            jsonExchange.setConvertedAmount(convertedAmount);

            return jsonExchange;
        }
    }
}
