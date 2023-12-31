package ru.s21school.util.validator.checkValidator;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.s21school.dto.CheckDto;
import ru.s21school.dto.PeerDto;
import ru.s21school.service.PeerService;
import ru.s21school.service.TaskService;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CheckSaveUpdateValidator implements Validator {
    private final PeerService peerService;
    private final TaskService taskService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return CheckDto.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        CheckDto check = (CheckDto) target;
        Optional<PeerDto> peer = peerService.findById(check.getPeerNickname());
        if (!peer.isPresent()) {
            errors.rejectValue("peerNickname", "", "Peer with this nickname does not exist");
        }
        if (!taskService.findById(check.getTaskTitle()).isPresent()) {
            errors.rejectValue("taskTitle", "", "Task with this title does not exist");
        }
    }
}
