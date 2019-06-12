package com.zjucsc.application.domain.bean;

import lombok.Data;

@Data
public class AttackF {
    private int danger;
    private int badType;
    private String timeSort;
    private int page;
    private int limit;
}
