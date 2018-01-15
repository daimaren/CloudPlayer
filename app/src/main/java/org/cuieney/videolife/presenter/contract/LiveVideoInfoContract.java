package org.cuieney.videolife.presenter.contract;

import org.cuieney.videolife.common.base.BasePresenter;
import org.cuieney.videolife.common.base.BaseView;
import org.cuieney.videolife.entity.LiveBean.TempLiveVideoBean;

/**
 * Created by daimaren on 2017/10/31
 * 975808394@qq.com
 */

public interface LiveVideoInfoContract {
	interface View extends BaseView {
		void SetLiveVideoInfo(TempLiveVideoBean liveVideoInfo);
		void error(Throwable throwable);
	}
	interface Presenter extends BasePresenter<View> {
		/**
		 * 获取视频数据
		 */
		void getLiveVideoInfo(String room_id);
	}
}
