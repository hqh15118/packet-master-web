package deploy.util;

import com.alibaba.fastjson.JSON;
import jdk.nashorn.internal.runtime.JSONFunctions;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ConfigUtil {
    private static final String CONFIG_FILE_PATH = "setting.json";
    private static Path checkValid() throws IOException {
        Path path = Paths.get(CONFIG_FILE_PATH);
        if (Files.notExists(path)){
            Files.createFile(path);
        }
        return path;
    }

    @SuppressWarnings("unchecked")
    public static void setData(String key,String value) throws IOException {
        HashMap map = JSON.parseObject(getConfigFileData(),HashMap.class);
        map.put(key,value);
        Path path = checkValid();
        Files.write(path,JSON.toJSONString(map).getBytes());
    }

    public static Map getData() throws IOException {
        return JSON.parseObject(getConfigFileData(),HashMap.class);
    }

    private static String getConfigFileData() throws IOException {
        StringBuilder sb = new StringBuilder();
        Path path = checkValid();
        BufferedReader bfReader = Files.newBufferedReader(path);
        String str;
        while ((str = bfReader.readLine())!=null){
            sb.append(str);
        }
        if (sb.toString().equals("")){
            return "{}";
        }
        return sb.toString();
    }
}
