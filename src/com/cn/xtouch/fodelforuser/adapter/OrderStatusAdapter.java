package com.cn.xtouch.fodelforuser.adapter;

import java.util.List;

import com.cn.xtouch.fodelforuser.R;
import com.cn.xtouch.fodelforuser.http.Response.OrderRouteInfo.RouteInfo;
import com.cn.xtouch.fodelforuser.utils.UIUtils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderStatusAdapter extends BaseAdapter {

	private Context context;

	private List<RouteInfo> list;

	private boolean isShowCurrentStatus;

	public OrderStatusAdapter(Context context, List<RouteInfo> list,
			boolean isShowCurrentStatus) {

		this.context = context;

		this.list = list;

		this.isShowCurrentStatus = isShowCurrentStatus;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			convertView = View.inflate(context,
					R.layout.order_detail_status_info_item, null);
			holder = new Holder();
			holder.tv_info = (TextView) convertView.findViewById(R.id.tv_info);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.img_point = (ImageView) convertView
					.findViewById(R.id.img_point);
			holder.tv_line = (TextView) convertView.findViewById(R.id.tv_line);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		RouteInfo routeInfo = list.get(position);
		holder.tv_info.setText(routeInfo.info);
		holder.tv_time.setText(routeInfo.create_time);
		if (isShowCurrentStatus && position == 0) {
			holder.img_point.setImageDrawable(UIUtils
					.getDrawable(R.drawable.current_point_icon));
			holder.tv_info.setTextColor(UIUtils.getResources().getColor(
					R.color.orange));
			holder.tv_time.setTextColor(UIUtils.getResources().getColor(
					R.color.orange));
		} else {
			holder.img_point.setImageDrawable(UIUtils
					.getDrawable(R.drawable.passed_point_icon));
			holder.tv_info.setTextColor(UIUtils.getResources().getColor(
					R.color.deep_gray));
			holder.tv_time.setTextColor(UIUtils.getResources().getColor(
					R.color.deep_gray));
		}
		return convertView;
	}

	public static class Holder {

		public ImageView img_point;

		public TextView tv_info, tv_time, tv_line;
	}

}
