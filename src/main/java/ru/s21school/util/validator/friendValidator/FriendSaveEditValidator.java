package ru.s21school.util.validator.friendValidator;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.s21school.dto.FriendDto;
import ru.s21school.service.PeerService;

@Component
@RequiredArgsConstructor
public class FriendSaveEditValidator implements Validator {

    private final PeerService peerService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return FriendDto.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        FriendDto friendDto = (FriendDto) target;
        String peerOneNickname = friendDto.getPeerOneNickname();
        String peerTwoNickname = friendDto.getPeerTwoNickname();
        if (!peerOneNickname.isEmpty() && !peerTwoNickname.isEmpty()) {
            if (!peerService.findById(peerTwoNickname).isPresent()) {
                errors.rejectValue("peerOneNickname", "", "Peer with this nickname does not exist");
            }
            if (!peerService.findById(peerTwoNickname).isPresent()) {
                errors.rejectValue("peerTwoNickname", "", "Peer with this nickname does not exist");
            }
            if (peerOneNickname.equals(peerTwoNickname)) {
                errors.rejectValue("peerOneNickname", "", "Can not be the same");
                errors.rejectValue("peerTwoNickname", "", "Can not be the same");
            }
        }
    }
}
