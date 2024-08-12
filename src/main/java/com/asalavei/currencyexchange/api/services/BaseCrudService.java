package com.asalavei.currencyexchange.api.services;

import com.asalavei.currencyexchange.api.dbaccess.converters.EntityDtoConverter;
import com.asalavei.currencyexchange.api.dbaccess.dao.BaseEntityDao;
import com.asalavei.currencyexchange.api.dbaccess.entities.BaseEntity;
import com.asalavei.currencyexchange.api.dto.BaseDto;

import java.util.Collection;
import java.util.stream.Collectors;

public abstract class BaseCrudService<I extends Comparable<I>,
        D extends BaseDto<I>,
        E extends BaseEntity<I>,
        C extends EntityDtoConverter<I, E, D>,
        R extends BaseEntityDao<I, E>> implements CrudService<I, D> {

    private final R entityDao;
    private final C converter;

    protected BaseCrudService(R entityDao, C converter) {
        this.entityDao = entityDao;
        this.converter = converter;
    }

    @Override
    public D create(D dto) {
        return converter.toDto(entityDao.save(converter.toEntity(dto)));
    }

    @Override
    public Collection<D> findAll() {
        return entityDao.findAll().stream().map(converter::toDto).collect(Collectors.toList());
    }

    @Override
    public D findByCode(String code) {
        E entity = entityDao.findByCode(code);
        return converter.toDto(entity);
    }
}
