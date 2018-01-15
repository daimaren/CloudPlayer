package org.cuieney.videolife.presenter;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.cuieney.videolife.common.api.ToutiaoVideoApiService;
import org.cuieney.videolife.common.base.RxPresenter;
import org.cuieney.videolife.common.utils.RxUtil;
import org.cuieney.videolife.entity.ToutiaoBean.MultiNewsArticleDataBean;
import org.cuieney.videolife.entity.ToutiaoBean.ToutiaoItemListBean;
import org.cuieney.videolife.entity.ToutiaoBean.ToutiaoVideoListBean;
import org.cuieney.videolife.presenter.contract.ToutiaoVideoHomeContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by daimaren on 2017/10/13
 * 975808394@qq.com
 */

public class ToutiaoVideoHomePresenter extends RxPresenter<ToutiaoVideoHomeContract.View> implements ToutiaoVideoHomeContract.Presenter{
	private ToutiaoVideoApiService mRetrofitHelper;
	private Gson gson = new Gson();
	private String time;
	@Inject
	public ToutiaoVideoHomePresenter(ToutiaoVideoApiService mRetrofitHelper) {
		this.mRetrofitHelper = mRetrofitHelper;
	}
	@Override
	public void getVideoData(String maxBehotTime, List<MultiNewsArticleDataBean> dataList) {
		Subscription rxSubscription = mRetrofitHelper.getVideoList("subv_game", maxBehotTime)
				.compose(RxUtil.rxSchedulerHelper())
				.flatMap(new Func1<ToutiaoVideoListBean, Observable<MultiNewsArticleDataBean>>() {
					@Override
					public Observable<MultiNewsArticleDataBean> call(ToutiaoVideoListBean toutiaoVideoListBean) {
						List<MultiNewsArticleDataBean> dataList = new ArrayList<>();
						for (ToutiaoItemListBean dataBean : toutiaoVideoListBean.getItemList()) {
							dataList.add(gson.fromJson(dataBean.getContent(), MultiNewsArticleDataBean.class));
						}
						return Observable.from(dataList);
					}
				})
				.filter(new Func1<MultiNewsArticleDataBean, Boolean>() {
					@Override
					public Boolean call(MultiNewsArticleDataBean dataBean) {
						time = dataBean.getBehot_time();
						if (TextUtils.isEmpty(dataBean.getSource())) {
							return false;
						}
						try {
							// 过滤头条问答新闻
							if (dataBean.getSource().contains("头条问答")
									|| dataBean.getTag().contains("ad")
									|| dataBean.getSource().contains("话题")) {
								return false;
							}
						} catch (NullPointerException e) {
							mView.error(e);
						}
						// 过滤重复新闻(与上次刷新的数据比较)
						for (MultiNewsArticleDataBean bean : dataList) {
							if (bean.getTitle().equals(dataBean.getTitle())) {
								return false;
							}
						}
						return true;
					}
				})
				.toList()
				.subscribe(list -> mView.showContent(list), throwable -> mView.error(throwable));
		addSubscrebe(rxSubscription);
	}
}
