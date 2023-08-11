package ru.s21school.util.validator.peerToPeerValidator;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.s21school.dto.CheckDto;
import ru.s21school.dto.PeerToPeerDto;
import ru.s21school.service.CheckService;
import ru.s21school.service.PeerService;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PeerToPeerSaveValidator implements Validator {
    private final PeerService peerService;
    private final CheckService checkService;


    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return PeerToPeerDto.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        PeerToPeerDto peerToPeer = (PeerToPeerDto) target;
        Long checkId = peerToPeer.getCheckId();
        if (checkId != null) {
            Optional<CheckDto> check = checkService.findById(checkId);
            if (!check.isPresent()) {
                errors.rejectValue("checkId", "", "Check with this ID does not exist");
            }
        }
        String checkingPeerNickname = peerToPeer.getCheckingPeerNickname();
        if (!checkingPeerNickname.isEmpty() && !peerService.findById(checkingPeerNickname).isPresent()) {
            errors.rejectValue("checkingPeerNickname", "", "Peer with this nickname does not exist");
        }
    }
}
