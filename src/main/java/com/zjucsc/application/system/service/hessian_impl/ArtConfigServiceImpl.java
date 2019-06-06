package com.zjucsc.application.system.service.hessian_impl;

import com.zjucsc.application.domain.bean.ArtConfig;
import com.zjucsc.application.domain.bean.ArtConfigPaged;
import com.zjucsc.application.domain.exceptions.ScriptException;
import com.zjucsc.application.system.mapper.base.BaseServiceImpl;
import com.zjucsc.application.system.service.hessian_iservice.IArtConfigService;
import com.zjucsc.application.system.service.hessian_mapper.ArtConfigMapper;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
public class ArtConfigServiceImpl extends BaseServiceImpl<ArtConfig,ArtConfigMapper> implements IArtConfigService {
    @Override
    public void deleteByProtocolIdAndMinLength(int protocolId, int minLength) {
        this.baseMapper.deleteArtConfigByProtocolIdAndMinLength(protocolId, minLength);
    }

    @Override
    public List<ArtConfig> getConfigPaged(ArtConfigPaged artConfigPaged) {
        int page = artConfigPaged.getPage();
        int limit = artConfigPaged.getLimit();
        int startIndex = page * 20;
        return this.baseMapper.getConfigPaged(artConfigPaged.getProtocolId(),
                artConfigPaged.getMinLength(),startIndex,limit);
    }

    @Override
    public List<ArtConfig> selectAllConfig() {
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

}
