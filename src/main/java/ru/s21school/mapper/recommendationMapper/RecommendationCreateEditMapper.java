package ru.s21school.mapper.recommendationMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.s21school.dto.RecommendationDto;
import ru.s21school.entity.Peer;
import ru.s21school.entity.Recommendation;
import ru.s21school.mapper.Mapper;
import ru.s21school.repository.PeerRepository;

@Component
@RequiredArgsConstructor
public class RecommendationCreateEditMapper implements Mapper<RecommendationDto, Recommendation> {
    private final PeerRepository peerRepository;

    @Override
    public Recommendation map(RecommendationDto object) {
        return new Recommendation(
                object.getId(),
                findPeer(object.getPeerNickname()),
                findPeer(object.getRecommendedPeerNickname())
        );
    }

    @Override
    public Recommendation map(RecommendationDto fromObject, Recommendation toObject) {
        toObject.setPeer(findPeer(fromObject.getPeerNickname()));
        toObject.setRecommendedPeer(findPeer(fromObject.getRecommendedPeerNickname()));
        return toObject;
    }

    private Peer findPeer(String nickname) {
        return peerRepository
                .findById(nickname)
                .orElseThrow(() -> new RuntimeException("Peer with this nickname does not exist: " + nickname));
    }
}
