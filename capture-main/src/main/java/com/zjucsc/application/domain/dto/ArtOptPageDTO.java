package com.zjucsc.application.domain.dto;

import lombok.Data;

@Data
public class ArtOptPageDTO {
    private int page;
    private int limit;
    private String protocol;
}
