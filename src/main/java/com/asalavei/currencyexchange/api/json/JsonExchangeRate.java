package com.asalavei.currencyexchange.api.json;

import java.math.BigDecimal;

/**
 * DTO representing exchange rate data that can be serialized to and deserialized from JSON
 */
public class JsonExchangeRate {
    private Integer id;
    private Integer baseCurrencyId;
    private Integer targetCurrencyId;
    private BigDecimal rate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBaseCurrencyId() {
        return baseCurrencyId;
    }

    public void setBaseCurrencyId(Integer baseCurrencyId) {
        this.baseCurrencyId = baseCurrencyId;
    }

    public Integer getTargetCurrencyId() {
        return targetCurrencyId;
    }

    public void setTargetCurrencyId(Integer targetCurrencyId) {
        this.targetCurrencyId = targetCurrencyId;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    /**
     * Creates a new {@link Builder} instance for constructing {@link JsonExchangeRate} instances
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
        private Integer baseCurrencyId;
        private Integer targetCurrencyId;
        private BigDecimal rate;

        private Builder() {
        }

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder baseCurrencyId(Integer baseCurrencyId) {
            this.baseCurrencyId = baseCurrencyId;
            return this;
        }

        public Builder targetCurrencyId(Integer targetCurrencyId) {
            this.targetCurrencyId = targetCurrencyId;
            return this;
        }

        public Builder rate(BigDecimal rate) {
            this.rate = rate;
            return this;
        }

        public JsonExchangeRate build() {
            JsonExchangeRate jsonExchangeRate = new JsonExchangeRate();

            jsonExchangeRate.setId(id);
            jsonExchangeRate.setBaseCurrencyId(baseCurrencyId);
            jsonExchangeRate.setTargetCurrencyId(targetCurrencyId);
            jsonExchangeRate.setRate(rate);

            return jsonExchangeRate;
        }
    }
}
