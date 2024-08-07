package com.asalavei.currencyexchange.api.dto;

import java.math.BigDecimal;

/**
 * Immutable DTO for transferring exchange rate data between business logic layers
 */
public class ExchangeRate {
    private final Integer id;
    private final Integer baseCurrencyId;
    private final Integer targetCurrencyId;
    private final BigDecimal rate;

    public ExchangeRate(Integer id, Integer baseCurrencyId, Integer targetCurrencyId, BigDecimal rate) {
        this.id = id;
        this.baseCurrencyId = baseCurrencyId;
        this.targetCurrencyId = targetCurrencyId;
        this.rate = rate;
    }

    public Integer getId() {
        return id;
    }

    public Integer getBaseCurrencyId() {
        return baseCurrencyId;
    }

    public Integer getTargetCurrencyId() {
        return targetCurrencyId;
    }

    public BigDecimal getRate() {
        return rate;
    }

    /**
     * Creates a new {@link Builder} instance for constructing {@link ExchangeRate} instances
     *
     * @return a new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for creating instances of the {@link ExchangeRate}
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

        public ExchangeRate build() {
            return new ExchangeRate(id, baseCurrencyId, targetCurrencyId, rate);
        }
    }
}
