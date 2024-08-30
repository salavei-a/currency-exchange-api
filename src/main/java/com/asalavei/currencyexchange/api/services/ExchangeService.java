package com.asalavei.currencyexchange.api.services;

import com.asalavei.currencyexchange.api.dbaccess.converters.EntityExchangeRateConverter;
import com.asalavei.currencyexchange.api.dbaccess.entities.EntityExchangeRate;
import com.asalavei.currencyexchange.api.dbaccess.repositories.ExchangeRateRepository;
import com.asalavei.currencyexchange.api.dto.Exchange;
import com.asalavei.currencyexchange.api.dto.ExchangeRate;
import com.asalavei.currencyexchange.api.exceptions.CEDatabaseException;
import com.asalavei.currencyexchange.api.exceptions.CENotFoundException;
import com.asalavei.currencyexchange.api.exceptions.ExceptionMessage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static java.math.MathContext.DECIMAL64;

public class ExchangeService implements Service {

    private final ExchangeRateRepository exchangeRateRepository;
    private final EntityExchangeRateConverter converter;
    private final String crossCurrencyCode;

    public ExchangeService(ExchangeRateRepository exchangeRateRepository, EntityExchangeRateConverter converter, String crossCurrencyCode) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.converter = converter;
        this.crossCurrencyCode = crossCurrencyCode;
    }

    public Exchange exchange(Exchange dto) {
        try {
            EntityExchangeRate entityExchangeRate = findExchangeRate(dto)
                    .orElseThrow(() -> new CENotFoundException(String.format(ExceptionMessage.EXCHANGE_RATE_NOT_FOUND,
                            dto.getBaseCurrency().getCode(), dto.getTargetCurrency().getCode())));

            ExchangeRate exchangeRate = converter.toDto(entityExchangeRate);
            BigDecimal amount = dto.getAmount();

            return Exchange.builder()
                    .baseCurrency(exchangeRate.getBaseCurrency())
                    .targetCurrency(exchangeRate.getTargetCurrency())
                    .rate(exchangeRate.getRate())
                    .amount(amount)
                    .convertedAmount(exchange(amount, entityExchangeRate.getRate()))
                    .build();
        } catch (CENotFoundException e) {
            throw new CENotFoundException(String.format(ExceptionMessage.EXCHANGE_FAILED, e.getMessage()));
        } catch (CEDatabaseException e) {
            throw new CEDatabaseException(String.format(ExceptionMessage.EXCHANGE_FAILED, e.getMessage()));
        }
    }

    private BigDecimal exchange(BigDecimal amount, BigDecimal rate) {
        return amount.multiply(rate).setScale(2, RoundingMode.HALF_EVEN);
    }

    private Optional<EntityExchangeRate> findExchangeRate(Exchange dto) {
        return findByDirectRate(dto)
                .or(() -> findByReverseRate(dto))
                .or(() -> findByCrossCurrency(dto));
    }

    private Optional<EntityExchangeRate> findByDirectRate(Exchange dto) {
        return exchangeRateRepository.findByCurrencyCodes(dto.getBaseCurrency().getCode(), dto.getTargetCurrency().getCode());
    }

    private Optional<EntityExchangeRate> findByReverseRate(Exchange dto) {
        return exchangeRateRepository.findByCurrencyCodes(dto.getTargetCurrency().getCode(), dto.getBaseCurrency().getCode())
                .map(reverseRate -> EntityExchangeRate.builder()
                                     .baseCurrency(reverseRate.getTargetCurrency())
                                     .targetCurrency(reverseRate.getBaseCurrency())
                                     .rate(BigDecimal.ONE.divide(reverseRate.getRate(), DECIMAL64).setScale(6, RoundingMode.HALF_EVEN))
                                     .build());
    }

    private Optional<EntityExchangeRate> findByCrossCurrency(Exchange dto) {
        return exchangeRateRepository.findByCurrencyCodes(crossCurrencyCode, dto.getBaseCurrency().getCode())
                .flatMap(crossToBase -> exchangeRateRepository.findByCurrencyCodes(crossCurrencyCode, dto.getTargetCurrency().getCode())
                .map(crossToTarget -> EntityExchangeRate.builder()
                                       .baseCurrency(crossToBase.getTargetCurrency())
                                       .targetCurrency(crossToTarget.getTargetCurrency())
                                       .rate(crossToTarget.getRate().divide(crossToBase.getRate(), DECIMAL64).setScale(6, RoundingMode.HALF_EVEN))
                                       .build()));
    }
}
