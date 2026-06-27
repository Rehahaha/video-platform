package com.example.videobackend.comment;

import com.example.videobackend.comment.dto.CommentRequest;
import com.example.videobackend.comment.dto.CommentResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/videos/{videoId}/comments")
    public Page<CommentResponse> listByVideo(
            @PathVariable("videoId") Long videoId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        return commentService.listByVideo(videoId, page, size);
    }

    @PostMapping("/videos/{videoId}/comments")
    public ResponseEntity<CommentResponse> add(
            @PathVariable("videoId") Long videoId,
            @Valid @RequestBody CommentRequest request
    ) {
        CommentResponse resp = commentService.addComment(videoId, request);
        return new ResponseEntity<>(resp, HttpStatus.CREATED);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}



