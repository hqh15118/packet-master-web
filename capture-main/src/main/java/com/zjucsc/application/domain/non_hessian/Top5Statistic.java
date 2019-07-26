package com.zjucsc.application.domain.non_hessian;

import lombok.Data;

import java.util.List;

@Data
public class Top5Statistic {
    private List<Top5Wrapper> attackIps;
    private List<Top5Wrapper> attackedIps;
    private List<Top5Wrapper> attackProtocol;
    private List<Top5Wrapper> attackType;

    @Data
    public static class Top5Wrapper{
        private String msg;
        private int count;

        public Top5Wrapper(String msg, int count) {
            this.msg = msg;
            this.count = count;
        }

        @Override
        public String toString() {
            return "Top5Wrapper{" +
                    "msg='" + msg + '\'' +
                    ", count=" + count +
                    '}';
        }
    }
}
