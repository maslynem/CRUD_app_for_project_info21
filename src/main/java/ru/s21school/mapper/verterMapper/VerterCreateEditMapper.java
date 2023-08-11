package ru.s21school.mapper.verterMapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.s21school.dto.VerterDto;
import ru.s21school.entity.Check;
import ru.s21school.entity.Verter;
import ru.s21school.mapper.Mapper;
import ru.s21school.repository.CheckRepository;

@Component
@AllArgsConstructor
public class VerterCreateEditMapper implements Mapper<VerterDto, Verter> {
    private final CheckRepository checkRepository;

    @Override
    public Verter map(VerterDto object) {
        Check check = checkRepository.findById(object.getCheckId()).orElseThrow(() -> new RuntimeException("Check whith this id does not exist"));
        return new Verter(
                object.getId(),
                check,
                object.getState(),
                object.getTime()
        );
    }

    @Override
    public Verter map(VerterDto fromObject, Verter toObject) {
        toObject.setState(fromObject.getState());
        toObject.setTime(fromObject.getTime());
        return toObject;
    }
}
