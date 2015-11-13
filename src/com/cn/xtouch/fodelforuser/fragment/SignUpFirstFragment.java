package com.cn.xtouch.fodelforuser.fragment;

import java.net.URLEncoder;
import com.cn.xtouch.fodelforuser.R;
import com.cn.xtouch.fodelforuser.constant.GlobalParameters;
import com.cn.xtouch.fodelforuser.http.Response.ResponseBean;
import com.cn.xtouch.fodelforuser.utils.EncryptUtils;
import com.cn.xtouch.fodelforuser.utils.JsonHelper;
import com.cn.xtouch.fodelforuser.utils.UIUtils;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SignUpFirstFragment extends BaseFragment {

	@ViewInject(R.id.get_code_btn)
	private TextView get_code_btn;
	@ViewInject(R.id.et_username)
	private EditText et_username;
	@ViewInject(R.id.et_vailcode)
	private EditText et_vailcode;

	@Override
	public View initUI() {
		View view = View
				.inflate(context, R.layout.fragment_sign_up_first, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void initData() {

	}

	@OnClick({ R.id.signup_btn, R.id.get_code_btn })
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.signup_btn:
			signUp();
			break;
		case R.id.get_code_btn:
			getCode();
			break;
		}
	}

	/**
	 * 注册
	 */
	private void signUp() {
		String phone = et_username.getText().toString();
		String code = et_vailcode.getText().toString();
		if (phone.isEmpty()) {
			UIUtils.showToast("Please enter a valid phone number");
			et_username.requestFocus();
			return;
		}
		if (code.isEmpty()) {
			UIUtils.showToast("Please enter a verification code");
			et_vailcode.requestFocus();
			return;
		}
		String URL = GlobalParameters.CHECK_VERIFICATION_CODE + "?phone="
				+ phone + "&code=" + code;

		httpUtils.send(HttpMethod.GET, URL, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				UIUtils.showToast("Failed to check verification code");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				beanType = new TypeToken<ResponseBean<Object>>() {
				}.getType();
				ResponseBean<?> rb = (ResponseBean<?>) JsonHelper.getInstance()
						.jsonToBean(arg0.result, beanType);
				if (rb != null) {
					if (rb.code.equals(GlobalParameters.STATUS_1001)) {
						FragmentManager fm = getActivity()
								.getSupportFragmentManager();
						FragmentTransaction ft = fm.beginTransaction();
						SignUpSecondFragment second = new SignUpSecondFragment();
						Bundle bundle = new Bundle();
						bundle.putString("phone", et_username.getText()
								.toString());
						bundle.putString("verify_code", et_vailcode.getText()
								.toString());
						second.setArguments(bundle);
						ft.replace(R.id.fl_content, second);
						ft.commit();
					} else {
						UIUtils.showToast(rb.msg);
					}
				} else {
					UIUtils.showToast("Failed to check verification code");
				}
			}
		});
	}

	/**
	 * 获取验证码
	 */
	private void getCode() {
		final String phone = et_username.getText().toString();
		if (phone.isEmpty()) {
			UIUtils.showToast("Please enter a valid phone number");
			et_username.requestFocus();
			return;
		}
		get_code_btn.setEnabled(false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String encodeResult = "phone=" + phone;
				String param = "";
				try {
					encodeResult = EncryptUtils.encode(encodeResult);
					param = URLEncoder.encode(encodeResult, "UTF-8");
				} catch (Exception e) {
					e.printStackTrace();
				}
				String URL = GlobalParameters.GET_VERIFICATION_CODE + param;
				httpUtils.send(HttpMethod.GET, URL,
						new RequestCallBack<String>() {

							@Override
							public void onFailure(HttpException arg0,
									String arg1) {
								UIUtils.showToast("Failed to get verification code");
							}

							@Override
							public void onSuccess(ResponseInfo<String> arg0) {
								beanType = new TypeToken<ResponseBean<Object>>() {
								}.getType();
								ResponseBean<?> rb = (ResponseBean<?>) JsonHelper
										.getInstance().jsonToBean(arg0.result,
												beanType);
								UIUtils.showToast(rb.msg);
							}
						});
				for (int i = 60; i > -1; i--) {
					Message msg = new Message();
					msg.what = 0;
					msg.obj = i;
					handler.sendMessage(msg);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				handleCount((int) msg.obj);
				break;
			}
		}

		private void handleCount(int obj) {
			get_code_btn.setText(obj + "s");
			if (obj < 1) {
				get_code_btn.setEnabled(true);
				get_code_btn.setText("Verification Code");
			}
		}
	};

}
