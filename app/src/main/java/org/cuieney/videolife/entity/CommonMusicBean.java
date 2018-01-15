package org.cuieney.videolife.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.cuieney.videolife.entity.wyBean.TracksBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daimaren on 2017/11/7
 * 975808394@qq.com
 */

public class CommonMusicBean implements Parcelable{
	private List<TracksBean> tracks;

	public List<TracksBean> getTracks() {
		return tracks;
	}

	public void setTracks(List<TracksBean> tracks) {
		this.tracks = tracks;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(tracks);
	}

	public CommonMusicBean() {
		this.tracks = new ArrayList<TracksBean>();
	}

	protected CommonMusicBean(Parcel in) {
		this.tracks = new ArrayList<TracksBean>();
		in.readList(this.tracks, TracksBean.class.getClassLoader());
	}

	public static final Parcelable.Creator<CommonMusicBean> CREATOR = new Parcelable.Creator<CommonMusicBean>() {
		@Override
		public CommonMusicBean createFromParcel(Parcel source) {
			return new CommonMusicBean(source);
		}

		@Override
		public CommonMusicBean[] newArray(int size) {
			return new CommonMusicBean[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}
}
