package ru.s21school.util.validator.ExperienceValidator;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.s21school.dto.checkDto.CheckDto;
import ru.s21school.dto.experienceDto.ExperienceDto;
import ru.s21school.service.CheckService;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ExperienceSaveValidator implements Validator {
    private final CheckService checkService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return ExperienceDto.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        ExperienceDto experience = (ExperienceDto) target;
        Long checkId = experience.getCheckId();
        if (checkId != null) {
            Optional<CheckDto> check = checkService.findById(checkId);
            if (!check.isPresent()) {
                errors.rejectValue("checkId", "", "Check with this ID does not exist");
            }
        }
    }
}
