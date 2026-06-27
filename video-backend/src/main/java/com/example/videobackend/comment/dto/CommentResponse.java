package com.example.videobackend.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class CommentResponse {

    private Long id;
    private Long videoId;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;
}

