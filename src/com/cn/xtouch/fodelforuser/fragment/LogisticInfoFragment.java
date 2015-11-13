package com.cn.xtouch.fodelforuser.fragment;

import java.util.List;

import com.cn.xtouch.fodelforuser.R;
import com.cn.xtouch.fodelforuser.adapter.OrderStatusAdapter;
import com.cn.xtouch.fodelforuser.constant.Constants;
import com.cn.xtouch.fodelforuser.http.Response.OrderRouteInfo.RouteInfo;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class LogisticInfoFragment extends BaseFragment {

	@ViewInject(R.id.tv_order_no)
	private TextView tv_order_no;
	@ViewInject(R.id.listview)
	private ListView listView;
	private OrderStatusAdapter orderStatusAdapter;

	@Override
	public View initUI() {
		View view = View
				.inflate(context, R.layout.fragment_logistic_info, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void initData() {
		Bundle data = getArguments();// 获得从activity中传递过来的值
		List<RouteInfo> list = data.getParcelableArrayList("info_list");
		tv_order_no.setText(data.getString(Constants.ORDER_NO));
		orderStatusAdapter = new OrderStatusAdapter(getActivity(), list,true);
		listView.setAdapter(orderStatusAdapter);
	}
}
