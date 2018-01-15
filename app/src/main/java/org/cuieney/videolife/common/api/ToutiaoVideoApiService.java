package org.cuieney.videolife.common.api;

import org.cuieney.videolife.entity.ToutiaoBean.ToutiaoVideoListBean;
import org.cuieney.videolife.entity.ToutiaoBean.VideoContentBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by daimaren on 2017/10/12
 * 975808394@qq.com
 */

public interface ToutiaoVideoApiService {
	/**
	 * 获取视频标题信息
	 * http://is.snssdk.com/api/news/feed/v62/?iid=5034850950&device_id=6096495334&refer=1&count=20&aid=13&category=subv_game&max_behot_time=1508239793
	 */
	@GET("api/news/feed/v62/?iid=5034850950&device_id=6096495334&refer=1&count=20&aid=13")
	Observable<ToutiaoVideoListBean> getVideoList(
			@Query("category") String category,
			@Query("max_behot_time") String maxBehotTime);
	/**
	 * 获取视频信息
	 * Api 生成较复杂 详情查看 {@linkplain com.meiji.toutiao.module.video.content.VideoContentPresenter#doLoadVideoData(String)}
	 * http://ib.365yg.com/video/urls/v/1/toutiao/mp4/视频ID?r=17位随机数&s=加密结果
	 */
	@GET
	Observable<VideoContentBean> getVideoContent(@Url String url);

}
