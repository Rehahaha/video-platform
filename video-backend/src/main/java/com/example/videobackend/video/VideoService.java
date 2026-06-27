package com.example.videobackend.video;

import com.example.videobackend.video.dto.VideoResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class VideoService {

    private final VideoRepository videoRepository;
    private final String basePath;

    public VideoService(VideoRepository videoRepository,
                        @Value("${video.storage.base-path}") String basePath) {
        this.videoRepository = videoRepository;
        this.basePath = basePath;
    }

    public Page<VideoResponse> listVideos(int page, int size) {
        PageRequest pr = PageRequest.of(page, size);
        return videoRepository.findAllByOrderByCreatedAtDesc(pr)
                .map(this::toResponse);
    }

    public VideoResponse getVideo(Long id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("视频不存在"));
        // 简单增加一次播放量
        video.setViewCount((video.getViewCount() == null ? 0 : video.getViewCount()) + 1);
        videoRepository.save(video);
        return toResponse(video);
    }

    public VideoResponse uploadVideo(Long userId,
                                     String title,
                                     String description,
                                     MultipartFile videoFile,
                                     MultipartFile coverFile) throws IOException {
        ensureBaseDir();

        String videoFileName = saveFile(videoFile, "videos");
        String coverFileName = coverFile != null && !coverFile.isEmpty()
                ? saveFile(coverFile, "covers")
                : null;

        Video video = Video.builder()
                .userId(userId)
                .title(title)
                .description(description)
                .videoUrl("/static/videos/" + videoFileName)
                .coverUrl(coverFileName != null ? "/static/covers/" + coverFileName : null)
                .viewCount(0)
                .createdAt(LocalDateTime.now())
                .build();

        Video saved = videoRepository.save(video);
        return toResponse(saved);
    }

    private void ensureBaseDir() throws IOException {
        Path base = Paths.get(basePath);
        if (!Files.exists(base)) {
            Files.createDirectories(base);
        }
        Path videosDir = base.resolve("videos");
        Path coversDir = base.resolve("covers");
        if (!Files.exists(videosDir)) {
            Files.createDirectories(videosDir);
        }
        if (!Files.exists(coversDir)) {
            Files.createDirectories(coversDir);
        }
    }

    private String saveFile(MultipartFile file, String subDir) throws IOException {
        String originalName = file.getOriginalFilename();
        String ext = "";
        if (originalName != null && originalName.contains(".")) {
            ext = originalName.substring(originalName.lastIndexOf("."));
        }
        String newName = UUID.randomUUID() + ext;

        Path dir = Paths.get(basePath, subDir);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        Path target = dir.resolve(newName);
        file.transferTo(target.toFile());
        return newName;
    }

    private VideoResponse toResponse(Video video) {
        return VideoResponse.builder()
                .id(video.getId())
                .userId(video.getUserId())
                .title(video.getTitle())
                .description(video.getDescription())
                .coverUrl(video.getCoverUrl())
                .videoUrl(video.getVideoUrl())
                .viewCount(video.getViewCount())
                .createdAt(video.getCreatedAt())
                .build();
    }
}



