package com.asalavei.currencyexchange.api.services;

import com.asalavei.currencyexchange.api.dbaccess.converters.EntityCurrencyConverter;
import com.asalavei.currencyexchange.api.dbaccess.repositories.CurrencyRepository;
import com.asalavei.currencyexchange.api.dbaccess.repositories.ExchangeRateRepository;
import com.asalavei.currencyexchange.api.dto.Currency;
import com.asalavei.currencyexchange.api.dto.Exchange;
import com.asalavei.currencyexchange.api.exceptions.CENotFoundException;
import com.asalavei.currencyexchange.api.exceptions.ExceptionMessages;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExchangeService implements Service {
    private final CurrencyRepository currencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;
    private final EntityCurrencyConverter converter;
    private final String crossCurrencyCode;
    private Integer crossCurrencyId;

    public ExchangeService(CurrencyRepository currencyRepository, ExchangeRateRepository exchangeRateRepository, EntityCurrencyConverter converter, String crossCurrencyCode) {
        this.currencyRepository = currencyRepository;
        this.exchangeRateRepository = exchangeRateRepository;
        this.converter = converter;
        this.crossCurrencyCode = crossCurrencyCode;
    }

    public Exchange exchange(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        Currency baseCurrency = converter.toDto(currencyRepository.findByCode(baseCurrencyCode));
        Currency targetCurrency = converter.toDto(currencyRepository.findByCode(targetCurrencyCode));
        BigDecimal rate = getExchangeRate(baseCurrency, targetCurrency);

        return Exchange.builder()
                .baseCurrency(baseCurrency)
                .targetCurrency(targetCurrency)
                .rate(rate)
                .amount(amount)
                .convertedAmount(exchange(amount, rate))
                .build();
    }

    private BigDecimal exchange(BigDecimal amount, BigDecimal rate) {
        return amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getExchangeRate(Currency baseCurrency, Currency targetCurrency) {
        try {
            return exchangeRateRepository.getRateByCurrencyPairIds(baseCurrency.getId(), targetCurrency.getId());
        } catch (CENotFoundException e) {
            return getRateFromReverseOrCrossCurrency(baseCurrency, targetCurrency);
        }

    }

    private BigDecimal getRateFromReverseOrCrossCurrency(Currency baseCurrency, Currency targetCurrency) {
        try {
            BigDecimal reverseRate = exchangeRateRepository.getRateByCurrencyPairIds(targetCurrency.getId(), baseCurrency.getId());
            return BigDecimal.ONE.divide(reverseRate, 6, RoundingMode.HALF_UP);
        } catch (CENotFoundException e) {
            return getRateViaCrossCurrency(baseCurrency, targetCurrency);
        }
    }

    private BigDecimal getRateViaCrossCurrency(Currency baseCurrency, Currency targetCurrency) {
        ensureCrossCurrencyId();
        try {
            BigDecimal crossToBaseCurrencyRate = exchangeRateRepository.getRateByCurrencyPairIds(crossCurrencyId, baseCurrency.getId());
            BigDecimal crossToTargetCurrencyRate = exchangeRateRepository.getRateByCurrencyPairIds(crossCurrencyId, targetCurrency.getId());
            return crossToTargetCurrencyRate.divide(crossToBaseCurrencyRate, 6, RoundingMode.HALF_UP);
        } catch (CENotFoundException exception) {
            throw new CENotFoundException(ExceptionMessages.EXCHANGE_RATE_NOT_FOUND);
        }
    }

    private void ensureCrossCurrencyId() {
        if (this.crossCurrencyId == null) {
            this.crossCurrencyId = currencyRepository.getIdByCode(crossCurrencyCode);
        }
    }
}
