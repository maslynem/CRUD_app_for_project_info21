package ru.s21school.util.validator.timeTrackinkValidator;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.s21school.dto.PeerDto;
import ru.s21school.dto.TimeTrackingDto;
import ru.s21school.service.PeerService;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TimeTrackingSaveEditValidator implements Validator {

    private final PeerService peerService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return TimeTrackingDto.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        TimeTrackingDto timeTrackingDto = (TimeTrackingDto) target;
        String peerNickname = timeTrackingDto.getPeerNickname();
        if (!peerNickname.isEmpty()) {
            Optional<PeerDto> peer = peerService.findById(peerNickname);
            if (!peer.isPresent()) {
                errors.rejectValue("peerNickname", "", "Peer with this nickname does not exist");
            }
        }
    }
}
