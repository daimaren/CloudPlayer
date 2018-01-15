package org.cuieney.videolife.presenter.contract;

import org.cuieney.videolife.common.base.BasePresenter;
import org.cuieney.videolife.common.base.BaseView;
import org.cuieney.videolife.entity.ToutiaoBean.MultiNewsArticleDataBean;

import java.util.List;

/**
 * Created by daimaren on 2017/10/13
 * 975808394@qq.com
 */

public interface ToutiaoVideoHomeContract {
	interface View extends BaseView{
		void showContent(List<MultiNewsArticleDataBean> dataBeen);
		void error(Throwable throwable);
	}
	interface Presenter extends BasePresenter<View>{
		/**
		 * 获取视频数据
		 */
		void getVideoData(String maxBehotTime, List<MultiNewsArticleDataBean> dataBeen);
	}
}
