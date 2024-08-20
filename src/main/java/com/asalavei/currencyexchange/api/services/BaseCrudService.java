package com.asalavei.currencyexchange.api.services;

import com.asalavei.currencyexchange.api.dbaccess.converters.EntityDtoConverter;
import com.asalavei.currencyexchange.api.dbaccess.entities.Entity;
import com.asalavei.currencyexchange.api.dbaccess.repositories.Repository;
import com.asalavei.currencyexchange.api.dto.Dto;

import java.util.Collection;

public abstract class BaseCrudService<
        D extends Dto,
        E extends Entity,
        C extends EntityDtoConverter<E, D>,
        R extends Repository<E>> implements CrudService<D> {

    protected final C converter;
    protected final R repository;

    protected BaseCrudService(C converter, R repository) {
        this.converter = converter;
        this.repository = repository;
    }

    @Override
    public D create(D dto) {
        return save(converter.toEntity(dto));
    }

    protected D save(E entity) {
        return converter.toDto(repository.save(entity));
    }

    @Override
    public Collection<D> findAll() {
        return converter.toDto(repository.findAll());
    }
}
