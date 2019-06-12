package com.zjucsc.application.system.service.hessian_impl;

import com.zjucsc.application.domain.bean.*;
import com.zjucsc.application.domain.exceptions.ScriptException;
import com.zjucsc.application.system.mapper.base.BaseServiceImpl;
import com.zjucsc.application.system.service.hessian_iservice.IArtConfigService;
import com.zjucsc.application.system.service.hessian_mapper.ArtConfigMapper;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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

    @Override
    public CompletableFuture<Exception> saveScriptFile(InputStream is,String name) {
        File file = new File("script");
        if (!file.exists()){
            FileOutputStream fis = null;
            PrintWriter pw = null;
            try {
                boolean b = file.mkdir();
                if (!b){
                    return CompletableFuture.completedFuture(new ScriptException("根目录下无法创建'script'文件夹，脚本上传失败"));
                }
                file = new File("script/name");
                if (file.exists()){
                    fis = new FileOutputStream(file);
                    pw = new PrintWriter(new OutputStreamWriter(fis));
                    pw.println();
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
                    pw.close();
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


}
