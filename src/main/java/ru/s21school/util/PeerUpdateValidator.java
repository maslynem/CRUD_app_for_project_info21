package ru.s21school.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.s21school.dto.PeerDto;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class PeerUpdateValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return PeerDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PeerDto peer = (PeerDto) target;
        LocalDate birthday = peer.getBirthday();
        if (birthday.isAfter(LocalDate.now().minusYears(18))) {
            errors.rejectValue("birthday", "", "Peer must be 18 years old");
        }
    }
}
