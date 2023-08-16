package ru.s21school.util.validator.verterValidator;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.s21school.dto.CheckDto;
import ru.s21school.dto.VerterDto;
import ru.s21school.service.CheckService;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VerterSaveValidator implements Validator {
    private final CheckService checkService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return VerterDto.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
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
