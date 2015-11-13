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
import android.view.View;
import android.widget.EditText;

public class SignUpSecondFragment extends BaseFragment {

	@ViewInject(R.id.et_email)
	private EditText et_email;
	@ViewInject(R.id.et_first_name)
	private EditText et_first_name;
	@ViewInject(R.id.et_last_name)
	private EditText et_last_name;
	@ViewInject(R.id.et_password)
	private EditText et_password;
	private String phone;
	private String verify_code;

	@Override
	public View initUI() {
		View view = View.inflate(context, R.layout.fragment_sign_up_second,
				null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void initData() {
		phone = getArguments().getString("phone");
		verify_code = getArguments().getString("verify_code");
	}

	@OnClick({ R.id.signup_btn })
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.signup_btn:
			signUp();
			break;
		}
	}

	/**
	 * 注册
	 */
	private void signUp() {
		String email = et_email.getText().toString();
		String first_name = et_first_name.getText().toString();
		String last_name = et_last_name.getText().toString();
		String password = et_password.getText().toString();
		if (email.isEmpty() || !UIUtils.isValidEmail(email)) {
			UIUtils.showToast("Please enter a valid email");
			et_email.requestFocus();
			return;
		}
		if (first_name.isEmpty()) {
			UIUtils.showToast("First name is null");
			et_first_name.requestFocus();
			return;
		}
		if (last_name.isEmpty()) {
			UIUtils.showToast("Last name is null");
			et_last_name.requestFocus();
			return;
		}
		if (password.length() < 6) {
			UIUtils.showToast("Password minimum 6");
			et_password.requestFocus();
			return;
		}

		String encodeResult = "phone=" + phone + "&email=" + email
				+ "&password=" + password + "&first_name=" + first_name
				+ "&last_name=" + last_name + "&verify_code=" + verify_code;
		String param = "";
		try {
			encodeResult = EncryptUtils.encode(encodeResult);
			param = URLEncoder.encode(encodeResult, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String URL = GlobalParameters.SIGN_UP + param;
		showProgressDialog(getActivity(), "Submitting");
		httpUtils.send(HttpMethod.GET, URL, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				dismissProgressDialog();
				UIUtils.showToast("Failed to sign up");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				dismissProgressDialog();
				beanType = new TypeToken<ResponseBean<Object>>() {
				}.getType();
				ResponseBean<?> rb = (ResponseBean<?>) JsonHelper.getInstance()
						.jsonToBean(arg0.result, beanType);
				if (rb != null) {
					if (rb.code.equals(GlobalParameters.STATUS_1001)) {

						UIUtils.showToast("Sign up succeeded");
						getActivity().finish();
					} else {
						UIUtils.showToast(rb.msg);
					}
				} else {
					UIUtils.showToast("Failed to sign up");
				}
			}
		});
	}


}
