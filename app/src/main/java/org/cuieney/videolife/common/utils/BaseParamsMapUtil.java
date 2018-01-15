package org.cuieney.videolife.common.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 描述信息：请求网络参数封装类
 * 备注信息:
 */
public class BaseParamsMapUtil {

    /**
     * 公共的参数集合
     *
     * @return
     */
    public static Map<String, String> getParamsMap() {
        Map<String, String> paramsmap = new LinkedHashMap<>();
        paramsmap.put("client_sys", "android");
        paramsmap.put("aid", "android1");
        paramsmap.put("time",System.currentTimeMillis()+"");
        return paramsmap;
    }


}
