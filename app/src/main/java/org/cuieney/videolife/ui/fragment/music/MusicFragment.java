package org.cuieney.videolife.ui.fragment.music;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.cuieney.videolife.R;
import org.cuieney.videolife.common.base.BaseMainFragment;
import org.cuieney.videolife.ui.fragment.cloudmusic.HomeFragment;

/**
 * Created by paohaile on 17/2/24.
 */

public class MusicFragment extends BaseMainFragment {

    public static MusicFragment newInstance(){
        Bundle bundle = new Bundle();
        MusicFragment musicFragment = new MusicFragment();
        musicFragment.setArguments(bundle);
        return musicFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.music_fragment, container, false);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (savedInstanceState == null) {
            //loadRootFragment(R.id.fl_first_container, MusicHomeFragment.newInstance());
            //loadRootFragment(R.id.fl_first_container, PlayListFragment.newInstance());
            loadRootFragment(R.id.fl_first_container, HomeFragment.newInstance());
        }
    }
}
