package org.cuieney.videolife.common.api;

import org.cuieney.videolife.common.net.HttpResponse;
import org.cuieney.videolife.entity.LiveBean.LiveListBean;
import org.cuieney.videolife.entity.LiveBean.LiveVideoBean;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by daimaren on 2017/10/31
 * 975808394@qq.com
 */

public interface LiveApiService {
	/**
	 *  全部直播
	 *  http://capi.douyucdn.cn/api/v1/live
	 * @return
	 */
	@GET("/api/v1/live")
	Observable<HttpResponse<List<LiveListBean>>> getLiveAllList();
	/**
	 *  直播播放页
	 * @return
	 */
	@GET("/api/v1/room/" + "{room_id}")
	Call<HttpResponse<LiveVideoBean>> getLiveVideoInfo(@Path("room_id") String room_id, @QueryMap Map<String, String> params);
}
