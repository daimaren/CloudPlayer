package org.cuieney.videolife.entity.CloudMusicBean;

/**
 * Created by daimaren on 2017/11/23
 * 975808394@qq.com
 */

public class CloudMusicLrcInfo {

	private boolean sgc;
	private boolean sfy;
	private boolean qfy;
	private LyricUserBean lyricUser;
	private LrcBean lrc;
	private KlyricBean klyric;
	private TlyricBean tlyric;
	private int code;

	public boolean isSgc() {
		return sgc;
	}

	public void setSgc(boolean sgc) {
		this.sgc = sgc;
	}

	public boolean isSfy() {
		return sfy;
	}

	public void setSfy(boolean sfy) {
		this.sfy = sfy;
	}

	public boolean isQfy() {
		return qfy;
	}

	public void setQfy(boolean qfy) {
		this.qfy = qfy;
	}

	public LyricUserBean getLyricUser() {
		return lyricUser;
	}

	public void setLyricUser(LyricUserBean lyricUser) {
		this.lyricUser = lyricUser;
	}

	public LrcBean getLrc() {
		return lrc;
	}

	public void setLrc(LrcBean lrc) {
		this.lrc = lrc;
	}

	public KlyricBean getKlyric() {
		return klyric;
	}

	public void setKlyric(KlyricBean klyric) {
		this.klyric = klyric;
	}

	public TlyricBean getTlyric() {
		return tlyric;
	}

	public void setTlyric(TlyricBean tlyric) {
		this.tlyric = tlyric;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public static class LyricUserBean {
		private int id;
		private int status;
		private int demand;
		private int userid;
		private String nickname;
		private long uptime;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public int getDemand() {
			return demand;
		}

		public void setDemand(int demand) {
			this.demand = demand;
		}

		public int getUserid() {
			return userid;
		}

		public void setUserid(int userid) {
			this.userid = userid;
		}

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

		public long getUptime() {
			return uptime;
		}

		public void setUptime(long uptime) {
			this.uptime = uptime;
		}
	}

	public static class LrcBean {
		private int version;
		private String lyric;

		public int getVersion() {
			return version;
		}

		public void setVersion(int version) {
			this.version = version;
		}

		public String getLyric() {
			return lyric;
		}

		public void setLyric(String lyric) {
			this.lyric = lyric;
		}
	}

	public static class KlyricBean {
		private int version;
		private String lyric;

		public int getVersion() {
			return version;
		}

		public void setVersion(int version) {
			this.version = version;
		}

		public String getLyric() {
			return lyric;
		}

		public void setLyric(String lyric) {
			this.lyric = lyric;
		}
	}

	public static class TlyricBean {
		private int version;
		private Object lyric;

		public int getVersion() {
			return version;
		}

		public void setVersion(int version) {
			this.version = version;
		}

		public Object getLyric() {
			return lyric;
		}

		public void setLyric(Object lyric) {
			this.lyric = lyric;
		}
	}
}
