package ru.s21school.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.s21school.mapper.Mapper;

import java.util.NoSuchElementException;
import java.util.Optional;

@Transactional(readOnly = true)
public abstract class BaseService<T, D, K> {
    private final JpaRepository<T, K> repository;
    private final Mapper<T, D> tdMapper;
    private final Mapper<D, T> dtMapper;

    protected BaseService(JpaRepository<T, K> repository, Mapper<T, D> tdmapper, Mapper<D, T> dtMapper) {
        this.repository = repository;
        this.tdMapper = tdmapper;
        this.dtMapper = dtMapper;
    }

    public Page<D> findAllWithPaginationAndSorting(int page, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        return repository.findAll(pageable).map(tdMapper::map);
    }

    public Optional<D> findById(K id) {
        return repository.findById(id).map(tdMapper::map);
    }

    @Transactional
    public D save(D entityDto) {
        return Optional.of(entityDto)
                .map(dtMapper::map)
                .map(repository::save)
                .map(tdMapper::map)
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public Optional<D> update(K id, D entityDto) {
        return repository.findById(id)
                .map(entity -> dtMapper.map(entityDto, entity))
                .map(repository::saveAndFlush)
                .map(tdMapper::map);
    }

    @Transactional
    public boolean delete(K id) {
        return repository.findById(id)
                .map(entity -> {
                    repository.delete(entity);
                    return true;
                }).orElse(false);
    }
}
