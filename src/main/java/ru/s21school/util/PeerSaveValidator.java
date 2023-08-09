package ru.s21school.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.s21school.dto.PeerDto;
import ru.s21school.service.PeerService;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class PeerSaveValidator implements Validator {
    private final PeerService peerService;

    @Override
    public boolean supports(Class<?> clazz) {
        return PeerDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PeerDto peer = (PeerDto) target;
        if (peerService.findByNickname(peer.getNickname()) != null) {
            errors.rejectValue("nickname", "", "Nickname is already taken");
        }
        LocalDate birthday = peer.getBirthday();
        if (birthday != null && birthday.isAfter(LocalDate.now().minusYears(18))) {
            errors.rejectValue("birthday", "", "Peer must be 18 years old");
        }
    }
}
