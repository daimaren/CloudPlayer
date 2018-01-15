package org.cuieney.videolife.entity.CloudMusicBean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daimaren on 2017/11/7
 * 975808394@qq.com
 */

public class CloudMusicItemBean  implements Parcelable {
	private String name;
	private int id;
	private int pst;
	private int t;
	private double pop;
	private int st;
	private String rt;
	private int fee;
	private int v;
	private Object crbt;
	private String cf;
	private AlBean al;
	private int dt;
	private HBean h;
	private MBean m;
	private LBean l;
	private Object a;
	private String cd;
	private int no;
	private Object rtUrl;
	private int ftype;
	private int djId;
	private int copyright;
	private int s_id;
	private int mst;
	private int cp;
	private int mv;
	private int rtype;
	private Object rurl;
	private java.util.List<ArBean> ar;
	private java.util.List<String> alia;
	private java.util.List<?> rtUrls;
	private java.util.List<String> tns;

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.name);
		dest.writeInt(id);
		dest.writeList(ar);
	}

	public CloudMusicItemBean() {
	}

	protected CloudMusicItemBean(Parcel in) {
		this.name = in.readString();
		this.id = in.readInt();
		this.ar = new ArrayList<ArBean>();
		in.readList(this.ar, ArBean.class.getClassLoader());
	}

	public static final Parcelable.Creator<CloudMusicItemBean> CREATOR = new Parcelable.Creator<CloudMusicItemBean>() {
		@Override
		public CloudMusicItemBean createFromParcel(Parcel source) {
			return new CloudMusicItemBean(source);
		}

		@Override
		public CloudMusicItemBean[] newArray(int size) {
			return new CloudMusicItemBean[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
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

	public int getPst() {
		return pst;
	}

	public void setPst(int pst) {
		this.pst = pst;
	}

	public int getT() {
		return t;
	}

	public void setT(int t) {
		this.t = t;
	}

	public double getPop() {
		return pop;
	}

	public void setPop(double pop) {
		this.pop = pop;
	}

	public int getSt() {
		return st;
	}

	public void setSt(int st) {
		this.st = st;
	}

	public String getRt() {
		return rt;
	}

	public void setRt(String rt) {
		this.rt = rt;
	}

	public int getFee() {
		return fee;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

	public int getV() {
		return v;
	}

	public void setV(int v) {
		this.v = v;
	}

	public Object getCrbt() {
		return crbt;
	}

	public void setCrbt(Object crbt) {
		this.crbt = crbt;
	}

	public String getCf() {
		return cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

	public AlBean getAl() {
		return al;
	}

	public void setAl(AlBean al) {
		this.al = al;
	}

	public int getDt() {
		return dt;
	}

	public void setDt(int dt) {
		this.dt = dt;
	}

	public HBean getH() {
		return h;
	}

	public void setH(HBean h) {
		this.h = h;
	}

	public MBean getM() {
		return m;
	}

	public void setM(MBean m) {
		this.m = m;
	}

	public LBean getL() {
		return l;
	}

	public void setL(LBean l) {
		this.l = l;
	}

	public Object getA() {
		return a;
	}

	public void setA(Object a) {
		this.a = a;
	}

	public String getCd() {
		return cd;
	}

	public void setCd(String cd) {
		this.cd = cd;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public Object getRtUrl() {
		return rtUrl;
	}

	public void setRtUrl(Object rtUrl) {
		this.rtUrl = rtUrl;
	}

	public int getFtype() {
		return ftype;
	}

	public void setFtype(int ftype) {
		this.ftype = ftype;
	}

	public int getDjId() {
		return djId;
	}

	public void setDjId(int djId) {
		this.djId = djId;
	}

	public int getCopyright() {
		return copyright;
	}

	public void setCopyright(int copyright) {
		this.copyright = copyright;
	}

	public int getS_id() {
		return s_id;
	}

	public void setS_id(int s_id) {
		this.s_id = s_id;
	}

	public int getMst() {
		return mst;
	}

	public void setMst(int mst) {
		this.mst = mst;
	}

	public int getCp() {
		return cp;
	}

	public void setCp(int cp) {
		this.cp = cp;
	}

	public int getMv() {
		return mv;
	}

	public void setMv(int mv) {
		this.mv = mv;
	}

	public int getRtype() {
		return rtype;
	}

	public void setRtype(int rtype) {
		this.rtype = rtype;
	}

	public Object getRurl() {
		return rurl;
	}

	public void setRurl(Object rurl) {
		this.rurl = rurl;
	}

	public List<ArBean> getAr() {
		return ar;
	}

	public void setAr(List<ArBean> ar) {
		this.ar = ar;
	}

	public List<String> getAlia() {
		return alia;
	}

	public void setAlia(List<String> alia) {
		this.alia = alia;
	}

	public List<?> getRtUrls() {
		return rtUrls;
	}

	public void setRtUrls(List<?> rtUrls) {
		this.rtUrls = rtUrls;
	}

	public List<String> getTns() {
		return tns;
	}

	public void setTns(List<String> tns) {
		this.tns = tns;
	}

	public static class AlBean {
		private int id;
		private String name;
		private String picUrl;
		private long pic;
		private java.util.List<?> tns;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPicUrl() {
			return picUrl;
		}

		public void setPicUrl(String picUrl) {
			this.picUrl = picUrl;
		}

		public long getPic() {
			return pic;
		}

		public void setPic(long pic) {
			this.pic = pic;
		}

		public List<?> getTns() {
			return tns;
		}

		public void setTns(List<?> tns) {
			this.tns = tns;
		}
	}

	public static class HBean {
		private int br;
		private int fid;
		private int size;
		private double vd;

		public int getBr() {
			return br;
		}

		public void setBr(int br) {
			this.br = br;
		}

		public int getFid() {
			return fid;
		}

		public void setFid(int fid) {
			this.fid = fid;
		}

		public int getSize() {
			return size;
		}

		public void setSize(int size) {
			this.size = size;
		}

		public double getVd() {
			return vd;
		}

		public void setVd(double vd) {
			this.vd = vd;
		}
	}

	public static class MBean {
		private int br;
		private int fid;
		private int size;
		private double vd;

		public int getBr() {
			return br;
		}

		public void setBr(int br) {
			this.br = br;
		}

		public int getFid() {
			return fid;
		}

		public void setFid(int fid) {
			this.fid = fid;
		}

		public int getSize() {
			return size;
		}

		public void setSize(int size) {
			this.size = size;
		}

		public double getVd() {
			return vd;
		}

		public void setVd(double vd) {
			this.vd = vd;
		}
	}

	public static class LBean {
		private int br;
		private int fid;
		private int size;
		private double vd;

		public int getBr() {
			return br;
		}

		public void setBr(int br) {
			this.br = br;
		}

		public int getFid() {
			return fid;
		}

		public void setFid(int fid) {
			this.fid = fid;
		}

		public int getSize() {
			return size;
		}

		public void setSize(int size) {
			this.size = size;
		}

		public double getVd() {
			return vd;
		}

		public void setVd(double vd) {
			this.vd = vd;
		}
	}

	public static class ArBean implements Parcelable{
		private int id;
		private String name;
		private java.util.List<?> tns;
		private java.util.List<?> alias;

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(this.name);
			dest.writeInt(id);
		}

		public ArBean() {
		}

		protected ArBean(Parcel in) {
			this.name = in.readString();
			this.id = in.readInt();
		}

		public static final Parcelable.Creator<ArBean> CREATOR = new Parcelable.Creator<ArBean>() {
			@Override
			public ArBean createFromParcel(Parcel source) {
				return new ArBean(source);
			}

			@Override
			public ArBean[] newArray(int size) {
				return new ArBean[size];
			}
		};

		@Override
		public int describeContents() {
			return 0;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<?> getTns() {
			return tns;
		}

		public void setTns(List<?> tns) {
			this.tns = tns;
		}

		public List<?> getAlias() {
			return alias;
		}

		public void setAlias(List<?> alias) {
			this.alias = alias;
		}
	}
}
