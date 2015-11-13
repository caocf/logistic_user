package com.cn.xtouch.fodelforuser.activity;

import java.util.HashMap;
import java.util.Map;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.cn.xtouch.fodelforuser.R;
import com.cn.xtouch.fodelforuser.constant.Constants;
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

public class EditInfoActivity extends BaseActivity {
	@ViewInject(R.id.top_tv_title)
	private TextView top_tv_title;
	@ViewInject(R.id.top_img_right)
	private ImageView top_img_right;
	@ViewInject(R.id.et_first_name)
	private EditText et_first_name;
	@ViewInject(R.id.et_last_name)
	private EditText et_last_name;
	private boolean firstNameisNull = false;
	private boolean lastNameisNull = false;

	@Override
	protected int getResourcesId() {
		return R.layout.activity_edit_info;
	}

	@Override
	protected void init() {
		ViewUtils.inject(this);
		top_img_right.setImageResource(R.drawable.selector_over_btn);
		top_img_right.setVisibility(View.VISIBLE);
		et_first_name.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 0) {
					firstNameisNull = false;
					if (!lastNameisNull) {
						top_img_right.setVisibility(View.VISIBLE);
					}
				} else {
					firstNameisNull = true;
					top_img_right.setVisibility(View.GONE);
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

		et_last_name.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 0) {
					lastNameisNull = false;
					if (!firstNameisNull) {
						top_img_right.setVisibility(View.VISIBLE);
					}
				} else {
					lastNameisNull = true;
					top_img_right.setVisibility(View.GONE);
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
		// String info =
		// getIntent().getExtras().getString(Constants.CHANGE_INFO);
//		 String type =
//		 getIntent().getExtras().getString(Constants.CHANGE_TYPE);
		 top_tv_title.setText("Edit Name");
		// editText.setText(info);
		// if (type.equals(Constants.CHANGE_NAME)) {
		// top_tv_title.setText("Edit Name");
		// } else {
		// top_tv_title.setText("Edit Phone");
		// editText.setInputType(InputType.TYPE_CLASS_PHONE);
		// }
		// editText.setSelection(info.length());
	}

	@Override
	protected void initData() {
		et_first_name.setText(sharedPref.getString(Constants.FIRST_NAME, ""));
		et_first_name.setSelection(sharedPref.getString(Constants.FIRST_NAME, "").length());
		et_last_name.setText(sharedPref.getString(Constants.LAST_NAME, ""));
	}

	@OnClick({ R.id.top_back_btn, R.id.top_img_right })
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.top_back_btn:
			finish();
			break;
		case R.id.top_img_right:
			overChange();
			break;
		}
	}

	/**
	 * 完成修改
	 */
	private void overChange() {
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("first_name", et_first_name.getText().toString());
		parameterMap.put("last_name", et_last_name.getText().toString());
		String URL = NetUtils.changeUrl(GlobalParameters.CHANGE_NAME,
				parameterMap, sharedPref, isLogin);
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
					String firstName = et_first_name.getText().toString();
					String lastName = et_last_name.getText().toString();
					sharedPref.edit()
							.putString(Constants.FIRST_NAME, firstName)
							.commit();
					sharedPref.edit().putString(Constants.LAST_NAME, lastName)
							.commit();
					Intent i = new Intent();
					i.putExtra(Constants.CHANGE_INFO, firstName + lastName);
					setResult(RESULT_OK, i);
					finish();
				} else {
					UIUtils.showToast("Failed to change name");
				}
			}
		});
	}
}
