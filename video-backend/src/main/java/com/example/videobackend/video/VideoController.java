package com.example.videobackend.video;

import com.example.videobackend.video.dto.VideoResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping
    public Page<VideoResponse> list(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "12") int size
    ) {
        return videoService.listVideos(page, size);
    }

    @GetMapping("/{id}")
    public VideoResponse detail(@PathVariable("id") Long id) {
        return videoService.getVideo(id);
    }

    @PostMapping
    public ResponseEntity<VideoResponse> upload(
            @RequestParam("userId") @NotNull Long userId,
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestPart("videoFile") MultipartFile videoFile,
            @RequestPart(value = "coverFile", required = false) MultipartFile coverFile
    ) throws IOException {
        VideoResponse resp = videoService.uploadVideo(userId, title, description, videoFile, coverFile);
        return new ResponseEntity<>(resp, HttpStatus.CREATED);
    }
}


