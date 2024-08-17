package com.asalavei.currencyexchange.api.dto;

import java.math.BigDecimal;

/**
 * Immutable DTO for transferring exchange rate data between business logic layers
 */
public class ExchangeRate extends BaseDto<Integer> {
    private final Currency baseCurrency;
    private final Currency targetCurrency;
    private final BigDecimal rate;

    public ExchangeRate(Integer id, Currency baseCurrency, Currency targetCurrency, BigDecimal rate) {
        super(id);
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public Currency getBaseCurrency() {
        return baseCurrency;
    }

    public Currency getTargetCurrency() {
        return targetCurrency;
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
        private Currency baseCurrency;
        private Currency targetCurrency;
        private BigDecimal rate;

        private Builder() {
        }

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder baseCurrency(Currency baseCurrency) {
            this.baseCurrency = baseCurrency;
            return this;
        }

        public Builder targetCurrency(Currency targetCurrency) {
            this.targetCurrency = targetCurrency;
            return this;
        }

        public Builder rate(BigDecimal rate) {
            this.rate = rate;
            return this;
        }

        public ExchangeRate build() {
            return new ExchangeRate(id, baseCurrency, targetCurrency, rate);
        }
    }
}
