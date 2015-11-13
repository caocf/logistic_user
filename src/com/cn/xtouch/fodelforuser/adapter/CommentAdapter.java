package com.cn.xtouch.fodelforuser.adapter;

import java.util.List;

import com.cn.xtouch.fodelforuser.R;
import com.cn.xtouch.fodelforuser.activity.LogisticCommentActivity;
import com.cn.xtouch.fodelforuser.constant.Constants;
import com.cn.xtouch.fodelforuser.http.Response.Order;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter {

	private Context context;
	private List<Order> list;

	public CommentAdapter(Context context, List<Order> list) {
		this.context = context;
		this.list = list;
	}

	public void setList(List<Order> list) {
		this.list = list;
		notifyDataSetChanged();
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
			convertView = View.inflate(context, R.layout.comment_item_view,
					null);
			holder = new Holder();
			holder.tv_order_no = (TextView) convertView
					.findViewById(R.id.tv_order_no);
			holder.tv_expected_time = (TextView) convertView
					.findViewById(R.id.tv_expected_time);
			holder.tv_recipient_name = (TextView) convertView
					.findViewById(R.id.tv_recipient_name);
			holder.tv_recipient_phone_number = (TextView) convertView
					.findViewById(R.id.tv_recipient_phone_number);
			holder.tv_address = (TextView) convertView
					.findViewById(R.id.tv_address);
			holder.comment_btn = (RelativeLayout) convertView
					.findViewById(R.id.comment_btn);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final Order order = list.get(position);
		holder.tv_order_no.setText(order.order_no);
		holder.tv_expected_time.setText(order.expected_time);
		holder.tv_recipient_name.setText(order.recipient_name);
		holder.tv_recipient_phone_number.setText(order.recipient_phone_number);
		holder.tv_address.setText(order.address);
		holder.comment_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,
						LogisticCommentActivity.class);
				intent.putExtra(Constants.ORDER_NO, order.order_no);
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	public static class Holder {
		public TextView tv_order_no, tv_expected_time, tv_recipient_name,
				tv_recipient_phone_number, tv_address;
		public RelativeLayout comment_btn;
	}

}
