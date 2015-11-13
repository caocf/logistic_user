package com.cn.xtouch.fodelforuser.http.Response;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

public class OrderRouteInfo {

	public String is_show_map;

	public String lg;

	public String lt;

	public List<RouteInfo> info_list;

	public String comment_content;
	
	public String comment_satisfaction;

	public static class RouteInfo implements Parcelable {

		public String create_time;

		public String info;

		public RouteInfo(Parcel source) {
			this.create_time = source.readString();
			this.info = source.readString();
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(create_time);
			dest.writeString(info);
		}

		public static final Parcelable.Creator<RouteInfo> CREATOR = new Creator<RouteInfo>() {
			@Override
			public RouteInfo[] newArray(int size) {
				return new RouteInfo[size];
			}

			@Override
			public RouteInfo createFromParcel(Parcel source) {
				return new RouteInfo(source);
			}
		};
	}
}
