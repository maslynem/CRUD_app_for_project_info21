package ru.s21school.util.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import ru.s21school.entity.CheckState;


public class StringToCheckStateConverter implements Converter<String, CheckState> {
    @Override
    public CheckState convert(@NonNull String source) {
        for (CheckState checkState : CheckState.values()) {
            if (checkState.name().equalsIgnoreCase(source))
                return checkState;
        }
        return null;
    }
}
