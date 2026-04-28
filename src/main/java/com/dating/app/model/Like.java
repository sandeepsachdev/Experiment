package com.dating.app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "likes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"liker_id", "liked_id"})
})
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "liker_id", nullable = false)
    private Long likerId;

    @Column(name = "liked_id", nullable = false)
    private Long likedId;

    public Like() {}

    public Like(Long likerId, Long likedId) {
        this.likerId = likerId;
        this.likedId = likedId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getLikerId() { return likerId; }
    public void setLikerId(Long likerId) { this.likerId = likerId; }

    public Long getLikedId() { return likedId; }
    public void setLikedId(Long likedId) { this.likedId = likedId; }
}
