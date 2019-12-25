package com.zjucsc.application.config.watch_config;


import com.zjucsc.art_decode.ArtDecodeUtil;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.context.annotation.Configuration;

@Endpoint(id = "iec104anddnp3mapperopt")
@Configuration
public class IEC104AndDNP3MapperOpt {

    @WriteOperation
    public String reloadMapperFile(String iec104 ,String dnp3){
        return ArtDecodeUtil.load104AndDNPMapperFile(iec104, dnp3);
    }

    @ReadOperation
    public String getIECDNPDecodeResult(){
        return ArtDecodeUtil.getWatchVar("iec_dnp_data");
    }

    @WriteOperation
    public String getIECDNPMapper(){
        return ArtDecodeUtil.getWatchVar("iec_dnp_mapper");
    }
}
