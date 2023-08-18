package ru.s21school.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;
import ru.s21school.dto.FriendDto;
import ru.s21school.entity.Friend;
import ru.s21school.mapper.friendMapper.CsvFriendMapper;
import ru.s21school.mapper.friendMapper.FriendCreateEditMapper;
import ru.s21school.mapper.friendMapper.FriendReadMapper;
import ru.s21school.repository.FriendRepository;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Slf4j
@Service
public class FriendService extends BaseService<Friend, FriendDto, Long> {
    protected FriendService(FriendRepository repository, FriendReadMapper readMapper, FriendCreateEditMapper createEditMapper, CsvFriendMapper csvMapper) {
        super(repository, readMapper, createEditMapper, csvMapper);
    }

    public void writeToCsv(Writer writer) {
        List<Friend> list = repository.findAll();
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            csvPrinter.printRecord("id", "peer1", "peer2");
            for (Friend entity : list) {
                csvPrinter.printRecord(
                        entity.getId(),
                        entity.getPeer1().getNickname(),
                        entity.getPeer2().getNickname()
                );
            }
        } catch (IOException e) {
            log.error("Error While writing CSV ", e);
        }
    }
}
