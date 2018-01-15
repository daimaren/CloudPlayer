package org.cuieney.videolife.presenter;

import com.google.gson.Gson;

import org.cuieney.videolife.common.api.CloudMusicApiService;
import org.cuieney.videolife.common.base.RxPresenter;
import org.cuieney.videolife.common.utils.RxUtil;
import org.cuieney.videolife.entity.CloudMusicBean.MusicPlayListBean;
import org.cuieney.videolife.presenter.contract.CloudMusicDetailContract;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by daimaren on 2017/10/13
 * 975808394@qq.com
 */

public class CloudMusicDetailPresenter extends RxPresenter<CloudMusicDetailContract.View> implements CloudMusicDetailContract.Presenter{
	private CloudMusicApiService mRetrofitHelper;
	private Gson gson = new Gson();
	@Inject
	public CloudMusicDetailPresenter(CloudMusicApiService mRetrofitHelper) {
		this.mRetrofitHelper = mRetrofitHelper;
	}

	@Override
	public void getPlayList(String type, String id) {
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
}
