package com;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tao on 2018/10/15.
 */

public class PattenUtil
{
    /**
     * 获取指定HTML标签的指定属性的值
     * @param source 要匹配的源文本
     * @param element 标签名称
     * @param attr 标签的属性名称
     * @return 属性值列表
     */
    public static List<String> match(String source, String element, String attr) {
        List<String> result = new ArrayList<String>();
        String reg = "<" + element + "[^<>]*?\\s" + attr + "=['\"]?(.*?)['\"]?(\\s.*?)?>";
        Matcher m = Pattern.compile(reg).matcher(source);
        while (m.find()) {
            String r = m.group(1);
            result.add(r);
        }
        return result;
    }

    /**
     * 解决编码问题，去掉换行
     * 方法名称:replaceBlank
     * 方法描述:   
     * @param   
     * @return 返回值描述
     * 创建人：Administrator    
     * 创建时间：2015-11-10 下午03:25:52
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }


    public static List<String> getSubUtil(String soap, String rgex, boolean content) {
        List<String> list = new ArrayList<>();
        Pattern pattern = Pattern.compile(rgex);// 匹配的模式
        Matcher m = pattern.matcher(soap);
        while (m.find()) {
            int i = 1;
            if (content)
                i = 0;
            list.add(m.group(i));
            i++;
        }

        return list;
    }

}
