package com.example.videobackend.like;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/videos/{videoId}")
public class VideoLikeController {

    private final VideoLikeService videoLikeService;

    public VideoLikeController(VideoLikeService videoLikeService) {
        this.videoLikeService = videoLikeService;
    }

    @GetMapping("/likes/count")
    public long count(@PathVariable("videoId") Long videoId) {
        return videoLikeService.countLikes(videoId);
    }

    @PostMapping("/like")
    public ResponseEntity<Void> like(
            @PathVariable("videoId") Long videoId,
            @RequestParam("userId") Long userId
    ) {
        videoLikeService.like(videoId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/like")
    public ResponseEntity<Void> unlike(
            @PathVariable("videoId") Long videoId,
            @RequestParam("userId") Long userId
    ) {
        videoLikeService.unlike(videoId, userId);
        return ResponseEntity.noContent().build();
    }
}



