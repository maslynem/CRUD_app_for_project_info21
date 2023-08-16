package ru.s21school.mapper.customQueryResultMapper;

import org.springframework.stereotype.Component;
import ru.s21school.mapper.Mapper;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomQueryResultMapper implements Mapper<List, List<String>> {
    @Override
    public List<String> map(List objects) {
        List<String> result = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        for (Object o : objects) {
            Object[] values = (Object[]) o;
            for (Object value : values) {
                stringBuilder.append(value).append(' ');
            }
            result.add(stringBuilder.toString());
            stringBuilder.setLength(0);
        }
        return result;
    }
}
