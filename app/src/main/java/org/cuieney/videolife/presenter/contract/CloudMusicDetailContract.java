package org.cuieney.videolife.presenter.contract;

import org.cuieney.videolife.common.base.BasePresenter;
import org.cuieney.videolife.common.base.BaseView;
import org.cuieney.videolife.entity.CloudMusicBean.MusicPlayListBean;

/**
 * Created by cuieney on 17/3/4.
 */

public interface CloudMusicDetailContract {
    interface View extends BaseView {
        void showContent(MusicPlayListBean playListBean);
        void error(Throwable throwable);
    }
    interface Presenter extends BasePresenter<CloudMusicDetailContract.View> {
        void getPlayList(String type, String id);
    }
}
