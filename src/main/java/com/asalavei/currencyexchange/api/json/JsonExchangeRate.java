package com.asalavei.currencyexchange.api.json;

import java.math.BigDecimal;

/**
 * JSON DTO for exchange rate data
 */
public class JsonExchangeRate extends BaseJsonDto<Integer> {

    private JsonCurrency baseCurrency;
    private JsonCurrency targetCurrency;
    private BigDecimal rate;

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

    /**
     * Creates a new {@link Builder} instance for constructing {@link JsonExchangeRate} instance
     *
     * @return a new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for creating instances of the {@link JsonExchangeRate}
     */
    public static final class Builder {

        private Integer id;
        private JsonCurrency baseCurrency;
        private JsonCurrency targetCurrency;
        private BigDecimal rate;

        private Builder() {
        }

        public Builder id(Integer id) {
            this.id = id;
            return this;
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

        public JsonExchangeRate build() {
            JsonExchangeRate jsonExchangeRate = new JsonExchangeRate();

            jsonExchangeRate.setId(id);
            jsonExchangeRate.setBaseCurrency(baseCurrency);
            jsonExchangeRate.setTargetCurrency(targetCurrency);
            jsonExchangeRate.setRate(rate);

            return jsonExchangeRate;
        }
    }
}
