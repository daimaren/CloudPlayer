package org.cuieney.videolife;

import android.app.Activity;
import android.app.Application;
import android.app.UiModeManager;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.cuieney.videolife.di.component.AppComponent;
import org.cuieney.videolife.di.component.DaggerAppComponent;
import org.cuieney.videolife.di.module.AppModule;
import org.cuieney.videolife.di.module.RetrofitModule;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import okhttp3.Cache;
import okhttp3.OkHttpClient;


/**
 * Created by cuieney on 17/2/21.
 */

public class App extends Application {

    private AppComponent appComponent;
    private static App app;
    private Set<Activity> allActivities;

    public static int SCREEN_WIDTH = -1;
    public static int SCREEN_HEIGHT = -1;
    public static float DIMEN_RATE = -1.0F;
    public static int DIMEN_DPI = -1;
    private static OkHttpClient mHttpClient;
    private static final long SIZE_OF_HTTP_CACHE = 10 * 1024 * 1024;
    public static App getInstance() {
        return app;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void addActivity(Activity act) {
        if (allActivities == null) {
            allActivities = new HashSet<>();
        }
        allActivities.add(act);
    }

    public void removeActivity(Activity act) {
        if (allActivities != null) {
            allActivities.remove(act);
        }
    }

    public void exitApp() {
        if (allActivities != null) {
            synchronized (allActivities) {
                for (Activity act : allActivities) {
                    act.finish();
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public void getScreenSize() {
        WindowManager windowManager = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(dm);
        DIMEN_RATE = dm.density / 1.0F;
        DIMEN_DPI = dm.densityDpi;
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;
        if(SCREEN_WIDTH > SCREEN_HEIGHT) {
            int t = SCREEN_HEIGHT;
            SCREEN_HEIGHT = SCREEN_WIDTH;
            SCREEN_WIDTH = t;
        }
    }



    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        initAppComponent();
        Fresco.initialize(this);
        mHttpClient = new OkHttpClient();
        initHttpClient(mHttpClient, app);
//        AutoLayoutConifg.getInstance().useDeviceSize();
//        getScreenSize();

//        //内存泄漏检测
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);
//        BlockCanary.install(this, new AppBlockCanaryContext()).start();

//        CrashHandler.getInstance().initCrashHandler(this);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    private void initAppComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .retrofitModule(new RetrofitModule(this))
                .build();
    }
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public static OkHttpClient getHttpClient() {
        return mHttpClient;
    }
    private void initHttpClient(OkHttpClient client, Context context) {
        File httpCacheDirectory = context.getCacheDir();
        Cache httpResponseCache = new Cache(httpCacheDirectory, SIZE_OF_HTTP_CACHE);
    }
    public static boolean isRunningOnTV(){
        UiModeManager uiModeManager = (UiModeManager)app.getSystemService(UI_MODE_SERVICE);
        return uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION;
    }
}
