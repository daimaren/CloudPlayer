package org.cuieney.videolife.di.component;

import android.app.Activity;

import org.cuieney.videolife.di.PerFragment;
import org.cuieney.videolife.di.module.FragmentModule;
import org.cuieney.videolife.ui.fragment.cloudmusic.DetailFragment;
import org.cuieney.videolife.ui.fragment.cloudmusic.PlayListFragment;
import org.cuieney.videolife.ui.fragment.essay.EssayHomeDetailFragment;
import org.cuieney.videolife.ui.fragment.essay.EssayHomeFragment;
import org.cuieney.videolife.ui.fragment.live.LiveDetailFragment;
import org.cuieney.videolife.ui.fragment.live.PcLiveHomeFragment;
import org.cuieney.videolife.ui.fragment.music.MusicHomeFragment;
import org.cuieney.videolife.ui.fragment.video.ToutiaoVideoDetailFragmentEx;
import org.cuieney.videolife.ui.fragment.video.ToutiaoVideoHomeFragment;
import org.cuieney.videolife.ui.fragment.video.VideoHomeFragment;

import dagger.Component;

/**
 * Created by cuieney on 16/8/7.
 */

@PerFragment
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    Activity getActivity();

    void inject(VideoHomeFragment videoFragment);
    void inject(ToutiaoVideoHomeFragment toutiaoVideoHomeFragment);
    void inject(MusicHomeFragment musicFragment);
    void inject(EssayHomeFragment essayFragment);
    void inject(EssayHomeDetailFragment homeDetailFragment);
    void inject(ToutiaoVideoDetailFragmentEx toutiaoVideoDetailFragmentEx);
    void inject(PcLiveHomeFragment pcLiveHomeFragment);
    void inject(LiveDetailFragment liveDetailFragment);
    void inject(PlayListFragment playListFragment);
    void inject(DetailFragment detailFragment);
}
