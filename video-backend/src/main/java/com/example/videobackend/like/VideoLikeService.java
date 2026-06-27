package com.example.videobackend.like;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class VideoLikeService {

    private final VideoLikeRepository videoLikeRepository;

    public VideoLikeService(VideoLikeRepository videoLikeRepository) {
        this.videoLikeRepository = videoLikeRepository;
    }

    // 统计点赞数（查询操作，可选只读事务）
    @Transactional(readOnly = true)
    public long countLikes(Long videoId) {
        return videoLikeRepository.countByVideoId(videoId);
    }

    // 点赞操作（写操作，添加事务）
    @Transactional
    public void like(Long videoId, Long userId) {
        if (videoLikeRepository.existsByVideoIdAndUserId(videoId, userId)) {
            return;
        }
        VideoLike like = VideoLike.builder()
                .videoId(videoId)
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .build();
        videoLikeRepository.save(like);
    }

    // 取消点赞操作（删除操作，核心事务注解）
    @Transactional
    public void unlike(Long videoId, Long userId) {
        videoLikeRepository.deleteByVideoIdAndUserId(videoId, userId);
    }
}
