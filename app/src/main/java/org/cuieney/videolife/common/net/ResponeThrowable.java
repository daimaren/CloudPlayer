package org.cuieney.videolife.common.net;

/**
 * 版本号：
 * 类描述：
 * 修改时间：${DATA}1904
 */

public class ResponeThrowable extends Exception {
    public int code;
    public String message;

    public ResponeThrowable(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }
}
