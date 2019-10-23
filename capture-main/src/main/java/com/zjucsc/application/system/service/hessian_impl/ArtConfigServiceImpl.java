package com.zjucsc.application.system.service.hessian_impl;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.config.PACKET_PROTOCOL;
import com.zjucsc.application.domain.bean.*;
import com.zjucsc.application.system.mapper.base.BaseServiceImpl;
import com.zjucsc.application.system.service.hessian_iservice.IArtConfigService;
import com.zjucsc.application.system.service.hessian_mapper.ArtConfigMapper;
import com.zjucsc.application.util.CacheUtil;
import com.zjucsc.art_decode.ArtDecodeUtil;
import com.zjucsc.art_decode.artconfig.*;
import com.zjucsc.art_decode.base.BaseConfig;
import com.zjucsc.common.exceptions.ScriptException;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class ArtConfigServiceImpl extends BaseServiceImpl<BaseArtConfig,ArtConfigMapper> implements IArtConfigService {

    @Override
    public BaseResponse getConfigPaged(PagedArtConfig pagedArtConfig) {
        return this.baseMapper.getConfigPaged(pagedArtConfig);
    }

    @Override
    public List<BaseArtConfig> selectAllConfig() {
        return this.baseMapper.selectAllConfig();
    }

    @Override
    public List<String> selectAllShowArt() {
        return this.baseMapper.selectAllShowArt();
    }

    @SuppressFBWarnings("NP_NONNULL_PARAM_VIOLATION")
    @Override
    public CompletableFuture<Exception> saveScriptFile(InputStream is,String name) {
        File file = new File("script");
        if (!file.exists()){
            FileOutputStream fis = null;
            BufferedWriter pw = null;
            try {
                boolean b = file.mkdir();
                if (!b){
                    return CompletableFuture.completedFuture(new ScriptException("根目录下无法创建'script'文件夹，脚本上传失败"));
                }
                file = new File("script/name");
                if (file.exists()){
                    fis = new FileOutputStream(file);
                    pw = new BufferedWriter(new OutputStreamWriter(fis));
                    pw.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (fis != null){
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (pw!=null){
                    try {
                        pw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public boolean scriptExistOrNot(String fileName) {
        File file = new File("script/" + fileName);
        return file.exists();
    }

    @Override
    public BaseArtConfig delArtConfigByProtocolIdAndId(int protocolId, int id) {
        return this.baseMapper.delArtConfigByProtocolIdAndId(protocolId, id);
    }

    @Override
    public BaseArtConfig getArtConfigByProtocolIdAndId(int protocolId, int id) {
        return this.baseMapper.getArtConfigByProtocolIdAndId(protocolId, id);
    }

    @Override
    public Map<String, List<ArtShowState>> selectAllArtConfigShowState() {
        return this.baseMapper.selectAllArtConfigShowState();
    }

    @Override
    public void changeArtConfigShowState(ArtArgShowState artArgShowState) {
        this.baseMapper.changeArtConfigShowState(artArgShowState);
    }

    @Override
    public BaseResponse updateByJSONStr(String jsonData) {
        return this.baseMapper.updateByJSONStr(jsonData);
    }

    @Override
    public BaseResponse insertByJSONStr(String jsonData) {
        return this.baseMapper.insertByJSONStr(jsonData);
    }

    @Override
    public List<ConfigValue> selectArtNameByProtocolName(String protocol) {
        return this.baseMapper.selectArtNameByProtocolName(protocol);
    }

    @Override
    public BaseResponse setArtConfig(int protocolId, String jsonData, BaseConfig baseConfig) {
        //要显示的工艺参数
        if (baseConfig.getShowGraph() == 1){
            CacheUtil.addShowGraphArg(baseConfig.getProtocolId(),baseConfig.getTag());
        }else{
            CacheUtil.removeShowGraph(baseConfig.getProtocolId(),baseConfig.getTag());
        }
        switch (protocolId) {
            case PACKET_PROTOCOL.MODBUS_ID:
                ModBusConfig modBusConfig = JSON.parseObject(jsonData, ModBusConfig.class);
                modBusConfig.setProtocol(PACKET_PROTOCOL.MODBUS);
                ArtDecodeUtil.addArtDecodeConfig(modBusConfig);
                return BaseResponse.OK(true);
            case PACKET_PROTOCOL.S7_ID:
                S7Config s7Config = JSON.parseObject(jsonData, S7Config.class);
                s7Config.setProtocol("s7comm");
                ArtDecodeUtil.addArtDecodeConfig(s7Config);
                return BaseResponse.OK(true);
            case PACKET_PROTOCOL.IEC104_ASDU_ID:
                IEC104Config iec104Config = JSON.parseObject(jsonData, IEC104Config.class);
                iec104Config.setProtocol(PACKET_PROTOCOL.IEC104_ASDU);
                ArtDecodeUtil.addArtDecodeConfig(iec104Config);
                break;
            case PACKET_PROTOCOL.DNP3_0_PRI_ID:
                DNP3Config dnp3Config = JSON.parseObject(jsonData, DNP3Config.class);
                dnp3Config.setProtocol("dnp3");     //这个协议是和工艺参数解析的模块对应起来的
                ArtDecodeUtil.addArtDecodeConfig(dnp3Config);
                break;
            case PACKET_PROTOCOL.PN_IO_ID:
                PnioConfig pnioConfig = JSON.parseObject(jsonData, PnioConfig.class);
                pnioConfig.setProtocol(PACKET_PROTOCOL.PN_IO);
                ArtDecodeUtil.addArtDecodeConfig(pnioConfig);
                break;
            case PACKET_PROTOCOL.OPC_UA_ID:
                OpcuaConfig opcuaConfig = JSON.parseObject(jsonData, OpcuaConfig.class);
                opcuaConfig.setProtocol(PACKET_PROTOCOL.OPC_UA);
                ArtDecodeUtil.addArtDecodeConfig(opcuaConfig);
                break;
            case PACKET_PROTOCOL.MMS_ID:
                MMSConfig mmsConfig = JSON.parseObject(jsonData,MMSConfig.class);
                mmsConfig.setProtocol(PACKET_PROTOCOL.MMS);
                ArtDecodeUtil.addArtDecodeConfig(mmsConfig);
                break;
            case PACKET_PROTOCOL.OPC_DA_ID:
                OpcdaConfig opcdaConfig = JSON.parseObject(jsonData,OpcdaConfig.class);
                opcdaConfig.setProtocol(PACKET_PROTOCOL.OPC_DA);
                ArtDecodeUtil.addArtDecodeConfig(opcdaConfig);
                break;
            default:
                log.error("未指定工艺参数解析ID [{}]",protocolId);
                return BaseResponse.ERROR(500,"未指定工艺参数解析ID" + protocolId);
        }
        //工艺参数组别
        CacheUtil.addOrUpdateArtName2ArtGroup(baseConfig.getTag(),baseConfig.getGroup());
        return BaseResponse.OK();
    }

}
