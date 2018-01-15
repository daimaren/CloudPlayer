package org.cuieney.videolife.common.net;

/**
 * 版本号：
 * 类描述：
 * 修改时间：${DATA}1848
 */

public class ServerException extends RuntimeException {
    public int code;
    public String message;

    public ServerException(String message, int code) {
        this.message=message;
        this.code = code;
    }
}
