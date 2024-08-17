package com.asalavei.currencyexchange.api.dto;

import java.math.BigDecimal;

public class Exchange implements Dto {
    private final Currency baseCurrency;
    private final Currency targetCurrency;
    private final BigDecimal rate;
    private final BigDecimal amount;
    private final BigDecimal convertedAmount;

    public Exchange(Currency baseCurrency, Currency targetCurrency, BigDecimal rate, BigDecimal amount, BigDecimal convertedAmount) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Currency baseCurrency;
        private Currency targetCurrency;
        private BigDecimal rate;
        private BigDecimal amount;
        private BigDecimal convertedAmount;

        private Builder() {
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

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder convertedAmount(BigDecimal convertedAmount) {
            this.convertedAmount = convertedAmount;
            return this;
        }

        public Exchange build() {
            return new Exchange(baseCurrency, targetCurrency, rate, amount, convertedAmount);
        }
    }
}
