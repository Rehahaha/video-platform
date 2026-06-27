package com.example.videobackend.video.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class VideoResponse {

    private Long id;
    private Long userId;
    private String title;
    private String description;
    private String coverUrl;
    private String videoUrl;
    private Integer viewCount;
    private LocalDateTime createdAt;
}



