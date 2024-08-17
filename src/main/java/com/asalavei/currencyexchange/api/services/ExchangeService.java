package com.asalavei.currencyexchange.api.services;


import com.asalavei.currencyexchange.api.dto.Currency;
import com.asalavei.currencyexchange.api.dto.Exchange;
import com.asalavei.currencyexchange.api.exceptions.CENotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExchangeService {
    private final CurrencyService currencyService;
    private final ExchangeRateService exchangeRateService;
    private final String crossCurrencyCode;
    private Integer crossCurrencyId;

    public ExchangeService(CurrencyService currencyService, ExchangeRateService exchangeRateService, String crossCurrencyCode) {
        this.currencyService = currencyService;
        this.exchangeRateService = exchangeRateService;
        this.crossCurrencyCode = crossCurrencyCode;
    }

    public BigDecimal exchange(BigDecimal amount, BigDecimal rate) {
        return amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }

    public Exchange exchange(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        Currency baseCurrency = currencyService.findByCode(baseCurrencyCode);
        Currency targetCurrency = currencyService.findByCode(targetCurrencyCode);

        BigDecimal rate = getExchangeRate(baseCurrency, targetCurrency);

        return Exchange.builder()
                .baseCurrency(baseCurrency)
                .targetCurrency(targetCurrency)
                .rate(rate)
                .amount(amount)
                .convertedAmount(exchange(amount, rate))
                .build();
    }

    private BigDecimal getExchangeRate(Currency baseCurrency, Currency targetCurrency) {
        try {
            return exchangeRateService.getRateByCurrencyPair(baseCurrency.getId(), targetCurrency.getId());
        } catch (CENotFoundException e) {
            return getRateFromReverseOrCrossCurrency(baseCurrency, targetCurrency);
        }

    }

    private BigDecimal getRateFromReverseOrCrossCurrency(Currency baseCurrency, Currency targetCurrency) {
        try {
            BigDecimal reverseRate = exchangeRateService.getRateByCurrencyPair(targetCurrency.getId(), baseCurrency.getId());
            return BigDecimal.ONE.divide(reverseRate, 6, RoundingMode.HALF_UP);
        } catch (CENotFoundException e) {
            return getRateViaCrossCurrency(baseCurrency, targetCurrency);
        }
    }

    private BigDecimal getRateViaCrossCurrency(Currency baseCurrency, Currency targetCurrency) {
        ensureCrossCurrencyId();
        try {
            BigDecimal crossToBaseCurrencyRate = exchangeRateService.getRateByCurrencyPair(crossCurrencyId, baseCurrency.getId());
            BigDecimal crossToTargetCurrencyRate = exchangeRateService.getRateByCurrencyPair(crossCurrencyId, targetCurrency.getId());
            return crossToTargetCurrencyRate.divide(crossToBaseCurrencyRate, 6, RoundingMode.HALF_UP);
        } catch (CENotFoundException exception) {
            throw new CENotFoundException("Not found exchange rate for this currency pair.");
        }
    }

    private void ensureCrossCurrencyId() {
        if (this.crossCurrencyId == null) {
            this.crossCurrencyId = currencyService.getIdByCode(crossCurrencyCode);
        }
    }
}
