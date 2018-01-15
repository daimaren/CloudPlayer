package org.cuieney.videolife.entity.CloudMusicBean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daimaren on 2017/11/6
 * 975808394@qq.com
 */

public class MusicPlayListBean {

	private PlaylistBean playlist;
	private int code;
	public  PlaylistBean getPlaylistBean(){
		return playlist;
	}
	public static class PlaylistBean implements Parcelable{
		private boolean subscribed;
		private CreatorBean creator;
		private String coverImgUrl;
		private boolean highQuality;
		private long trackUpdateTime;
		private int trackCount;
		private long updateTime;
		private String commentThreadId;
		private long coverImgId;
		private long createTime;
		private int playCount;
		private int privacy;
		private boolean newImported;
		private int specialType;
		private int userId;
		private int status;
		private boolean ordered;
		private int subscribedCount;
		private int cloudTrackCount;
		private int adType;
		private long trackNumberUpdateTime;
		private Object description;
		private String name;
		private int id;
		private int shareCount;
		private int commentCount;
		private java.util.List<?> subscribers;
		private java.util.List<CloudMusicItemBean> tracks;
		private java.util.List<TrackIdsBean> trackIds;
		private java.util.List<?> tags;

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(this.name);
			dest.writeInt(id);
			dest.writeList(tracks);
		}

		public PlaylistBean() {
		}

		protected PlaylistBean(Parcel in) {
			this.name = in.readString();
			this.id = in.readInt();
			this.tracks = new ArrayList<CloudMusicItemBean>();
			in.readList(this.tracks, CloudMusicItemBean.class.getClassLoader());
		}

		public static final Parcelable.Creator<PlaylistBean> CREATOR = new Parcelable.Creator<PlaylistBean>() {
			@Override
			public PlaylistBean createFromParcel(Parcel source) {
				return new PlaylistBean(source);
			}

			@Override
			public PlaylistBean[] newArray(int size) {
				return new PlaylistBean[size];
			}
		};

		@Override
		public int describeContents() {
			return 0;
		}

		public boolean isSubscribed() {
			return subscribed;
		}

		public void setSubscribed(boolean subscribed) {
			this.subscribed = subscribed;
		}

		public CreatorBean getCreator() {
			return creator;
		}

		public void setCreator(CreatorBean creator) {
			this.creator = creator;
		}

		public String getCoverImgUrl() {
			return coverImgUrl;
		}

		public void setCoverImgUrl(String coverImgUrl) {
			this.coverImgUrl = coverImgUrl;
		}

		public boolean isHighQuality() {
			return highQuality;
		}

		public void setHighQuality(boolean highQuality) {
			this.highQuality = highQuality;
		}

		public long getTrackUpdateTime() {
			return trackUpdateTime;
		}

		public void setTrackUpdateTime(long trackUpdateTime) {
			this.trackUpdateTime = trackUpdateTime;
		}

		public int getTrackCount() {
			return trackCount;
		}

		public void setTrackCount(int trackCount) {
			this.trackCount = trackCount;
		}

		public long getUpdateTime() {
			return updateTime;
		}

		public void setUpdateTime(long updateTime) {
			this.updateTime = updateTime;
		}

		public String getCommentThreadId() {
			return commentThreadId;
		}

		public void setCommentThreadId(String commentThreadId) {
			this.commentThreadId = commentThreadId;
		}

		public long getCoverImgId() {
			return coverImgId;
		}

		public void setCoverImgId(long coverImgId) {
			this.coverImgId = coverImgId;
		}

		public long getCreateTime() {
			return createTime;
		}

		public void setCreateTime(long createTime) {
			this.createTime = createTime;
		}

		public int getPlayCount() {
			return playCount;
		}

		public void setPlayCount(int playCount) {
			this.playCount = playCount;
		}

		public int getPrivacy() {
			return privacy;
		}

		public void setPrivacy(int privacy) {
			this.privacy = privacy;
		}

		public boolean isNewImported() {
			return newImported;
		}

		public void setNewImported(boolean newImported) {
			this.newImported = newImported;
		}

		public int getSpecialType() {
			return specialType;
		}

		public void setSpecialType(int specialType) {
			this.specialType = specialType;
		}

		public int getUserId() {
			return userId;
		}

		public void setUserId(int userId) {
			this.userId = userId;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public boolean isOrdered() {
			return ordered;
		}

		public void setOrdered(boolean ordered) {
			this.ordered = ordered;
		}

		public int getSubscribedCount() {
			return subscribedCount;
		}

		public void setSubscribedCount(int subscribedCount) {
			this.subscribedCount = subscribedCount;
		}

		public int getCloudTrackCount() {
			return cloudTrackCount;
		}

		public void setCloudTrackCount(int cloudTrackCount) {
			this.cloudTrackCount = cloudTrackCount;
		}

		public int getAdType() {
			return adType;
		}

		public void setAdType(int adType) {
			this.adType = adType;
		}

		public long getTrackNumberUpdateTime() {
			return trackNumberUpdateTime;
		}

		public void setTrackNumberUpdateTime(long trackNumberUpdateTime) {
			this.trackNumberUpdateTime = trackNumberUpdateTime;
		}

		public Object getDescription() {
			return description;
		}

		public void setDescription(Object description) {
			this.description = description;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getShareCount() {
			return shareCount;
		}

		public void setShareCount(int shareCount) {
			this.shareCount = shareCount;
		}

		public int getCommentCount() {
			return commentCount;
		}

		public void setCommentCount(int commentCount) {
			this.commentCount = commentCount;
		}

		public List<?> getSubscribers() {
			return subscribers;
		}

		public void setSubscribers(List<?> subscribers) {
			this.subscribers = subscribers;
		}

		public List<CloudMusicItemBean> getCloudMusicItemBean() {
			return tracks;
		}

		public void setTracks(List<CloudMusicItemBean> tracks) {
			this.tracks = tracks;
		}

		public List<TrackIdsBean> getTrackIds() {
			return trackIds;
		}

		public void setTrackIds(List<TrackIdsBean> trackIds) {
			this.trackIds = trackIds;
		}

		public List<?> getTags() {
			return tags;
		}

		public void setTags(List<?> tags) {
			this.tags = tags;
		}

		public static class CreatorBean {
			private boolean defaultAvatar;
			private int province;
			private int authStatus;
			private boolean followed;
			private String avatarUrl;
			private int accountStatus;
			private int gender;
			private int city;
			private long birthday;
			private int userId;
			private int userType;
			private String nickname;
			private String signature;
			private String description;
			private String detailDescription;
			private long avatarImgId;
			private long backgroundImgId;
			private String backgroundUrl;
			private int authority;
			private boolean mutual;
			private Object expertTags;
			private Object experts;
			private int djStatus;
			private int vipType;
			private Object remarkName;
			private String avatarImgIdStr;
			private String backgroundImgIdStr;

			public boolean isDefaultAvatar() {
				return defaultAvatar;
			}

			public void setDefaultAvatar(boolean defaultAvatar) {
				this.defaultAvatar = defaultAvatar;
			}

			public int getProvince() {
				return province;
			}

			public void setProvince(int province) {
				this.province = province;
			}

			public int getAuthStatus() {
				return authStatus;
			}

			public void setAuthStatus(int authStatus) {
				this.authStatus = authStatus;
			}

			public boolean isFollowed() {
				return followed;
			}

			public void setFollowed(boolean followed) {
				this.followed = followed;
			}

			public String getAvatarUrl() {
				return avatarUrl;
			}

			public void setAvatarUrl(String avatarUrl) {
				this.avatarUrl = avatarUrl;
			}

			public int getAccountStatus() {
				return accountStatus;
			}

			public void setAccountStatus(int accountStatus) {
				this.accountStatus = accountStatus;
			}

			public int getGender() {
				return gender;
			}

			public void setGender(int gender) {
				this.gender = gender;
			}

			public int getCity() {
				return city;
			}

			public void setCity(int city) {
				this.city = city;
			}

			public long getBirthday() {
				return birthday;
			}

			public void setBirthday(long birthday) {
				this.birthday = birthday;
			}

			public int getUserId() {
				return userId;
			}

			public void setUserId(int userId) {
				this.userId = userId;
			}

			public int getUserType() {
				return userType;
			}

			public void setUserType(int userType) {
				this.userType = userType;
			}

			public String getNickname() {
				return nickname;
			}

			public void setNickname(String nickname) {
				this.nickname = nickname;
			}

			public String getSignature() {
				return signature;
			}

			public void setSignature(String signature) {
				this.signature = signature;
			}

			public String getDescription() {
				return description;
			}

			public void setDescription(String description) {
				this.description = description;
			}

			public String getDetailDescription() {
				return detailDescription;
			}

			public void setDetailDescription(String detailDescription) {
				this.detailDescription = detailDescription;
			}

			public long getAvatarImgId() {
				return avatarImgId;
			}

			public void setAvatarImgId(long avatarImgId) {
				this.avatarImgId = avatarImgId;
			}

			public long getBackgroundImgId() {
				return backgroundImgId;
			}

			public void setBackgroundImgId(long backgroundImgId) {
				this.backgroundImgId = backgroundImgId;
			}

			public String getBackgroundUrl() {
				return backgroundUrl;
			}

			public void setBackgroundUrl(String backgroundUrl) {
				this.backgroundUrl = backgroundUrl;
			}

			public int getAuthority() {
				return authority;
			}

			public void setAuthority(int authority) {
				this.authority = authority;
			}

			public boolean isMutual() {
				return mutual;
			}

			public void setMutual(boolean mutual) {
				this.mutual = mutual;
			}

			public Object getExpertTags() {
				return expertTags;
			}

			public void setExpertTags(Object expertTags) {
				this.expertTags = expertTags;
			}

			public Object getExperts() {
				return experts;
			}

			public void setExperts(Object experts) {
				this.experts = experts;
			}

			public int getDjStatus() {
				return djStatus;
			}

			public void setDjStatus(int djStatus) {
				this.djStatus = djStatus;
			}

			public int getVipType() {
				return vipType;
			}

			public void setVipType(int vipType) {
				this.vipType = vipType;
			}

			public Object getRemarkName() {
				return remarkName;
			}

			public void setRemarkName(Object remarkName) {
				this.remarkName = remarkName;
			}

			public String getAvatarImgIdStr() {
				return avatarImgIdStr;
			}

			public void setAvatarImgIdStr(String avatarImgIdStr) {
				this.avatarImgIdStr = avatarImgIdStr;
			}

			public String getBackgroundImgIdStr() {
				return backgroundImgIdStr;
			}

			public void setBackgroundImgIdStr(String backgroundImgIdStr) {
				this.backgroundImgIdStr = backgroundImgIdStr;
			}
		}

		public static class TrackIdsBean {
			private int id;
			private int v;

			public int getId() {
				return id;
			}

			public void setId(int id) {
				this.id = id;
			}

			public int getV() {
				return v;
			}

			public void setV(int v) {
				this.v = v;
			}
		}
	}
}