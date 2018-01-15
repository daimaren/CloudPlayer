package org.cuieney.videolife.common.api;

public class UrlManager {
    public static final String KAIYAN_HOST = "https://baobab.kaiyanapp.com/api/v4/" ;
    public static final String WANGYI_HOST = "http://www.wawa.fm:9090/wawa/api.php/magazine/";
    public static final String YIREN_HOST = "http://v3.wufazhuce.com:8000/api/";
    public static final String VEER_HOST = "https://api.veervr.tv/";
    public static final String KUULA_HOST = "https://kuula.co/api/";
    public static final String TOUTIAO_HOST = "http://is.snssdk.com/";
    public static final String DOUYU_HOST = "http://capi.douyucdn.cn";
    public static final String API_HOST = "https://api.imjad.cn";

    public static String getKuulaCover(String uuid){
        return "https://storage.kuula.co/"+uuid+"/01-cover.jpg";
    }
}
