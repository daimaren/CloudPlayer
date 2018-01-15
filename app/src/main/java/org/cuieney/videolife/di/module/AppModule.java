package org.cuieney.videolife.di.module;

import android.content.Context;

import org.cuieney.videolife.App;
import org.cuieney.videolife.common.api.CloudMusicApiService;
import org.cuieney.videolife.common.api.KyApiService;
import org.cuieney.videolife.common.api.LiveApiService;
import org.cuieney.videolife.common.api.OpApiService;
import org.cuieney.videolife.common.api.ToutiaoVideoApiService;
import org.cuieney.videolife.common.api.WyApiService;
import org.cuieney.videolife.di.RetrofitHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class AppModule {

    public final App mApp;


    public AppModule(App mApp) {
        this.mApp = mApp;
    }

    @Provides
    @Singleton
    Context providesContext() {
        return mApp;
    }


    @Provides
    KyApiService proviesKyService(RetrofitHelper retrofitHelper){
        return retrofitHelper.getKyApiService();
    }

    @Provides
    WyApiService proviesWyService(RetrofitHelper retrofitHelper){
        return retrofitHelper.getWyApiService();
    }


    @Provides
    OpApiService proviesOpService(RetrofitHelper retrofitHelper){
        return retrofitHelper.getOpApiService();
    }
    @Provides
    ToutiaoVideoApiService proviesToutiaoVideoApiService(RetrofitHelper retrofitHelper){
        return retrofitHelper.getToutiaoVideoApiService();
    }
    @Provides
    LiveApiService providesLiveApiService(RetrofitHelper retrofitHelper){
        return retrofitHelper.getLiveApiService();
    }
    @Provides
    CloudMusicApiService providesCloudMusicApiService(RetrofitHelper retrofitHelper){
        return retrofitHelper.getCloudMusicApiService();
    }
}
