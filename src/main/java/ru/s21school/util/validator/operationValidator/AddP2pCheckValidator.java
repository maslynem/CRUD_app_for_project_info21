package ru.s21school.util.validator.operationValidator;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.s21school.dto.operationDto.AddP2pCheckParametersDto;
import ru.s21school.service.PeerService;


@Component
@RequiredArgsConstructor
public class AddP2pCheckValidator implements Validator {
    private final PeerService peerService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return AddP2pCheckParametersDto.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        AddP2pCheckParametersDto dto = (AddP2pCheckParametersDto) target;
        String checkedPeer = dto.getCheckedPeer();
        String checkingPeer = dto.getCheckingPeer();
        if (!checkedPeer.isEmpty() && !checkingPeer.isEmpty()) {
            if (!peerService.findById(checkedPeer).isPresent()) {
                errors.rejectValue("checkedPeer", "", "Peer with this nickname does not exist");
            }
            if (!peerService.findById(checkingPeer).isPresent()) {
                errors.rejectValue("checkingPeer", "", "Peer with this nickname does not exist");
            }
            if (checkedPeer.equals(checkingPeer)) {
                errors.rejectValue("checkedPeer", "", "Can not be the same");
                errors.rejectValue("checkingPeer", "", "Can not be the same");
            }
        }
    }
}
