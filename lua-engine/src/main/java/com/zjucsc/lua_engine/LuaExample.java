package com.zjucsc.lua_engine;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.CoerceLuaToJava;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LuaExample {
    public static void main(String[] args) {

        String luaFileName =  "script/helloworld.lua";
        Globals globals = JsePlatform.standardGlobals();//加载标准库
        LuaValue root = globals.loadfile(luaFileName);

        System.out.println(
                "******************************\n" +
                        "* 无返回对象情况下java调用lua函数\n" +
                        "*****************************"
        );

        LuaValue value = root.call(); //call default method
        System.out.println(value.get("name"));      //ok return 'hongqianhui'
        LuaValue func1 = globals.get(LuaValue.valueOf("helloWithoutTranscoder"));//get lua function
        String result1 = func1.call().toString();   //call function
        System.out.println(result1);

        System.out.println(
                "******************************\n" +
                        "* 接下来均为在有返回对象情况下调用lua函数\n" +
                        "*****************************"
        );

        LuaValue transCoderObj = root.call();       //get transcoder table
        LuaValue func2 = transCoderObj.get(LuaValue.valueOf("hello"));  //nil
        String result2 = func2.call().toString();
        System.out.println(result2);

        System.out.println(
                "******************************\n" +
                        "* 有参（参数为字符串数据）lua函数\n" +
                        "*****************************"
        );

        transCoderObj = globals.loadfile(luaFileName).call();
        LuaValue func = transCoderObj.get(LuaValue.valueOf("test"));
        String result = func.call(LuaValue.valueOf("sky")).toString();
        System.out.println(result);

        System.out.println(
                "******************************\n" +
                        "* 返回一个lua对象的lua函数\n" +
                        "*****************************"
        );

        transCoderObj = globals.loadfile(luaFileName).call();
        func = transCoderObj.get(LuaValue.valueOf("getInfo"));
        LuaValue hTable  = func.call();
        //解析返回来的table，这里按照格式，一个个参数去取
        String userId = hTable.get("userId").toString();
        LuaTable servicesTable = (LuaTable) CoerceLuaToJava.coerce(hTable.get("services"), LuaTable.class);
        List<String> servicesList = new ArrayList<>();
        for (int i = 1; i <= servicesTable.length(); i++) {
            int length = servicesTable.get(i).length();
            StringBuilder service = new StringBuilder();
            for (int j = 1; j <= length; j++) {
                service.append(servicesTable.get(i).get(j).toString());
            }
            servicesList.add(service.toString());
        }
        System.out.println("userId:\n"+userId);
        System.out.println("servcies:\n"+servicesList);

        System.out.println(
                "******************************\n" +
                        "* 传入一个java对象到lua函数\n" +
                        "*****************************"
        );
        transCoderObj = globals.loadfile(luaFileName).call();
        func = transCoderObj.get(LuaValue.valueOf("readInfo"));//获取transCoderObj山的readInfo方法
        //CoerceJavaToLua.coerce(javaObject) 经测试，可以直接调用该方法
        //将一个java对象转化为luaValue，但是嵌套model情况下的java对象转
        //换有问题，因此这里直接使用LuaValue手动去包装
        LuaValue luaValue = new LuaTable();
        luaValue.set("userId", "11111");
        userId  = func.invoke(luaValue).toString();
        System.out.println("userId:\n"+userId);

        System.out.println(
                "******************************\n" +
                        "* 传入一个java map对象到lua函数\n" +
                        "*****************************"
        );
        LuaValue doMapFun = globals.get(LuaValue.valueOf("do_map"));
        HashMap<String,String> map = new HashMap<>();
        LuaValue luaDog = CoerceJavaToLua.coerce(map); // Java to Lua
        doMapFun.invoke(luaDog);
        System.out.println(map);
    }
}
