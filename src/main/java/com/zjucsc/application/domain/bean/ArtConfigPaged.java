package com.zjucsc.application.domain.bean;

import lombok.Data;

@Data
public class ArtConfigPaged {
    private BaseArtConfig baseArtConfig;
    private int page;
    private int limit;
}
