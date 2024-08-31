package com.asalavei.currencyexchange.api.dbaccess.entities;

import java.math.BigDecimal;

/**
 * Entity for exchange rate data
 */
public class EntityExchangeRate extends BaseEntity<Integer> {

    private EntityCurrency baseCurrency;
    private EntityCurrency targetCurrency;
    private BigDecimal rate;

    public EntityCurrency getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(EntityCurrency baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public EntityCurrency getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(EntityCurrency targetCurrency) {
        this.targetCurrency = targetCurrency;
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
        private EntityCurrency baseCurrency;
        private EntityCurrency targetCurrency;
        private BigDecimal rate;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder baseCurrency(EntityCurrency baseCurrency) {
            this.baseCurrency = baseCurrency;
            return this;
        }

        public Builder targetCurrency(EntityCurrency targetCurrency) {
            this.targetCurrency = targetCurrency;
            return this;
        }

        public Builder rate(BigDecimal rate) {
            this.rate = rate;
            return this;
        }

        public EntityExchangeRate build() {
            EntityExchangeRate entityExchangeRate = new EntityExchangeRate();

            entityExchangeRate.setId(id);
            entityExchangeRate.setBaseCurrency(baseCurrency);
            entityExchangeRate.setTargetCurrency(targetCurrency);
            entityExchangeRate.setRate(rate);

            return entityExchangeRate;
        }
    }
}
