package com.cn.xtouch.fodelforuser.activity;

import java.util.ArrayList;
import java.util.HashMap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;
import com.cn.xtouch.fodelforuser.R;
import com.cn.xtouch.fodelforuser.constant.Constants;
import com.cn.xtouch.fodelforuser.constant.GlobalParameters;
import com.cn.xtouch.fodelforuser.fragment.LogisticInfoFragment;
import com.cn.xtouch.fodelforuser.fragment.LogisticInfoMapFragment;
import com.cn.xtouch.fodelforuser.http.Response.OrderRouteInfo;
import com.cn.xtouch.fodelforuser.http.Response.ResponseBean;
import com.cn.xtouch.fodelforuser.utils.JsonHelper;
import com.cn.xtouch.fodelforuser.utils.NetUtils;
import com.cn.xtouch.fodelforuser.utils.UIUtils;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class LogisticInfoActivity extends BaseActivity {
	@ViewInject(R.id.top_tv_title)
	private TextView top_tv_title;
	private FragmentTransaction fragmentTransaction;
	private FragmentManager fragmentManager;
	private int fl_content = R.id.fl_content;
	@Override
	protected int getResourcesId() {
		return R.layout.activity_logistic_info;
	}

	@Override
	protected void init() {
		ViewUtils.inject(this);
		top_tv_title.setText("Logistics Information");
	}

	@Override
	protected void initData() {
		final String orderNo = getIntent().getExtras().getString(Constants.ORDER_NO);
		HashMap<String, String> paramMap = new HashMap<>();
		paramMap.put("order_no", orderNo);
		String url = NetUtils.changeUrl(GlobalParameters.GET_ROUTE_INFO,
				paramMap, sharedPref, isLogin);
		showProgressDialog(this, UIUtils.getString(R.string.loading));
		httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				dismissProgressDialog();
				if (arg0.getExceptionCode() == GlobalParameters.STATUS_CODE_401) {
					logOff();
				} else {
					UIUtils.showToast("Failed to connect server");
				}
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				dismissProgressDialog();
				beanType = new TypeToken<ResponseBean<OrderRouteInfo>>() {
				}.getType();
				ResponseBean<?> rb = (ResponseBean<?>) JsonHelper.getInstance()
						.jsonToBean(arg0.result, beanType);
				if (rb != null) {
					if (rb.code.equals(GlobalParameters.STATUS_1001)) {
						OrderRouteInfo orderRouteInfo = (OrderRouteInfo) rb.data;
						Bundle data = new Bundle();
						data.putString(Constants.ORDER_NO, orderNo);
						data.putParcelableArrayList("info_list",
								(ArrayList<? extends Parcelable>) orderRouteInfo.info_list);
						// 显示地图上的包裹位置信息
						if (orderRouteInfo.is_show_map.equals("1")) {
							LogisticInfoMapFragment logisticInfoMapFragment = new LogisticInfoMapFragment();
							data.putString("lg", orderRouteInfo.lg);
							data.putString("lt", orderRouteInfo.lt);
							logisticInfoMapFragment.setArguments(data);
							addFragment(logisticInfoMapFragment);
						} else {
							LogisticInfoFragment logisticInfoFragment = new LogisticInfoFragment();
							logisticInfoFragment.setArguments(data);
							addFragment(logisticInfoFragment);
						}
					}
				} else {
					UIUtils.showToast("Failed to load data");
				}
			}
		});
	}

	/**
	 * 添加fragment
	 * 
	 * @param fragment
	 *            fragment
	 * @param tag
	 *            fragment对应的标志
	 */
	private void addFragment(Fragment fragment) {
		fragmentManager = getSupportFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add(fl_content, fragment);
		fragmentTransaction.commit();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@OnClick({ R.id.top_back_btn })
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.top_back_btn:
			finish();
			break;
		default:
			break;
		}
	}
}
