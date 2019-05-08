package com.zjucsc.application.domain.bean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BulkBean;

import java.util.ArrayList;
import java.util.List;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-08 - 23:15
 */

@Slf4j
public class TsharkCommand {

    private OutputType outputType;
    private String tsharkPath;
    private String pcapFilePath="";
    private String captureDeviceName;
    private String captureDeviceIp;
    private List<String> fields;

    public enum OutputType{
        JSON(" -T json "),
        EK(" -T ek "),
        JSON_RAW(" -T jsonraw ");

        private String output;
        OutputType(String outputType){
            this.output = outputType;
        }
    }



    public static class Builder{
        private TsharkCommand tsharkCommand;
        public Builder(){
            tsharkCommand = new TsharkCommand();
        }

        public Builder outputType(OutputType outputType){
            tsharkCommand.outputType = outputType;
            return this;
        }

        public Builder tsharkPath(String tsharkPath){
            tsharkCommand.tsharkPath = tsharkPath;
            return this;
        }

        public Builder pcapFilePath(String pcapFilePath){
            tsharkCommand.pcapFilePath = pcapFilePath;
            return this;
        }

        public Builder captureDeviceName(String captureDeviceName){
            tsharkCommand.captureDeviceName = captureDeviceName;
            return this;
        }

        public Builder captureDeviceIp(String captureDeviceIp){
            tsharkCommand.captureDeviceIp = captureDeviceIp;
            return this;
        }

        public Builder ek_E(String field){
            if (tsharkCommand.fields == null){
                tsharkCommand.fields = new ArrayList<>();
            }
            tsharkCommand.fields.add(field);
            return this;
        }

        public String build(){
            StringBuilder sb = new StringBuilder();
            if (tsharkCommand.outputType == null){
                sb.append("not define outputType | ");
            }
            if (tsharkCommand.tsharkPath == null){
                sb.append("not define tshark program path");
            }
            if (sb.length() > 0){
                log.error("can not init tshark command without some core args");
                throw new RuntimeException(sb.toString());
            }else{
                sb.append(tsharkCommand.tsharkPath).append(" ")
                        .append(tsharkCommand.outputType.output);
                for (String field : tsharkCommand.fields) {
                    sb.append(" -e ").append(field).append(" ");
                }
                sb.append(tsharkCommand.pcapFilePath);
                if (tsharkCommand.captureDeviceName != null){
                    //TODO
                }
            }
            String command = sb.toString();
            log.info("build tshark command {}" , command);
            return command;
        }
    }
}
