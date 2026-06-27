package com.example.videobackend.comment;

import com.example.videobackend.comment.dto.CommentRequest;
import com.example.videobackend.comment.dto.CommentResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Page<CommentResponse> listByVideo(Long videoId, int page, int size) {
        PageRequest pr = PageRequest.of(page, size);
        return commentRepository.findByVideoIdOrderByCreatedAtDesc(videoId, pr)
                .map(this::toResponse);
    }

    public CommentResponse addComment(Long videoId, CommentRequest request) {
        Comment comment = Comment.builder()
                .videoId(videoId)
                .userId(request.getUserId())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .build();
        Comment saved = commentRepository.save(comment);
        return toResponse(saved);
    }

    public void deleteComment(Long id) {
        Comment c = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("评论不存在"));
        commentRepository.delete(c);
    }

    private CommentResponse toResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .videoId(comment.getVideoId())
                .userId(comment.getUserId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}




