package org.cuieney.videolife.presenter.contract;

import org.cuieney.videolife.common.base.BasePresenter;
import org.cuieney.videolife.common.base.BaseView;
import org.cuieney.videolife.entity.CloudMusicBean.CloudMusicInfo;
import org.cuieney.videolife.entity.CloudMusicBean.CloudMusicLrcInfo;
import org.cuieney.videolife.entity.CloudMusicBean.MusicPlayListBean;

/**
 * Created by cuieney on 17/3/4.
 */

public interface CloudMusicHomeContract {
    interface View extends BaseView {
        void showContent(MusicPlayListBean musicPlayListBean);
        void showMusicInfo(CloudMusicInfo cloudMusicInfo);
        void showLrcInfo(CloudMusicLrcInfo cloudMusicLrcInfo);
        void error(Throwable throwable);
    }

    interface Presenter extends BasePresenter<CloudMusicHomeContract.View> {
        void getMusicPlayList(String type, String id);
        void getMusicInfo(String type, String id);
        void getLrcInfo(String type, String id);
    }
}
