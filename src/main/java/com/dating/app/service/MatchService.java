package com.dating.app.service;

import com.dating.app.model.Like;
import com.dating.app.model.Profile;
import com.dating.app.repository.LikeRepository;
import com.dating.app.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MatchService {

    private final LikeRepository likeRepository;
    private final ProfileRepository profileRepository;

    public MatchService(LikeRepository likeRepository, ProfileRepository profileRepository) {
        this.likeRepository = likeRepository;
        this.profileRepository = profileRepository;
    }

    public Map<String, Object> likeProfile(Long likerId, Long likedId) {
        if (likerId.equals(likedId)) {
            throw new IllegalArgumentException("Cannot like yourself");
        }
        if (!profileRepository.existsById(likerId) || !profileRepository.existsById(likedId)) {
            throw new IllegalArgumentException("Profile not found");
        }
        if (likeRepository.existsByLikerIdAndLikedId(likerId, likedId)) {
            return Map.of("message", "Already liked", "matched", isMatch(likerId, likedId));
        }

        likeRepository.save(new Like(likerId, likedId));
        boolean matched = isMatch(likerId, likedId);
        return Map.of(
            "message", matched ? "It's a match!" : "Like recorded",
            "matched", matched
        );
    }

    public List<Profile> getMatches(Long profileId) {
        List<Long> likedByMe = likeRepository.findByLikerId(profileId)
            .stream().map(Like::getLikedId).toList();

        List<Long> likedMe = likeRepository.findByLikedId(profileId)
            .stream().map(Like::getLikerId).toList();

        List<Long> matchIds = likedByMe.stream()
            .filter(likedMe::contains)
            .collect(Collectors.toList());

        return profileRepository.findAllById(matchIds);
    }

    public List<Profile> getLikesReceived(Long profileId) {
        List<Long> likerIds = likeRepository.findByLikedId(profileId)
            .stream().map(Like::getLikerId).toList();
        return profileRepository.findAllById(likerIds);
    }

    public Optional<Profile> unlike(Long likerId, Long likedId) {
        return likeRepository.findByLikerIdAndLikedId(likerId, likedId).map(like -> {
            likeRepository.delete(like);
            return profileRepository.findById(likedId).orElse(null);
        });
    }

    private boolean isMatch(Long a, Long b) {
        return likeRepository.existsByLikerIdAndLikedId(a, b)
            && likeRepository.existsByLikerIdAndLikedId(b, a);
    }
}
