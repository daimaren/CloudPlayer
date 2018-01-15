package org.cuieney.videolife.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.zhy.autolayout.utils.L;

import org.cuieney.videolife.common.api.LiveApiService;
import org.cuieney.videolife.common.base.RxPresenter;
import org.cuieney.videolife.entity.LiveBean.TempLiveVideoBean;
import org.cuieney.videolife.presenter.contract.LiveVideoInfoContract;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by daimaren on 2017/10/13
 * 975808394@qq.com
 */

public class LiveVideoInfoPresenter extends RxPresenter< LiveVideoInfoContract.View> implements  LiveVideoInfoContract.Presenter{
	private LiveApiService mRetrofitHelper;
	private Gson gson = new Gson();
	private String time;
	@Inject
	public LiveVideoInfoPresenter(LiveApiService mRetrofitHelper) {
		this.mRetrofitHelper = mRetrofitHelper;
	}
	public Request getLiveVideoInfoRequest(String room_id) {
		String str="https://m.douyu.com/html5/live?roomId="+room_id;
		Request requestPost = new Request.Builder()
				.url(str)
				.get()
				.build();
		return requestPost;
	}
	@Override
	public void getLiveVideoInfo(String room_id) {
		OkHttpClient client = new OkHttpClient.Builder()
				.connectTimeout(20, TimeUnit.SECONDS)
				.readTimeout(20, TimeUnit.SECONDS)
				.build();
		client.newCall(getLiveVideoInfoRequest(room_id)).enqueue(new okhttp3.Callback() {
			@Override
			public void onFailure(okhttp3.Call call, IOException e) {
				Log.e("error",e.getMessage()+"---");
				L.e("错误信息:"+e.getMessage());
				mView.error(e);
			}
			@Override
			public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
				String json =response.body().string().toString();
				Log.e("onResponse",json);
				try {
					JSONObject jsonObject = new JSONObject(json);
					if (jsonObject.getInt("error")==0) {
						Gson gson = new Gson();
						TempLiveVideoBean mLiveVideoInfo = gson.fromJson(json, TempLiveVideoBean.class);
						mView.SetLiveVideoInfo(mLiveVideoInfo);
					} else {

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
