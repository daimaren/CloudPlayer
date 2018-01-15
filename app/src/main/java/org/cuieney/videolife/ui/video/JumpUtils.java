package org.cuieney.videolife.ui.video;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;

import org.cuieney.videolife.R;
import org.cuieney.videolife.common.utils.LogUtil;
import org.cuieney.videolife.entity.CloudMusicBean.CloudMusicItemBean;
import org.cuieney.videolife.entity.CommonMusicBean;
import org.cuieney.videolife.entity.LiveBean.LiveListBean;
import org.cuieney.videolife.entity.MusicListBean;
import org.cuieney.videolife.entity.ToutiaoBean.MultiNewsArticleDataBean;
import org.cuieney.videolife.entity.kaiyanBean.DataBean;
import org.cuieney.videolife.entity.wyBean.TracksBean;
import org.cuieney.videolife.ui.act.DanmkuVideoActivity;
import org.cuieney.videolife.ui.act.PlayActivity;
import org.cuieney.videolife.ui.act.PlayCloudMusicActivity;
import org.cuieney.videolife.ui.act.PlayMusciActivity;
import org.cuieney.videolife.ui.act.PlayMusicActivityEx;

import java.util.List;

import static org.cuieney.videolife.ui.act.DanmkuVideoActivity.ROOM_ID;


/**
 * Created by cuieney on 2016/11/11.
 */

public class JumpUtils {

    /**
     * 跳转到视频播放 url title has_danmu room_id
     *
     * @param activity
     * @param view
     */
    public static void goToVideoPlayer(Activity activity, View view, DataBean dataBean) {
        Intent intent = new Intent(activity, PlayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PlayActivity.DATA,dataBean);
        intent.putExtras(bundle);
        intent.putExtra(PlayActivity.TRANSITION, true);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Pair pair = new Pair<>(view, PlayActivity.IMG_TRANSITION);
            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity, pair);
            ActivityCompat.startActivity(activity, intent, activityOptions.toBundle());
        } else {
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }
    }
    public static void goToDouyuVideoPlayer(Activity activity, View view, LiveListBean liveListBean) {
        //Intent intent = new Intent(activity, PlayActivity.class);
        Intent intent = new Intent(activity, DanmkuVideoActivity.class);
        Bundle bundle = new Bundle();
        DataBean dataBean = new DataBean();
        LogUtil.d("goToDouyuVideoPlayer:" + liveListBean.getUrl());
        dataBean.setPlayUrl(liveListBean.getUrl());
        dataBean.setTitle("");//liveListBean.getRoom_name()
        bundle.putString(ROOM_ID,liveListBean.getRoom_id());
        bundle.putParcelable(PlayActivity.DATA, dataBean);
        intent.putExtras(bundle);
        intent.putExtra(PlayActivity.TRANSITION, true);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Pair pair = new Pair<>(view, PlayActivity.IMG_TRANSITION);
            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity, pair);
            ActivityCompat.startActivity(activity, intent, activityOptions.toBundle());
        } else {
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }
    }
    public static void goToToutiaoVideoPlayer(Activity activity, View view, MultiNewsArticleDataBean videoBean) {
        Intent intent = new Intent(activity, PlayActivity.class);
        Bundle bundle = new Bundle();
        DataBean dataBean = new DataBean();
        LogUtil.d("goToToutiaoVideoPlayer:" + videoBean.getUrl());
        dataBean.setPlayUrl(videoBean.getUrl());
        dataBean.setTitle("");//videoBean.getTitle()
        bundle.putParcelable(PlayActivity.DATA,dataBean);
        intent.putExtras(bundle);
        intent.putExtra(PlayActivity.TRANSITION, true);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Pair pair = new Pair<>(view, PlayActivity.IMG_TRANSITION);
            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity, pair);
            ActivityCompat.startActivity(activity, intent, activityOptions.toBundle());
        } else {
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }
    }
    public static void goToMusicPlayer(Activity activity, View view, MusicListBean dataBean) {
        Intent intent = new Intent(activity, PlayMusciActivity.class);
        Bundle bundle = new Bundle();
        //不同的数据源都采用TracksBean传递音乐数据
        CommonMusicBean musicBean = new CommonMusicBean();
        //fill data
        musicBean.getTracks().addAll(dataBean.getTracks());
        bundle.putParcelable(PlayMusciActivity.DATA, musicBean);
        intent.putExtras(bundle);
        intent.putExtra(PlayActivity.TRANSITION, true);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Pair pair = new Pair<>(view, PlayActivity.IMG_TRANSITION);
            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity, pair);
            ActivityCompat.startActivity(activity, intent, activityOptions.toBundle());
        } else {
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }
    }
    public static void goToCloudMusicPlayer(Activity activity, List<CloudMusicItemBean> musicList, int position) {
        //Intent intent = new Intent(activity, PlayMusicActivityEx.class);
        Intent intent = new Intent(activity, PlayCloudMusicActivity.class);
        Bundle bundle = new Bundle();
        //不同的数据源都采用CommonMusicBean传递音乐数据
        CommonMusicBean musicBean = new CommonMusicBean();
        List<TracksBean> tracksBeans = musicBean.getTracks();
        //fill data
        for (CloudMusicItemBean bean : musicList) {
            TracksBean tracksBean = new TracksBean();
            tracksBean.setSonger(bean.getAr().get(0).getName());
            tracksBean.setSongname(bean.getName());
            tracksBean.setId(String.valueOf(bean.getId()));
            tracksBean.setSongphoto(bean.getAl().getPicUrl());
            tracksBean.setFilename("");
            tracksBeans.add(tracksBean);
        }
        bundle.putParcelable(PlayMusicActivityEx.DATA, musicBean);
        intent.putExtras(bundle);
        intent.putExtra(PlayMusicActivityEx.TRANSITION, true);
        intent.putExtra(PlayMusicActivityEx.PLAY_INDEX, position);
        activity.startActivity(intent);
    }
}
