package ru.s21school.util.validator.operationValidator;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.s21school.dto.operationDto.AddVerterCheckParametersDto;
import ru.s21school.service.PeerService;

@Component
@RequiredArgsConstructor
public class AddVerterCheckValidator implements Validator {
    private final PeerService peerService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return AddVerterCheckParametersDto.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        AddVerterCheckParametersDto dto = (AddVerterCheckParametersDto) target;
        String checkedPeer = dto.getCheckedPeer();
        if (!checkedPeer.isEmpty() && !peerService.findById(checkedPeer).isPresent()) {
                errors.rejectValue("checkedPeer", "", "Peer with this nickname does not exist");
        }
    }
}
