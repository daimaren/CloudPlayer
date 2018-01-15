package org.cuieney.videolife.common.api;

import org.cuieney.videolife.entity.CloudMusicBean.CloudMusicInfo;
import org.cuieney.videolife.entity.CloudMusicBean.CloudMusicLrcInfo;
import org.cuieney.videolife.entity.CloudMusicBean.MusicPlayListBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by daimaren on 2017/10/31
 * 975808394@qq.com
 */

public interface CloudMusicApiService {
	/**
	 *  歌单: https://api.imjad.cn/cloudmusic/?type=playlist&id=126102318
	 * @return
	 */
	@GET("/cloudmusic/")
	Observable<MusicPlayListBean> getPlayList(@Query("type") String type, @Query("id") String id);
	/**
	 *  单曲信息：https://api.imjad.cn/cloudmusic/?type=song&id=247491
	 * @return
	 */
	@GET("/cloudmusic/")
	Observable<CloudMusicInfo> getMusicInfo(@Query("type") String type, @Query("id") String id);
	/**
	 *  歌词：https://api.imjad.cn/cloudmusic/?type=lyric&id=247491
	 * @return
	 */
	@GET("/cloudmusic/")
	Observable<CloudMusicLrcInfo> getLrcInfo(@Query("type") String type, @Query("id") String id);
}
