package com.cn.xtouch.fodelforuser.activity;

import java.net.URLEncoder;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.cn.xtouch.fodelforuser.R;
import com.cn.xtouch.fodelforuser.constant.Constants;
import com.cn.xtouch.fodelforuser.constant.GlobalParameters;
import com.cn.xtouch.fodelforuser.http.Response.ResponseBean;
import com.cn.xtouch.fodelforuser.http.Response.User;
import com.cn.xtouch.fodelforuser.utils.EncryptUtils;
import com.cn.xtouch.fodelforuser.utils.InstalltionIdFactory;
import com.cn.xtouch.fodelforuser.utils.JsonHelper;
import com.cn.xtouch.fodelforuser.utils.NetUtils;
import com.cn.xtouch.fodelforuser.utils.UIUtils;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class LoginActivity extends BaseActivity {

	@ViewInject(R.id.top_tv_title)
	private TextView top_tv_title;
	@ViewInject(R.id.et_username)
	private EditText et_username;
	@ViewInject(R.id.et_password)
	private EditText et_password;
	private boolean userisNull = true;
	private boolean pwdisNull = true;
	@ViewInject(R.id.btn_login)
	private Button btn_login;

	@Override
	protected int getResourcesId() {
		return R.layout.activity_login;
	}

	@Override
	protected void init() {
		ViewUtils.inject(this);
		top_tv_title.setText("Log In");
		et_username.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 0) {
					userisNull = false;
					if (!pwdisNull) {
						btn_login.setEnabled(true);
					}
				} else {
					userisNull = true;
					btn_login.setEnabled(false);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		et_password.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 0) {
					pwdisNull = false;
					if (!userisNull) {
						btn_login.setEnabled(true);
					}
				} else {
					pwdisNull = true;
					btn_login.setEnabled(false);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@OnClick({ R.id.top_back_btn, R.id.btn_login, R.id.tv_forgot_password })
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.top_back_btn:
			Intent intent1 = new Intent(LoginActivity.this,
					SplashActivity.class);
			startActivity(intent1);
			finish();
			break;
		case R.id.btn_login:
			login();
			break;
		case R.id.tv_forgot_password:
			Intent intent2 = new Intent(LoginActivity.this,
					ForgotPwdActivity.class);
			startActivity(intent2);
			break;
		}
	}

	/**
	 * 登录
	 */
	private void login() {
		if (NetUtils.getNetType(UIUtils.getContext()) == NetUtils.UNCONNECTED) {
			UIUtils.showToast(UIUtils.getString(R.string.no_network));
			return;
		}
		String username = et_username.getText().toString();
		String password = et_password.getText().toString();
		String encodeResult = "username=" + username + "&password=" + password
				+ "&type=1&imei="
				+ InstalltionIdFactory.id(UIUtils.getContext());
		String param = "";
		try {
			encodeResult = EncryptUtils.encode(encodeResult);
			param = URLEncoder.encode(encodeResult, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String URL = GlobalParameters.LOGIN + param;
		showProgressDialog(this, " Logging");
		httpUtils.send(HttpRequest.HttpMethod.GET, URL,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						dismissProgressDialog();
						beanType = new TypeToken<ResponseBean<User>>() {
						}.getType();
						ResponseBean<?> rb = JsonHelper.getInstance()
								.jsonToBean(responseInfo.result, beanType);
						if (rb != null) {
							if (rb.code.equals(GlobalParameters.STATUS_1001)) {
								LogUtils.i("登录成功");
								User user = (User) rb.data;
								if (user == null) {
									return;
								}
								// 保存登录状态
								sharedPref.edit()
										.putBoolean(Constants.ISLOGIN, true)
										.commit();
								// 保存uid和token
								sharedPref.edit()
										.putString(Constants.UID, user.uid)
										.commit();
								sharedPref.edit()
										.putString(Constants.TOKEN, user.token)
										.commit();
								sharedPref
										.edit()
										.putString(Constants.USERNAME,
												user.name).commit();
								sharedPref
										.edit()
										.putString(Constants.PORTRAIT,
												user.portrait).commit();
								sharedPref
										.edit()
										.putString(Constants.FIRST_NAME,
												user.first_name).commit();
								sharedPref
										.edit()
										.putString(Constants.LAST_NAME,
												user.last_name).commit();
								sharedPref
										.edit()
										.putString(Constants.PHONE,
												user.phone_number).commit();
								afterLogin();
							} else {
								UIUtils.showToast(rb.msg);
							}
						} else {
							UIUtils.showToast("Failed to Login");
						}
					}

					@Override
					public void onFailure(HttpException e, String s) {
						UIUtils.showToast("Failed to Login");
						LogUtils.e("登录失败", e);
						dismissProgressDialog();
					}
				});

	}

	private void afterLogin() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		finish();
	}
}
