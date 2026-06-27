package com.example.videobackend.like;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoLikeRepository extends JpaRepository<VideoLike, Long> {

    long countByVideoId(Long videoId);

    boolean existsByVideoIdAndUserId(Long videoId, Long userId);

    void deleteByVideoIdAndUserId(Long videoId, Long userId);
}



