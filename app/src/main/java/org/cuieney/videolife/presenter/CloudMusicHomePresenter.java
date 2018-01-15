package org.cuieney.videolife.presenter;

import com.google.gson.Gson;

import org.cuieney.videolife.common.api.CloudMusicApiService;
import org.cuieney.videolife.common.base.RxPresenter;
import org.cuieney.videolife.common.utils.RxUtil;
import org.cuieney.videolife.entity.CloudMusicBean.CloudMusicInfo;
import org.cuieney.videolife.entity.CloudMusicBean.CloudMusicLrcInfo;
import org.cuieney.videolife.entity.CloudMusicBean.MusicPlayListBean;
import org.cuieney.videolife.presenter.contract.CloudMusicHomeContract;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by daimaren on 2017/10/13
 * 975808394@qq.com
 */

public class CloudMusicHomePresenter extends RxPresenter<CloudMusicHomeContract.View> implements CloudMusicHomeContract.Presenter{
	private CloudMusicApiService mRetrofitHelper;
	private Gson gson = new Gson();
	@Inject
	public CloudMusicHomePresenter(CloudMusicApiService mRetrofitHelper) {
		this.mRetrofitHelper = mRetrofitHelper;
	}

	@Override
	public void getMusicPlayList(String type, String id) {
		Subscription rxSubscription = mRetrofitHelper.getPlayList(type, id)
				.compose(RxUtil.rxSchedulerHelper())
				.subscribe(new Subscriber<MusicPlayListBean>() {
					@Override
					public void onCompleted() {
					}

					@Override
					public void onError(Throwable e) {
						mView.error(e);
					}

					@Override
					public void onNext(MusicPlayListBean playListBean) {
						mView.showContent(playListBean);
					}
				});
		addSubscrebe(rxSubscription);
	}
	@Override
	public void getMusicInfo(String type, String id) {
		Subscription rxSubscription = mRetrofitHelper.getMusicInfo(type, id)
				.compose(RxUtil.rxSchedulerHelper())
				.subscribe(new Subscriber<CloudMusicInfo>() {
					@Override
					public void onCompleted() {
					}

					@Override
					public void onError(Throwable e) {
						mView.error(e);
					}

					@Override
					public void onNext(CloudMusicInfo cloudMusicInfo) {
						mView.showMusicInfo(cloudMusicInfo);
					}
				});
		addSubscrebe(rxSubscription);
	}

	@Override
	public void getLrcInfo(String type, String id) {
		Subscription rxSubscription = mRetrofitHelper.getLrcInfo(type, id)
				.compose(RxUtil.rxSchedulerHelper())
				.subscribe(new Subscriber<CloudMusicLrcInfo>() {
					@Override
					public void onCompleted() {
					}

					@Override
					public void onError(Throwable e) {
						mView.error(e);
					}

					@Override
					public void onNext(CloudMusicLrcInfo cloudMusicLrcInfo) {
						mView.showLrcInfo(cloudMusicLrcInfo);
					}
				});
		addSubscrebe(rxSubscription);
	}
}
