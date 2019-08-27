package com.zjucsc.application.domain.non_hessian;

import lombok.Data;

@Data
public class ArtGroupWrapper {
    private String value;
    private String group;

    public ArtGroupWrapper(String value, String group) {
        this.value = value;
        this.group = group;
    }

    @Override
    public String toString() {
        return "ArtGroupWrapper{" +
                "value='" + value + '\'' +
                ", group='" + group + '\'' +
                '}';
    }
}
