package ru.s21school.util.validator.recommendationValidator;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.s21school.dto.PeerDto;
import ru.s21school.dto.RecommendationDto;
import ru.s21school.service.PeerService;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RecommendationSaveEditValidator implements Validator {
    private final PeerService peerService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return RecommendationDto.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        RecommendationDto recommendationDto = (RecommendationDto) target;
        String peerNickname = recommendationDto.getPeerNickname();
        String recommendedPeerNickname = recommendationDto.getRecommendedPeerNickname();
        if (!peerNickname.isEmpty() && !recommendedPeerNickname.isEmpty()) {
            Optional<PeerDto> peer = peerService.findById(peerNickname);
            if (!peer.isPresent()) {
                errors.rejectValue("peerNickname", "", "Peer with this nickname does not exist");
            }
            Optional<PeerDto> recommendedPeer = peerService.findById(recommendedPeerNickname);
            if (!recommendedPeer.isPresent()) {
                errors.rejectValue("recommendedPeerNickname", "", "Peer with this nickname does not exist");
            }
        }

    }
}
