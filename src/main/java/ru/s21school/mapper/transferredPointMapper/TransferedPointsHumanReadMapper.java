package ru.s21school.mapper.transferredPointMapper;

import org.springframework.stereotype.Component;
import ru.s21school.dto.TransferredPointDto;
import ru.s21school.mapper.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TransferedPointsHumanReadMapper implements Mapper<List<Map<String, Object>>, List<TransferredPointDto>> {
    @Override
    public List<TransferredPointDto> map(List<Map<String, Object>> object) {
        List<TransferredPointDto> result = new ArrayList<>();
        for (Map<String, Object> entity : object) {
            result.add(new TransferredPointDto(
                    null,
                    entity.get("Peer1").toString(),
                    entity.get("Peer2").toString(),
                    Long.valueOf(entity.get("PointsAmount").toString())
            ));
        }
        return result;
    }
}
