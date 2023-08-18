package ru.s21school.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.s21school.mapper.Mapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
public abstract class BaseService<E, D, K> {
    @Value("${hibernate.jdbc.batch_size}")
    private int batchSize;
    protected final JpaRepository<E, K> repository;
    protected final Mapper<E, D> tdMapper;
    protected final Mapper<D, E> dtMapper;
    protected final Mapper<CSVRecord, D> csvMapper;

    protected BaseService(JpaRepository<E, K> repository, Mapper<E, D> tdmapper, Mapper<D, E> dtMapper, Mapper<CSVRecord, D> csvMapper) {
        this.repository = repository;
        this.tdMapper = tdmapper;
        this.dtMapper = dtMapper;
        this.csvMapper = csvMapper;
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

    @Transactional
    public void readFromCsv(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            Iterable<CSVRecord> records = csvParser.getRecords();
            List<D> list = new ArrayList<>();
            int count = 0;
            for (CSVRecord myRecord : records) {
                list.add(csvMapper.map(myRecord));
                count++;
                if (count % batchSize == 0) {
                    repository.saveAll(list.stream().map(dtMapper::map).collect(Collectors.toList()));
                    list.clear();
                }
            }
            if (!list.isEmpty()) {
                repository.saveAll(list.stream().map(dtMapper::map).collect(Collectors.toList()));
                list.clear();
            }
        } catch (IOException e) {
            log.error("Error While reading CSV ", e);
        }
    }
}
