package ru.s21school.util.validator.transferedPointValidator;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.s21school.dto.TransferredPointDto;
import ru.s21school.service.PeerService;

@Component
@RequiredArgsConstructor
public class TransferredPointSaveValidator implements Validator {
    private final PeerService peerService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return TransferredPointDto.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        TransferredPointDto pointDto = (TransferredPointDto) target;
        String checkingPeerNickname = pointDto.getCheckingPeerNickname();
        if (!checkingPeerNickname.isEmpty() && !peerService.findById(checkingPeerNickname).isPresent()) {
            errors.rejectValue("checkingPeerNickname", "", "Peer with this nickname does not exist");
        }
        String checkedPeerNickname = pointDto.getCheckedPeerNickname();
        if (!checkingPeerNickname.isEmpty() && !peerService.findById(checkedPeerNickname).isPresent()) {
            errors.rejectValue("checkedPeerNickname", "", "Peer with this nickname does not exist");
        }
    }
}
