package com.dating.app.repository;

import com.dating.app.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByLikerId(Long likerId);
    List<Like> findByLikedId(Long likedId);
    Optional<Like> findByLikerIdAndLikedId(Long likerId, Long likedId);
    boolean existsByLikerIdAndLikedId(Long likerId, Long likedId);
}
