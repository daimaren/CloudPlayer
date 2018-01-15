package org.cuieney.videolife.presenter.contract;

import org.cuieney.videolife.common.base.BasePresenter;
import org.cuieney.videolife.common.base.BaseView;

/**
 * Created by cuieney on 17/2/24.
 */

public interface VideoContentContract {

    interface View extends BaseView{
        /**
         * 设置播放器
         */
        void onSetVideoPlayUrl(String url);
        void error(Throwable throwable);
    }

    interface Presenter extends BasePresenter<View> {
        /**
         * 解析视频播放地址
         */
        void getVideoPlayUrl(String videoid);
    }
}
