package com.zjucsc.application.domain.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.*;
import java.lang.reflect.Field;
import java.util.List;

public class BeanToJSONGenerator {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, IOException, InstantiationException {
        String outputPath = "C:\\Users\\Administrator\\Desktop\\base_response_json.txt";
        File outputFile = new File(outputPath);
        if (!outputFile.exists()){
            throw new RuntimeException("output file not define!");
        }
        BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile,true)));
        MyClassLoader myClassLoader = new MyClassLoader();
        String classPathz = BeanToJSONGenerator.class.getResource("/").getPath();
        String classPath = BeanToJSONGenerator.class.getResource("").getPath();
        File file = new File(classPath);
        classPath = classPath.replace(classPathz,"");
        File[] files = file.listFiles();
        String classFileName = classPath.replace("/",".");
        for (File singleFile : files) {
            String className = classFileName  + singleFile.getName().replace(".class","");
            Class<?> clazz = Class.forName(className,true,myClassLoader);
            Object res = null;
            try {
                res = clazz.newInstance();
            } catch (InstantiationException e) {
                System.err.println("can not instance " + className);
                continue;
            }
            //if (res instanceof BaseResponse){
                Field[] fields = res.getClass().getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (field.getType().getSimpleName().equals("String")){
                        Object obj = field.getType().newInstance();
                        if (obj instanceof List){
                            System.out.println("list : " + className);
                        }else{
                            field.set(res,"Test");
                        }
                    }
                }
                String json = JSON.toJSONString(res, SerializerFeature.PrettyFormat);
                br.write(className);
                br.newLine();
                br.write("**********************************");
                br.newLine();
                br.write(json);
                br.newLine();
                br.write("**********************************");
                br.newLine();
                br.flush();
                System.out.println(json);
                System.out.println("output : " + className);
           // }
        }
        System.out.println("finish");
        br.close();
    }

    public static class MyClassLoader extends ClassLoader{

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            File file = new File(name);
            assert file.exists();
            FileInputStream fis;
            try {
                fis = new FileInputStream(file);
                int size = fis.available();
                byte[] bytes = new byte[size];
                fis.read(bytes,0,size);
                return defineClass(name,bytes,0,bytes.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
