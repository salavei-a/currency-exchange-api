package com.asalavei.currencyexchange.api.services;


import com.asalavei.currencyexchange.api.dto.Currency;
import com.asalavei.currencyexchange.api.dto.Exchange;
import com.asalavei.currencyexchange.api.exceptions.CENotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExchangeService {
    private final CurrencyService currencyService;
    private final ExchangeRateService exchangeRateService;
    private final Integer crossCurrencyId;

    public ExchangeService(CurrencyService currencyService, ExchangeRateService exchangeRateService, String crossCurrencyCode) {
        this.currencyService = currencyService;
        this.exchangeRateService = exchangeRateService;
        this.crossCurrencyId = currencyService.getIdByCode(crossCurrencyCode);
    }

    public BigDecimal exchange(BigDecimal amount, BigDecimal rate) {
        return amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }

    public Exchange exchange(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        Currency baseCurrency = currencyService.findByCode(baseCurrencyCode);
        Currency targetCurrency = currencyService.findByCode(targetCurrencyCode);

        BigDecimal rate;

        try {
            rate = exchangeRateService.getRateByCurrencyPair(baseCurrency.getId(), targetCurrency.getId());
        } catch (CENotFoundException e) {
            try {
                BigDecimal reverseRate = exchangeRateService.getRateByCurrencyPair(targetCurrency.getId(), baseCurrency.getId());
                rate = BigDecimal.ONE.divide(reverseRate, 6, RoundingMode.HALF_UP);
            } catch (CENotFoundException ex) {
                try {
                    BigDecimal rateUsdToBaseCurrency = exchangeRateService.getRateByCurrencyPair(crossCurrencyId, baseCurrency.getId());
                    BigDecimal rateUsdToTargetCurrency = exchangeRateService.getRateByCurrencyPair(crossCurrencyId, targetCurrency.getId());
                    rate = rateUsdToTargetCurrency.divide(rateUsdToBaseCurrency, 6, RoundingMode.HALF_UP);
                } catch (CENotFoundException exception) {
                    throw new CENotFoundException("Not found exchange rate for this currency pair.");
                }
            }
        }

        return Exchange.builder()
                .baseCurrency(baseCurrency)
                .targetCurrency(targetCurrency)
                .rate(rate)
                .amount(amount)
                .convertedAmount(exchange(amount, rate))
                .build();
    }
}
