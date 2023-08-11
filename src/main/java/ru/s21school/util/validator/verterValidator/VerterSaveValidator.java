package ru.s21school.util.validator.verterValidator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.s21school.dto.checkDto.CheckDto;
import ru.s21school.dto.verterDto.VerterDto;
import ru.s21school.service.CheckService;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VerterSaveValidator implements Validator {
    private final CheckService checkService;

    @Override
    public boolean supports(Class<?> clazz) {
        return VerterDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        VerterDto verter = (VerterDto) target;
        Long checkId = verter.getCheckId();
        if (checkId != null) {
            Optional<CheckDto> check = checkService.findById(checkId);
            if (!check.isPresent()) {
                errors.rejectValue("checkId", "", "Check with this ID does not exist");
            }
        }
    }
}
