package com.zjucsc.attack.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArtAttackAnalyzeUtil {
    private static List<String> exp = new ArrayList<>();

    public static String attackDecode(List<String> expression, Map<String, Float> techmap, String description)
    {
        if(expression ==null)
        {
            return null;
        }
        exp.clear();
        String logic = null;
        Boolean result= null;
        for(String x:expression)
        {
            if(x.equals("&&") || x.equals("||") || x.equals("="))
            {
                if(decodebool(exp,techmap)==null)
                {
//                    return "配置错误";
                    return null;
                }
                else if(logic == null)
                {
                    result = decodebool(exp,techmap);
                    if(result == null)
                    {
//                        return "配置错误";
                        return null;
                    }
                }
                else if(logic.equals("&&")){
                    result = result && decodebool(exp, techmap);
                }
                else if(logic.equals("||"))
                {
                    result = result || decodebool(exp,techmap);
                }
                logic = x;
                exp.clear();
            }
            else if(logic != null && logic.equals("="))
            {
                if(x.equals("0") && !result)
                {
                    return description;
                }
                else if(x.equals("1") && result)
                {
                    return description;
                }
            }
            else
            {
                exp.add(x);
            }
        }
        return null;
    }

    private static Boolean decodebool(List<String> list, Map<String, Float> techmap)
    {
        if(list == null || list.isEmpty())
        {
            return null;
        }
        int len = list.size();
        if(len==1)
        {
            if(techmap.get(list.get(0))==null)
            {
                return null;
            }
            if(techmap.get(list.get(0))==0f)
            {
                return false;
            }
            else if(techmap.get(list.get(0))==1f)
            {
                return true;
            }
            else
            {
                return null; /////公式表述错误
            }
        }
        else if(list.get(0).equals("!"))
        {
            if(len==2 && techmap.get(list.get(1))!=null)
            {
                if(techmap.get(list.get(1))==1f)
                {
                    return false;
                }
                else if(techmap.get(list.get(1))==0f)
                {
                    return true;
                }
                else
                {
                    return null; /////公式表述错误
                }
            }
            else
            {
                return null;
            }
        }
        else if(len==2)
        {
            return null;
        }
        else
        {
            if(techmap.get(list.get(0))!= null) {
                float A = techmap.get(list.get(0));
                if (list.get(1).equals(">")) {
                    if(techmap.get(list.get(2))!=null) {
                        return A > techmap.get(list.get(2));
                    }
                    else
                    {
                        return A > Float.valueOf(list.get(2));
                    }
                }
                else if (list.get(1).equals("<")) {
                    if(techmap.get(list.get(2))!=null) {
                        return A < techmap.get(list.get(2));
                    }
                    else
                    {
                        return A < Float.valueOf(list.get(2));
                    }
                }
                else if (list.get(1).equals(">=")) {
                    if(techmap.get(list.get(2))!=null) {
                        return A >= techmap.get(list.get(2));
                    }
                    else
                    {
                        return A >= Float.valueOf(list.get(2));
                    }
                }
                else if (list.get(1).equals("<=")) {
                    if(techmap.get(list.get(2))!=null) {
                        return A <= techmap.get(list.get(2));
                    }
                    else
                    {
                        return A <= Float.valueOf(list.get(2));
                    }
                }
                else if (list.get(1).equals("==")) {
                    if(techmap.get(list.get(2))!=null) {
                        return A == techmap.get(list.get(2));
                    }
                    else
                    {
                        return A == Float.valueOf(list.get(2));
                    }
                }
            }
        }
        return null;
    }
}
