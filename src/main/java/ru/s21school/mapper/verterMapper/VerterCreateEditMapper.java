package ru.s21school.mapper.verterMapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.s21school.dto.VerterDto;
import ru.s21school.entity.Check;
import ru.s21school.entity.Verter;
import ru.s21school.exceptions.NoSuchCheckException;
import ru.s21school.mapper.Mapper;
import ru.s21school.repository.CheckRepository;

@Component
@AllArgsConstructor
public class VerterCreateEditMapper implements Mapper<VerterDto, Verter> {
    private final CheckRepository checkRepository;

    @Override
    public Verter map(VerterDto object) {
        Check check = checkRepository.findById(object.getCheckId()).orElseThrow(() -> new NoSuchCheckException("Check does not exist. id: " + object.getCheckId()));
        return new Verter(
                object.getId(),
                check,
                object.getCheckState(),
                object.getTime()
        );
    }

    @Override
    public Verter map(VerterDto fromObject, Verter toObject) {
        toObject.setState(fromObject.getCheckState());
        toObject.setTime(fromObject.getTime());
        return toObject;
    }
}
