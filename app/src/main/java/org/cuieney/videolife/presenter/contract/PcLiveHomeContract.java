package org.cuieney.videolife.presenter.contract;

import org.cuieney.videolife.common.base.BasePresenter;
import org.cuieney.videolife.common.base.BaseView;
import org.cuieney.videolife.entity.LiveBean.LiveListBean;

import java.util.List;

/**
 * Created by daimaren on 2017/10/31
 * 975808394@qq.com
 */

public interface PcLiveHomeContract {
	interface View extends BaseView {
		void showContent(List<LiveListBean> liveListBeans);
		void error(Throwable throwable);
	}
	interface Presenter extends BasePresenter<View> {
		/**
		 * 获取直播list
		 */
		void getAllLiveList(int offset, int limit, List<LiveListBean> liveListBeans);
	}
}
