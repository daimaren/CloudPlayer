package com.shuyu.gsyvideoplayer;


import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.Surface;

import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.file.Md5FileNameGenerator;
import com.danikula.videocache.headers.HeaderInjector;
import com.shuyu.gsyvideoplayer.listener.GSYMediaPlayerListener;
import com.shuyu.gsyvideoplayer.model.GSYModel;
import com.shuyu.gsyvideoplayer.model.VideoOptionModel;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.FileUtils;
import com.shuyu.gsyvideoplayer.utils.StorageUtils;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

import tv.danmaku.ijk.media.exo.IjkExoMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkLibLoader;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.TextureMediaPlayer;

/**
 * 视频管理，单例
 * 目前使用的是IJK封装的谷歌EXOPlayer
 * Created by shuyu on 2016/11/11.
 */

public class GSYVideoManager implements IMediaPlayer.OnPreparedListener, IMediaPlayer.OnCompletionListener,
        IMediaPlayer.OnBufferingUpdateListener, IMediaPlayer.OnSeekCompleteListener, IMediaPlayer.OnErrorListener,
        IMediaPlayer.OnVideoSizeChangedListener, IMediaPlayer.OnInfoListener, CacheListener {

    public static String TAG = "GSYVideoManager";

    @SuppressLint("StaticFieldLeak")
    private static GSYVideoManager videoManager;

    private static final int HANDLER_PREPARE = 0;

    private static final int HANDLER_SETDISPLAY = 1;

    private static final int HANDLER_RELEASE = 2;

    private static final int BUFFER_TIME_OUT_ERROR = -192;//外部超时错误码

    private IMediaPlayer mediaPlayer;

    private MediaHandler mMediaHandler;

    private Handler mainThreadHandler;

    private WeakReference<GSYMediaPlayerListener> listener;

    private WeakReference<GSYMediaPlayerListener> lastListener;

    //单例模式实在不好给instance()加参数，还是直接设为静态变量吧
    //自定义so包加载类
    private static IjkLibLoader ijkLibLoader;

    //配置ijk option
    private List<VideoOptionModel> optionModelList;

    //视频代理
    private HttpProxyCacheServer proxy;

    //是否需要的自定义缓冲路径
    private File cacheFile;

    //播放的tag，防止错位置，因为普通的url也可能重复
    private String playTag = "";

    //header for cache
    private Map<String, String> mMapHeadData;

    private Context context;

    //当前播放的视频宽的高
    private int currentVideoWidth = 0;

    //当前播放的视屏的高
    private int currentVideoHeight = 0;

    //当前视频的最后状态
    private int lastState;

    //播放的tag，防止错位置，因为普通的url也可能重复
    private int playPosition = -22;

    //缓冲比例
    private int buffterPoint;

    //播放超时
    private int timeOut = 8 * 1000;

    //播放类型，默认IJK
    private int videoType = GSYVideoType.IJKPLAYER;

    //log level
    private int logLevel = IjkMediaPlayer.IJK_LOG_DEFAULT;

    //是否需要静音
    private boolean needMute = false;

    //是否需要外部超时判断
    private boolean needTimeOutOther;

    /**
     * 单例管理器
     */
    public static synchronized GSYVideoManager instance() {
        if (videoManager == null) {
            videoManager = new GSYVideoManager(ijkLibLoader);
        }
        return videoManager;
    }

    /**
     * 同步创建一个临时管理器
     */
    public static synchronized GSYVideoManager tmpInstance(GSYMediaPlayerListener listener) {
        GSYVideoManager gsyVideoManager = new GSYVideoManager(ijkLibLoader);
        gsyVideoManager.buffterPoint = videoManager.buffterPoint;
        gsyVideoManager.optionModelList = videoManager.optionModelList;
        gsyVideoManager.cacheFile = videoManager.cacheFile;
        gsyVideoManager.playTag = videoManager.playTag;
        gsyVideoManager.mMapHeadData = videoManager.mMapHeadData;
        gsyVideoManager.currentVideoWidth = videoManager.currentVideoWidth;
        gsyVideoManager.currentVideoHeight = videoManager.currentVideoHeight;
        gsyVideoManager.context = videoManager.context;
        gsyVideoManager.lastState = videoManager.lastState;
        gsyVideoManager.playPosition = videoManager.playPosition;
        gsyVideoManager.timeOut = videoManager.timeOut;
        gsyVideoManager.videoType = videoManager.videoType;
        gsyVideoManager.needMute = videoManager.needMute;
        gsyVideoManager.needTimeOutOther = videoManager.needTimeOutOther;
        gsyVideoManager.setListener(listener);
        return gsyVideoManager;
    }

    /**
     * 替换管理器
     */
    public static synchronized void changeManager(GSYVideoManager gsyVideoManager) {
        videoManager = gsyVideoManager;
    }


    /**
     * 设置自定义so包加载类
     * 需要在instance之前设置
     */
    public static void setIjkLibLoader(IjkLibLoader libLoader) {
        ijkLibLoader = libLoader;
    }

    public static IjkLibLoader getIjkLibLoader() {
        return ijkLibLoader;
    }


    /**
     * 删除默认所有缓存文件
     */
    public static void clearAllDefaultCache(Context context) {
        String path = StorageUtils.getIndividualCacheDirectory
                (context.getApplicationContext()).getAbsolutePath();
        FileUtils.deleteFiles(new File(path));
    }

    /**
     * 删除url对应默认缓存文件
     */
    public static void clearDefaultCache(Context context, String url) {
        Md5FileNameGenerator md5FileNameGenerator = new Md5FileNameGenerator();
        String name = md5FileNameGenerator.generate(url);
        String pathTmp = StorageUtils.getIndividualCacheDirectory
                (context.getApplicationContext()).getAbsolutePath()
                + File.separator + name + ".download";
        String path = StorageUtils.getIndividualCacheDirectory
                (context.getApplicationContext()).getAbsolutePath()
                + File.separator + name;
        CommonUtil.deleteFile(pathTmp);
        CommonUtil.deleteFile(path);

    }


    /**
     * 获取缓存代理服务,带文件目录的
     */
    public static HttpProxyCacheServer getProxy(Context context, File file) {

        //如果为空，返回默认的
        if (file == null) {
            return getProxy(context);
        }

        //如果已经有缓存文件路径，那么判断缓存文件路径是否一致
        if (GSYVideoManager.instance().cacheFile != null
                && !GSYVideoManager.instance().cacheFile.getAbsolutePath().equals(file.getAbsolutePath())) {
            //不一致先关了旧的
            HttpProxyCacheServer proxy = GSYVideoManager.instance().proxy;

            if (proxy != null) {
                proxy.shutdown();
            }
            //开启新的
            return (GSYVideoManager.instance().proxy =
                    GSYVideoManager.instance().newProxy(context, file));
        } else {
            //还没有缓存文件的或者一致的，返回原来
            HttpProxyCacheServer proxy = GSYVideoManager.instance().proxy;

            return proxy == null ? (GSYVideoManager.instance().proxy =
                    GSYVideoManager.instance().newProxy(context, file)) : proxy;
        }
    }


    /**
     * 获取缓存代理服务
     */
    private static HttpProxyCacheServer getProxy(Context context) {
        HttpProxyCacheServer proxy = GSYVideoManager.instance().proxy;
        return proxy == null ? (GSYVideoManager.instance().proxy =
                GSYVideoManager.instance().newProxy(context)) : proxy;
    }

    /**
     * 创建缓存代理服务,带文件目录的.
     */
    public HttpProxyCacheServer newProxy(Context context, File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
        HttpProxyCacheServer.Builder builder = new HttpProxyCacheServer.Builder(context);
        builder.cacheDirectory(file);
        builder.headerInjector(new UserAgentHeadersInjector());
        cacheFile = file;
        return builder.build();
    }

    public void setProxy(HttpProxyCacheServer proxy) {
        this.proxy = proxy;
    }

    /**
     * 创建缓存代理服务
     */
    public HttpProxyCacheServer newProxy(Context context) {
        return new HttpProxyCacheServer.Builder(context.getApplicationContext())
                .headerInjector(new UserAgentHeadersInjector()).build();
    }


    public GSYMediaPlayerListener listener() {
        if (listener == null)
            return null;
        return listener.get();
    }

    public GSYMediaPlayerListener lastListener() {
        if (lastListener == null)
            return null;
        return lastListener.get();
    }

    public void setListener(GSYMediaPlayerListener listener) {
        if (listener == null)
            this.listener = null;
        else
            this.listener = new WeakReference<>(listener);
    }

    public void setLastListener(GSYMediaPlayerListener lastListener) {
        if (lastListener == null)
            this.lastListener = null;
        else
            this.lastListener = new WeakReference<>(lastListener);
    }

    /***
     * @param libLoader 是否使用外部动态加载so
     * */
    private GSYVideoManager(IjkLibLoader libLoader) {
        mediaPlayer = (libLoader == null) ? new IjkMediaPlayer() : new IjkMediaPlayer(libLoader);
        ijkLibLoader = libLoader;
        HandlerThread mediaHandlerThread = new HandlerThread(TAG);
        mediaHandlerThread.start();
        mMediaHandler = new MediaHandler((mediaHandlerThread.getLooper()));
        mainThreadHandler = new Handler();
    }

    private class MediaHandler extends Handler {

        MediaHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_PREPARE:
                    initVideo(msg);
                    break;
                case HANDLER_SETDISPLAY:
                    showDisplay(msg);
                    break;
                case HANDLER_RELEASE:
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                    }
                    setNeedMute(false);
                    if (proxy != null) {
                        proxy.unregisterCacheListener(GSYVideoManager.this);
                    }
                    buffterPoint = 0;
                    cancelTimeOutBuffer();
                    break;
            }
        }

    }

    private void initVideo(Message msg) {
        try {
            currentVideoWidth = 0;
            currentVideoHeight = 0;
            mediaPlayer.release();

            if (videoType == GSYVideoType.IJKPLAYER) {
                initIJKPlayer(msg);
            } else if (videoType == GSYVideoType.IJKEXOPLAYER) {
                initEXOPlayer(msg);
            }
            setNeedMute(needMute);
            mediaPlayer.setOnCompletionListener(GSYVideoManager.this);
            mediaPlayer.setOnBufferingUpdateListener(GSYVideoManager.this);
            mediaPlayer.setScreenOnWhilePlaying(true);
            mediaPlayer.setOnPreparedListener(GSYVideoManager.this);
            mediaPlayer.setOnSeekCompleteListener(GSYVideoManager.this);
            mediaPlayer.setOnErrorListener(GSYVideoManager.this);
            mediaPlayer.setOnInfoListener(GSYVideoManager.this);
            mediaPlayer.setOnVideoSizeChangedListener(GSYVideoManager.this);
            mediaPlayer.prepareAsync();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initIJKPlayer(Message msg) {
        mediaPlayer = (ijkLibLoader == null) ? new IjkMediaPlayer() : new IjkMediaPlayer(ijkLibLoader);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        ((IjkMediaPlayer) mediaPlayer).setOnNativeInvokeListener(new IjkMediaPlayer.OnNativeInvokeListener() {
            @Override
            public boolean onNativeInvoke(int i, Bundle bundle) {
                return true;
            }
        });
        try {
            //开启硬解码
            if (GSYVideoType.isMediaCodec()) {
                Debuger.printfLog("enable mediaCodec");
                ((IjkMediaPlayer) mediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
                ((IjkMediaPlayer) mediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1);
                ((IjkMediaPlayer) mediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1);
            }
            mMapHeadData = ((GSYModel) msg.obj).getMapHeadData();
            ((IjkMediaPlayer) mediaPlayer).setDataSource(((GSYModel) msg.obj).getUrl(), ((GSYModel) msg.obj).getMapHeadData());
            mediaPlayer.setLooping(((GSYModel) msg.obj).isLooping());
            if (((GSYModel) msg.obj).getSpeed() != 1 && ((GSYModel) msg.obj).getSpeed() > 0) {
                ((IjkMediaPlayer) mediaPlayer).setSpeed(((GSYModel) msg.obj).getSpeed());
            }
            ((IjkMediaPlayer) mediaPlayer).native_setLogLevel(logLevel);
            initIJKOption((IjkMediaPlayer) mediaPlayer);
            //开启硬解码渲染优化
            if (GSYVideoType.isMediaCodecTexture()) {
                IMediaPlayer iMediaPlayer = new TextureMediaPlayer(mediaPlayer);
                mediaPlayer = iMediaPlayer;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initEXOPlayer(Message msg) {
        mediaPlayer = new IjkExoMediaPlayer(context);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMapHeadData = null;
        try {
            mediaPlayer.setDataSource(context, Uri.parse(((GSYModel) msg.obj).getUrl()), ((GSYModel) msg.obj).getMapHeadData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initIJKOption(IjkMediaPlayer ijkMediaPlayer) {
        if (optionModelList != null && optionModelList.size() > 0) {
            for (VideoOptionModel videoOptionModel : optionModelList) {
                if (videoOptionModel.getValueType() == VideoOptionModel.VALUE_TYPE_INT) {
                    ijkMediaPlayer.setOption(videoOptionModel.getCategory(),
                            videoOptionModel.getName(), videoOptionModel.getValueInt());
                } else {
                    ijkMediaPlayer.setOption(videoOptionModel.getCategory(),
                            videoOptionModel.getName(), videoOptionModel.getValueString());
                }
            }
        }
    }


    /**
     * 启动十秒的定时器进行 缓存操作
     */
    private void startTimeOutBuffer() {
        // 启动定时
        Debuger.printfError("startTimeOutBuffer");
        mainThreadHandler.postDelayed(mTimeOutRunnable, timeOut);

    }

    /**
     * 取消 十秒的定时器进行 缓存操作
     */
    private void cancelTimeOutBuffer() {
        Debuger.printfError("cancelTimeOutBuffer");
        // 取消定时
        if (needTimeOutOther)
            mainThreadHandler.removeCallbacks(mTimeOutRunnable);
    }


    private Runnable mTimeOutRunnable = new Runnable() {
        @Override
        public void run() {
            if (listener != null) {
                Debuger.printfError("time out for error listener");
                listener().onError(BUFFER_TIME_OUT_ERROR, BUFFER_TIME_OUT_ERROR);
            }
        }
    };


    private void showDisplay(Message msg) {
        if (msg.obj == null && mediaPlayer != null) {
            mediaPlayer.setSurface(null);
        } else {
            Surface holder = (Surface) msg.obj;
            if (mediaPlayer != null && holder.isValid()) {
                mediaPlayer.setSurface(holder);
            }
            if (mediaPlayer instanceof IjkExoMediaPlayer) {
                if (mediaPlayer != null && mediaPlayer.getDuration() > 30
                        && mediaPlayer.getCurrentPosition() < mediaPlayer.getDuration()) {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 20);
                }
            }
        }
    }

    /**
     * for android video cache header
     */
    private class UserAgentHeadersInjector implements HeaderInjector {

        @Override
        public Map<String, String> addHeaders(String url) {
            return mMapHeadData;
        }
    }

    public void prepare(final String url, final Map<String, String> mapHeadData, boolean loop, float speed) {
        if (TextUtils.isEmpty(url)) return;
        Message msg = new Message();
        msg.what = HANDLER_PREPARE;
        GSYModel fb = new GSYModel(url, mapHeadData, loop, speed);
        msg.obj = fb;
        mMediaHandler.sendMessage(msg);
        if (needTimeOutOther) {
            startTimeOutBuffer();
        }
    }

    public void releaseMediaPlayer() {
        Message msg = new Message();
        msg.what = HANDLER_RELEASE;
        mMediaHandler.sendMessage(msg);
        playTag = "";
        playPosition = -22;
    }

    public void setDisplay(Surface holder) {
        Message msg = new Message();
        msg.what = HANDLER_SETDISPLAY;
        msg.obj = holder;
        mMediaHandler.sendMessage(msg);
    }

    @Override
    public void onPrepared(IMediaPlayer mp) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                cancelTimeOutBuffer();
                if (listener != null) {
                    listener().onPrepared();
                }
            }
        });
    }

    @Override
    public void onCompletion(IMediaPlayer mp) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                cancelTimeOutBuffer();
                if (listener != null) {
                    listener().onAutoCompletion();
                }
            }
        });
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer mp, final int percent) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    if (percent > buffterPoint) {
                        listener().onBufferingUpdate(percent);
                    } else {
                        listener().onBufferingUpdate(buffterPoint);
                    }
                }
            }
        });
    }

    @Override
    public void onSeekComplete(IMediaPlayer mp) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                cancelTimeOutBuffer();
                if (listener != null) {
                    listener().onSeekComplete();
                }
            }
        });
    }

    @Override
    public boolean onError(IMediaPlayer mp, final int what, final int extra) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                cancelTimeOutBuffer();
                if (listener != null) {
                    listener().onError(what, extra);
                }
            }
        });
        return true;
    }

    @Override
    public boolean onInfo(IMediaPlayer mp, final int what, final int extra) {
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (needTimeOutOther) {
                    if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                        startTimeOutBuffer();
                    } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                        cancelTimeOutBuffer();
                    }
                }
                if (listener != null) {
                    listener().onInfo(what, extra);
                }
            }
        });
        return false;
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
        currentVideoWidth = mp.getVideoWidth();
        currentVideoHeight = mp.getVideoHeight();
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener().onVideoSizeChanged();
                }
            }
        });
    }


    @Override
    public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
        buffterPoint = percentsAvailable;
    }

    /**
     * 暂停播放
     */
    public static void onPause() {
        if (GSYVideoManager.instance().listener() != null) {
            GSYVideoManager.instance().listener().onVideoPause();
        }
    }

    /**
     * 恢复播放
     */
    public static void onResume() {
        if (GSYVideoManager.instance().listener() != null) {
            GSYVideoManager.instance().listener().onVideoResume();
        }
    }


    public IMediaPlayer getMediaPlayer() {
        if (mediaPlayer instanceof TextureMediaPlayer) {
            return ((TextureMediaPlayer) mediaPlayer).getInternalMediaPlayer();
        }
        return mediaPlayer;
    }

    public int getCurrentVideoWidth() {
        return currentVideoWidth;
    }

    public int getCurrentVideoHeight() {
        return currentVideoHeight;
    }

    public int getLastState() {
        return lastState;
    }

    public void setLastState(int lastState) {
        this.lastState = lastState;
    }

    public void setCurrentVideoHeight(int currentVideoHeight) {
        this.currentVideoHeight = currentVideoHeight;
    }

    public void setCurrentVideoWidth(int currentVideoWidth) {
        this.currentVideoWidth = currentVideoWidth;
    }

    public String getPlayTag() {
        return playTag;
    }

    public void setPlayTag(String playTag) {
        this.playTag = playTag;
    }

    public int getPlayPosition() {
        return playPosition;
    }

    public void setPlayPosition(int playPosition) {
        this.playPosition = playPosition;
    }


    public int getVideoType() {
        return videoType;
    }

    public List<VideoOptionModel> getOptionModelList() {
        return optionModelList;
    }

    /**
     * 设置IJK视频的option
     */
    public void setOptionModelList(List<VideoOptionModel> optionModelList) {
        this.optionModelList = optionModelList;
    }

    /**
     * 设置了视频的播放类型
     * GSYVideoType IJKPLAYER = 0 or IJKEXOPLAYER = 1;
     */
    public void setVideoType(Context context, int videoType) {
        this.context = context.getApplicationContext();
        this.videoType = videoType;
    }

    public boolean isNeedMute() {
        return needMute;
    }

    /**
     * 是否需要静音
     */
    public void setNeedMute(boolean needMute) {
        this.needMute = needMute;
        if (mediaPlayer != null) {
            if (needMute) {
                mediaPlayer.setVolume(0, 0);
            } else {
                mediaPlayer.setVolume(1, 1);
            }
        }
    }

    public int getTimeOut() {
        return timeOut;
    }

    public boolean isNeedTimeOutOther() {
        return needTimeOutOther;
    }

    /**
     * 是否需要在buffer缓冲时，增加外部超时判断
     * <p>
     * 超时后会走onError接口，播放器通过onPlayError回调出
     * <p>
     * 错误码为 ： BUFFER_TIME_OUT_ERROR = -192
     * <p>
     * 由于onError之后执行GSYVideoPlayer的OnError，如果不想触发错误，
     * 可以重载onError，在super之前拦截处理。
     * <p>
     * public void onError(int what, int extra){
     * do you want before super and return;
     * super.onError(what, extra)
     * }
     *
     * @param timeOut          超时时间，毫秒 默认8000
     * @param needTimeOutOther 是否需要延时设置，默认关闭
     */
    public void setTimeOut(int timeOut, boolean needTimeOutOther) {
        this.timeOut = timeOut;
        this.needTimeOutOther = needTimeOutOther;
    }

    /**
     * 设置log输入等级
     *
     */
    public void setLogLevel(int logLevel) {
        if (mediaPlayer != null && mediaPlayer instanceof IjkMediaPlayer) {
            this.logLevel = logLevel;
            ((IjkMediaPlayer) mediaPlayer).native_setLogLevel(logLevel);
        }
    }

}