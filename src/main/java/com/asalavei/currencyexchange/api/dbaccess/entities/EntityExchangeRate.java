package com.asalavei.currencyexchange.api.dbaccess.entities;

import java.math.BigDecimal;

/**
 * Entity representing exchange rate data stored in the database
 */
public class EntityExchangeRate {
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
     * Creates a new {@link Builder} instance for constructing {@link EntityExchangeRate} instances
     *
     * @return a new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for creating instances of the {@link EntityExchangeRate}
     */
    public static final class Builder {
        private Integer id;
        private Integer baseCurrencyId;
        private Integer targetCurrencyId;
        private BigDecimal rate;

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

        public EntityExchangeRate build() {
            EntityExchangeRate entityExchangeRate = new EntityExchangeRate();

            entityExchangeRate.setId(id);
            entityExchangeRate.setBaseCurrencyId(baseCurrencyId);
            entityExchangeRate.setTargetCurrencyId(targetCurrencyId);
            entityExchangeRate.setRate(rate);

            return entityExchangeRate;
        }
    }
}
