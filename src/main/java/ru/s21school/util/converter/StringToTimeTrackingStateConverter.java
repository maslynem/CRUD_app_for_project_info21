package ru.s21school.util.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import ru.s21school.dto.TimeTrackingDto;

import static ru.s21school.dto.TimeTrackingDto.State.IN;
import static ru.s21school.dto.TimeTrackingDto.State.OUT;

public class StringToTimeTrackingStateConverter implements Converter<String, TimeTrackingDto.State> {
    @Override
    public TimeTrackingDto.State convert(@NonNull String source) {
        return source.equalsIgnoreCase("in") ? IN : OUT;
    }
}
