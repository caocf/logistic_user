package com.cn.xtouch.fodelforuser.activity;

import java.util.HashMap;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cn.xtouch.fodelforuser.R;
import com.cn.xtouch.fodelforuser.adapter.OrderStatusAdapter;
import com.cn.xtouch.fodelforuser.constant.Constants;
import com.cn.xtouch.fodelforuser.constant.GlobalParameters;
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

public class LogisticCommentActivity extends BaseActivity {

	@ViewInject(R.id.top_tv_title)
	private TextView top_tv_title;
	@ViewInject(R.id.ly_content)
	private LinearLayout ly_content;
	@ViewInject(R.id.ry_comment_content)
	private RelativeLayout ry_comment_content;
	@ViewInject(R.id.room_ratingbar)
	private RatingBar ratingBar;
	@ViewInject(R.id.tv_order_no)
	private TextView tv_order_no;
	@ViewInject(R.id.tv_comment_content)
	private TextView tv_comment_content;
	@ViewInject(R.id.listview)
	private ListView listView;
	@ViewInject(R.id.tv_satisfaction_txt)
	private TextView tv_satisfaction_txt;
	@Override
	protected int getResourcesId() {
		return R.layout.activity_logistic_comment;
	}

	@Override
	protected void init() {
		ViewUtils.inject(this);
		top_tv_title.setText("Comment");
		ratingBar.setIsIndicator(true);
	}

	@Override
	protected void initData() {
		final String orderNo = getIntent().getExtras().getString(
				Constants.ORDER_NO);
		HashMap<String, String> paramMap = new HashMap<>();
		paramMap.put("order_no", orderNo);
		String URL = NetUtils.changeUrl(GlobalParameters.GET_COMMENT_INFO,
				paramMap, sharedPref, isLogin);
		showProgressDialog(this, UIUtils.getString(R.string.loading));
		httpUtils.send(HttpMethod.GET, URL, new RequestCallBack<String>() {

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
						if (orderRouteInfo != null) {
							ly_content.setVisibility(View.VISIBLE);
							tv_order_no.setText(orderNo);
							if (!orderRouteInfo.comment_satisfaction.isEmpty()) {
								int satisfaction_score = Integer
										.parseInt(orderRouteInfo.comment_satisfaction);
								String satisfaction = "";
								switch (satisfaction_score) {
								case 1:
									satisfaction = "Unsatisfactory";// 不满意
									break;
								case 2:
									satisfaction = "General";// 一般
									break;
								case 3:
									satisfaction = "Well enough";// 还可以
									break;
								case 4:
									satisfaction = "Satisfaction";// 满意
									break;
								case 5:
									satisfaction = "Very satisfied";// 非常满意
									break;
								}
								// 5分制，1 分占20%
								ratingBar.setProgress(satisfaction_score * 20);
								tv_satisfaction_txt.setText("Score "
										+ satisfaction_score + " -"
										+ satisfaction);
							}
							if (!orderRouteInfo.comment_content.isEmpty()) {
								ry_comment_content.setVisibility(View.VISIBLE);
								tv_comment_content
										.setText(orderRouteInfo.comment_content);
							}
							OrderStatusAdapter orderStatusAdapter = new OrderStatusAdapter(
									LogisticCommentActivity.this,
									orderRouteInfo.info_list, false);
							listView.setAdapter(orderStatusAdapter);
						}
					}
				} else {
					UIUtils.showToast("Failed to load comment");
				}
			}
		});
	}

	@OnClick({ R.id.top_back_btn })
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.top_back_btn:
			finish();
			break;
		}
	}
}
