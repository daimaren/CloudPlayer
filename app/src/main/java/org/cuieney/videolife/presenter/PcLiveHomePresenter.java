package org.cuieney.videolife.presenter;

import com.google.gson.Gson;

import org.cuieney.videolife.common.api.LiveApiService;
import org.cuieney.videolife.common.base.RxPresenter;
import org.cuieney.videolife.common.net.HttpResponse;
import org.cuieney.videolife.common.net.ServerException;
import org.cuieney.videolife.common.utils.LogUtil;
import org.cuieney.videolife.common.utils.RxUtil;
import org.cuieney.videolife.entity.LiveBean.LiveListBean;
import org.cuieney.videolife.presenter.contract.PcLiveHomeContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by daimaren on 2017/10/13
 * 975808394@qq.com
 */

public class PcLiveHomePresenter extends RxPresenter<PcLiveHomeContract.View> implements PcLiveHomeContract.Presenter{
	private LiveApiService mRetrofitHelper;
	private Gson gson = new Gson();
	private String time;
	@Inject
	public PcLiveHomePresenter(LiveApiService mRetrofitHelper) {
		this.mRetrofitHelper = mRetrofitHelper;
	}

	@Override
	public void getAllLiveList(int offset, int limit, List<LiveListBean> liveListBeans) {
		Subscription rxSubscription = mRetrofitHelper.getLiveAllList()
				.compose(RxUtil.rxSchedulerHelper())
				.flatMap(new Func1<HttpResponse<List<LiveListBean>>, Observable<LiveListBean>>() {
					@Override
					public Observable<LiveListBean> call(HttpResponse<List<LiveListBean>> listHttpResponse) {
						if (listHttpResponse.getError()!=0) {
							LogUtil.e("cc",listHttpResponse.toString());
							//如果服务器端有错误信息返回，那么抛出异常，让下面的方法去捕获异常做统一处理
							throw  new ServerException(String.valueOf(listHttpResponse.getData()),listHttpResponse.getError());
						}
						return Observable.from(listHttpResponse.getData());
					}
				})
				.filter(new Func1<LiveListBean, Boolean>() {
					@Override
					public Boolean call(LiveListBean liveListBean) {
						for (LiveListBean bean : liveListBeans){
							if (bean.getRoom_id().equals(liveListBean.getRoom_id()))
								return  false;
						}
						return true;
					}
				})
				.toList()
				.subscribe(new Observer<List<LiveListBean>>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						mView.error(e);
					}

					@Override
					public void onNext(List<LiveListBean> liveListBeans) {
						mView.showContent(liveListBeans);
					}
				});
		addSubscrebe(rxSubscription);
	}
}
