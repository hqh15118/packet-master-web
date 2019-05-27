package com.zjucsc.application.domain.bean;


import lombok.Data;

@Data
public class ArtConfig {
    private int protocolId;
    private int minLength;
    private int offset;
    private int length;
    private String tag;
    private String artMeaning;
}
