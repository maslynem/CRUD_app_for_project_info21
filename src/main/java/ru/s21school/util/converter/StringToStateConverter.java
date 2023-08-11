package ru.s21school.util.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import ru.s21school.entity.State;


public class StringToStateConverter implements Converter<String, State> {
    @Override
    public State convert(@NonNull String source) {
        for (State state : State.values()) {
            if (state.name().equalsIgnoreCase(source))
                return state;
        }
        return null;
    }
}
