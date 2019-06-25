package com.zjucsc.application.domain.bean;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class CositeDosConfigBean {
    @Min(1)
    private int timeInMill;
    @Min(2)
    private int maxNum;
}
