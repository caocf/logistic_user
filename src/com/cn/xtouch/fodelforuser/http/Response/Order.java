package com.cn.xtouch.fodelforuser.http.Response;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

public class Order implements Parcelable {
	public String order_no;
	public String recipient_name;
	public String recipient_phone_number;
	public String address;
	public String expected_time;
	public String carrier_name;
	public String carrier_phone_number;
	public String portrait;
	public String status;

	public DeliveryTime delivery_time_list;

	public static class DeliveryTime implements Parcelable {

		public List<String> dates;

		public List<String> hours;

		public DeliveryTime(Parcel source) {
			source.readStringList(dates);
			source.readStringList(hours);
		}

		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeList(dates);
			dest.writeList(hours);
		}
		
		
		public static final Parcelable.Creator<DeliveryTime> CREATOR = new Creator<DeliveryTime>() {
			@Override
			public DeliveryTime[] newArray(int size) {
				return new DeliveryTime[size];
			}

			@Override
			public DeliveryTime createFromParcel(Parcel source) {
				return new DeliveryTime(source);
			}
		};
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(order_no);
		dest.writeString(recipient_name);
		dest.writeString(recipient_phone_number);
		dest.writeString(address);
		dest.writeString(expected_time);
		dest.writeString(carrier_phone_number);
		dest.writeString(portrait);
		dest.writeString(status);
		dest.writeParcelable(delivery_time_list, PARCELABLE_WRITE_RETURN_VALUE);
	}

	public Order(Parcel source) {
		this.order_no = source.readString();
		this.recipient_name = source.readString();
		this.recipient_phone_number = source.readString();
		this.address = source.readString();
		this.expected_time = source.readString();
		this.carrier_name = source.readString();
		this.carrier_phone_number = source.readString();
		this.portrait = source.readString();
		this.status = source.readString();
		this.delivery_time_list = source.readParcelable(DeliveryTime.class
				.getClassLoader());
	}

	public static final Parcelable.Creator<Order> CREATOR = new Creator<Order>() {
		@Override
		public Order[] newArray(int size) {
			return new Order[size];
		}

		@Override
		public Order createFromParcel(Parcel source) {
			return new Order(source);
		}
	};
}
