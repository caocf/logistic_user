package com.cn.xtouch.fodelforuser.activity;

import java.util.HashMap;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.cn.xtouch.fodelforuser.R;
import com.cn.xtouch.fodelforuser.constant.GlobalParameters;
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

public class ForgotPwdActivity extends BaseActivity {
	@ViewInject(R.id.top_tv_title)
	private TextView title;
	@ViewInject(R.id.et_email_address)
	private EditText et_email_address;
	@ViewInject(R.id.btn_send)
	private Button button;

	@Override
	protected int getResourcesId() {
		return R.layout.activity_forgotpwd;
	}

	@Override
	protected void init() {
		ViewUtils.inject(this);
		title.setText("Forgot Password");
		button.setEnabled(false);
		et_email_address.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (UIUtils.isValidEmail(s.toString())) {
					button.setEnabled(true);
				} else {
					button.setEnabled(false);
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

	}

	@OnClick({ R.id.top_back_btn, R.id.btn_send })
	public void onclick(View view) {
		switch (view.getId()) {
		case R.id.top_back_btn:
			finish();
			break;
		case R.id.btn_send:
			send();
			break;
		}
	}

	/**
	 * 忘记密码邮箱地址发送
	 */
	private void send() {
		String email = et_email_address.getText().toString();
		HashMap<String, String> paramMap = new HashMap<>();
		paramMap.put("email", email);
		String url = NetUtils.changeUrl(GlobalParameters.FIND_PASSWORD,
				paramMap, sharedPref, false);
		showProgressDialog(this, " Sending");
		httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				dismissProgressDialog();
				UIUtils.showToast(UIUtils.getString(R.string.send_failure));
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				dismissProgressDialog();
				beanType = new TypeToken<ResponseBean<Object>>() {
				}.getType();
				ResponseBean<?> rb = (ResponseBean<?>) JsonHelper.getInstance()
						.jsonToBean(arg0.result, beanType);
				if (rb != null) {
					UIUtils.showToast(rb.msg);
				} else {
					UIUtils.showToast(UIUtils.getString(R.string.send_failure));
				}
			}
		});
	}
}
