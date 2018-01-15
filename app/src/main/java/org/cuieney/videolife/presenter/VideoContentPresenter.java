package org.cuieney.videolife.presenter;

import android.util.Base64;

import com.google.gson.Gson;

import org.cuieney.videolife.common.api.ToutiaoVideoApiService;
import org.cuieney.videolife.common.base.RxPresenter;
import org.cuieney.videolife.common.utils.LogUtil;
import org.cuieney.videolife.common.utils.RxUtil;
import org.cuieney.videolife.entity.ToutiaoBean.VideoContentBean;
import org.cuieney.videolife.presenter.contract.VideoContentContract;

import java.util.Random;
import java.util.zip.CRC32;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by daimaren on 2017/10/13
 * 975808394@qq.com
 */

public class VideoContentPresenter extends RxPresenter<VideoContentContract.View> implements VideoContentContract.Presenter{
	private ToutiaoVideoApiService mRetrofitHelper;
	private Gson gson = new Gson();
	private String time;
	@Inject
	public VideoContentPresenter(ToutiaoVideoApiService mRetrofitHelper) {
		this.mRetrofitHelper = mRetrofitHelper;
	}
	private static String getRandom() {
		Random random = new Random();
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < 16; i++) {
			result.append(random.nextInt(10));
		}
		return result.toString();
	}
	private static String getVideoContentApi(String videoid) {
		String VIDEO_HOST = "http://ib.365yg.com";
		String VIDEO_URL = "/video/urls/v/1/toutiao/mp4/%s?r=%s";
		String r = getRandom();
		String s = String.format(VIDEO_URL, videoid, r);
		// 将/video/urls/v/1/toutiao/mp4/{videoid}?r={Math.random()} 进行crc32加密
		CRC32 crc32 = new CRC32();
		crc32.update(s.getBytes());
		String crcString = crc32.getValue() + "";
		String url = VIDEO_HOST + s + "&s=" + crcString;
		return url;
	}
	@Override
	public void getVideoPlayUrl(String videoid) {
		String url = getVideoContentApi(videoid);
		Subscription rxSubscription = mRetrofitHelper.getVideoContent(url)
				.compose(RxUtil.rxSchedulerHelper())
				.map(new Func1<VideoContentBean, String>() {
					@Override
					public String call(VideoContentBean videoContentBean) {
						VideoContentBean.DataBean.VideoListBean videoList = videoContentBean.getData().getVideo_list();
						if (videoList.getVideo_3() != null) {
							String base64 = videoList.getVideo_3().getMain_url();
							String url = (new String(Base64.decode(base64.getBytes(), Base64.DEFAULT)));
							LogUtil.d("getVideoUrls: " + url);
							return url;
						}

						if (videoList.getVideo_2() != null) {
							String base64 = videoList.getVideo_2().getMain_url();
							String url = (new String(Base64.decode(base64.getBytes(), Base64.DEFAULT)));
							LogUtil.d("getVideoUrls: " + url);
							return url;
						}

						if (videoList.getVideo_1() != null) {
							String base64 = videoList.getVideo_1().getMain_url();
							String url = (new String(Base64.decode(base64.getBytes(), Base64.DEFAULT)));
							LogUtil.d("getVideoUrls: " + url);
							return url;
						}
						return null;
					}
				})
				.subscribe(new Subscriber<String>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						mView.error(e);
					}

					@Override
					public void onNext(String s) {
						mView.onSetVideoPlayUrl(s);
						LogUtil.d("doLoadVideoData:" + s);
					}
				});
		addSubscrebe(rxSubscription);
	}
}
