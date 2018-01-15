package org.cuieney.videolife.entity.LiveBean;

/**
 * 版本号：
 * 类描述：
 * 修改时间：${DATA}1202
 */

public class TempLiveVideoBean {
    private int error;
    private Data data;

    public int getError() {
        return error;
    }

    public Data getData() {
        return data;
    }

    public static class Data {
        private int room_id;
        private String hls_url;

        public int getRoom_id() {
            return room_id;
        }

        public String getHls_url() {
            return hls_url;
        }

        @Override
        public String toString() {
            return "GsonDouyuRoom [room_id=" + room_id + ", hls_url" + hls_url + "]";
        }
    }
}
