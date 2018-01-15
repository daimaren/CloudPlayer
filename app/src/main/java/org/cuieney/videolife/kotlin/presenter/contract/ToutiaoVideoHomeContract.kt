package org.cuieney.videolife.kotlin.presenter.contract

import org.cuieney.videolife.entity.ToutiaoBean.ToutiaoVideoListBean
import org.cuieney.videolife.kotlin.base.BasePresenter
import org.cuieney.videolife.kotlin.base.BaseView

/**
 * Created by cuieney on 2017/6/2.
 */
interface ToutiaoVideoHomeContract {
    interface View : BaseView {
        fun showContent(data: ToutiaoVideoListBean)
        fun error(throwable: Throwable)
    }

    interface Presenter : BasePresenter {
        fun getVideoData(date: String, view: View)
    }
}