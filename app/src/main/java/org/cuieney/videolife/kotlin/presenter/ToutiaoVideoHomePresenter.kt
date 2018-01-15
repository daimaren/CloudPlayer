package org.cuieney.videolife.kotlin.presenter

import org.cuieney.videolife.common.api.ToutiaoVideoApiService
import org.cuieney.videolife.common.utils.RxUtil
import org.cuieney.videolife.entity.ToutiaoBean.ToutiaoVideoListBean
import org.cuieney.videolife.kotlin.base.RxPresenter
import org.cuieney.videolife.kotlin.presenter.contract.ToutiaoVideoHomeContract
import javax.inject.Inject

/**
 * Created by cuieney on 2017/6/2.
 */
class ToutiaoVideoHomePresenter
@Inject constructor(val retrofitHelper: ToutiaoVideoApiService) : RxPresenter(), ToutiaoVideoHomeContract.Presenter {

    override fun getVideoData(date: String, view: ToutiaoVideoHomeContract.View) {
        val rxSubscription = retrofitHelper.getVideoList("video","")
                .compose<ToutiaoVideoListBean>(RxUtil.rxSchedulerHelper<ToutiaoVideoListBean>())
                .subscribe({ t -> view.showContent(t) }) { throwable -> view.error(throwable) }
        addSubscrebe(rxSubscription)
    }
}