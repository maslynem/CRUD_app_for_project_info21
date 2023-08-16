package ru.s21school.mapper.experienceMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.s21school.dto.ExperienceDto;
import ru.s21school.entity.Check;
import ru.s21school.entity.Experience;
import ru.s21school.exceptions.NoSuchCheckException;
import ru.s21school.mapper.Mapper;
import ru.s21school.repository.CheckRepository;

@Component
@RequiredArgsConstructor
public class ExperienceCreateEditMapper implements Mapper<ExperienceDto, Experience> {
    private final CheckRepository checkRepository;

    @Override
    public Experience map(ExperienceDto object) {
        Check check = checkRepository
                .findById(object.getCheckId())
                .orElseThrow(() -> new NoSuchCheckException("Check with id does not exist: " + object.getCheckId()));
        return new Experience(object.getId(), check, object.getXpAmount());
    }

    @Override
    public Experience map(ExperienceDto fromObject, Experience toObject) {
        toObject.setXpAmount(fromObject.getXpAmount());
        return toObject;
    }
}
