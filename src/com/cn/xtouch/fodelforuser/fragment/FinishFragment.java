package com.cn.xtouch.fodelforuser.fragment;

import java.util.HashMap;
import com.cn.xtouch.fodelforuser.R;
import com.cn.xtouch.fodelforuser.activity.HomeActivity;
import com.cn.xtouch.fodelforuser.constant.GlobalParameters;
import com.cn.xtouch.fodelforuser.http.Response.Order;
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
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

public class FinishFragment extends BaseFragment {

	@ViewInject(R.id.tv_carrier_name)
	private TextView tv_carrier_name;
	private Order currentOrder;
	@ViewInject(R.id.et_comment)
	private EditText et_comment;
	@ViewInject(R.id.room_ratingbar)
	private RatingBar ratingBar;
	@ViewInject(R.id.tv_satisfaction_txt)
	private TextView tv_satisfaction_txt;
	private int satisfaction_score = 5;// 默认5分

	@Override
	public View initUI() {
		View view = View.inflate(context, R.layout.fragment_finish, null);
		ViewUtils.inject(this, view);
		tv_satisfaction_txt.setText("Score " + satisfaction_score + " -"
				+ "Very satisfied");
		return view;
	}

	@Override
	public void initData() {

		Bundle data = getArguments();// 获得从activity中传递过来的值
		currentOrder = data.getParcelable("currentOrder");
		tv_carrier_name.setText(currentOrder.carrier_name);
		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				String satisfaction = "Unsatisfactory";
				satisfaction_score = (int) rating;
				switch (satisfaction_score) {
				case 0:
					ratingBar.setProgress(20);
					satisfaction_score=1;
					break;
				case 1:
					satisfaction = "Unsatisfactory";//不满意
					break;
				case 2:
					satisfaction = "General";//一般
					break;
				case 3:
					satisfaction = "Well enough";//还可以
					break;
				case 4:
					satisfaction = "Satisfaction";//满意
					break;
				case 5:
					satisfaction = "Very satisfied";//非常满意
					break;
				}
				tv_satisfaction_txt.setText("Score " + satisfaction_score + " -"
						+ satisfaction);
			}
		});
	}

	@OnClick({ R.id.submit_btn })
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.submit_btn:
			postComment();
			break;
		}
	}

	/**
	 * 提交评论
	 */
	private void postComment() {
		String comment_content = et_comment.getText().toString();
		if (comment_content.isEmpty()) {
			UIUtils.showToast("Please enter a comment ~");
			et_comment.requestFocus();
			return;
		}
		if (comment_content.length() < 5) {
			UIUtils.showToast("Comment on at least five words ~");
			et_comment.setSelection(comment_content.length());
			return;
		}
		HashMap<String, String> paramMap = new HashMap<>();
		paramMap.put("order_no", currentOrder.order_no);
		paramMap.put("comment", comment_content);
		paramMap.put("satisfaction", String.valueOf(satisfaction_score));
		String URL = NetUtils.changeUrl(GlobalParameters.POST_COMMENT,
				paramMap, sharedPref, isLogin);
		showProgressDialog(getActivity(), " Submitting");
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
				beanType = new TypeToken<ResponseBean<Object>>() {
				}.getType();
				ResponseBean<?> rb = (ResponseBean<?>) JsonHelper.getInstance()
						.jsonToBean(arg0.result, beanType);
				if (rb != null && rb.code.equals(GlobalParameters.STATUS_1001)) {
					// 刷新数据
					((HomeActivity) getActivity()).refreshView();
				} else {
					UIUtils.showToast("Failed to post comment");
				}
			}
		});
	}
}
