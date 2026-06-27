package com.example.videobackend.video.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoUploadRequest {

    @NotBlank
    private String title;

    private String description;

    private String tags;
}



