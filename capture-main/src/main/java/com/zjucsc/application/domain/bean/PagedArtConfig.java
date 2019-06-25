package com.zjucsc.application.domain.bean;

import lombok.Data;

@Data
public class PagedArtConfig {
    private int page;
    private int limit;
    private String tag;
    private int protocolId;
}
