package com.cn.xtouch.fodelforuser.activity;

import java.net.URLEncoder;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.cn.xtouch.fodelforuser.R;
import com.cn.xtouch.fodelforuser.constant.Constants;
import com.cn.xtouch.fodelforuser.constant.GlobalParameters;
import com.cn.xtouch.fodelforuser.http.Response.ResponseBean;
import com.cn.xtouch.fodelforuser.model.CurrentOrder;
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

public class ChangeAddressActivity extends BaseActivity {
	@ViewInject(R.id.top_img_right)
	private ImageView top_img_right;
	@ViewInject(R.id.top_tv_title)
	private TextView top_tv_title;
	@ViewInject(R.id.et_name)
	private EditText et_name;
	@ViewInject(R.id.et_phone)
	private EditText et_phone;
	@ViewInject(R.id.et_address)
	private EditText et_address;

	@Override
	protected int getResourcesId() {
		return R.layout.activity_change_address;
	}

	@Override
	protected void init() {
		ViewUtils.inject(this);
		top_tv_title.setText("Change Delivery Address");
		top_img_right.setImageResource(R.drawable.selector_over_btn);
		top_img_right.setVisibility(View.VISIBLE);
	}

	private String order_no = null;

	@Override
	protected void initData() {

		// order_no = getIntent().getExtras().getString(Constants.ORDER_NO);
		// String recipient_name=getIntent().getExtras().getString("recipient");
		// String
		// recipient_phone_number=getIntent().getExtras().getString("phone");
		// String address=getIntent().getExtras().getString("address");
		Bundle bundle = getIntent().getExtras();
		CurrentOrder order = (CurrentOrder) bundle.getSerializable("order");
		order_no = order.orderNo;
		et_name.setText(order.name);
		et_name.setSelection(order.name.length());
		et_phone.setText(order.phoneNum);
		et_address.setText(order.address);
		// HashMap<String, String> parmMap = new HashMap<>();
		// parmMap.put("order_no", order_no);
		// String URL =
		// NetUtils.changeUrl(GlobalParameters.GET_DELIVERY_ADDRESS,
		// parmMap, sharedPref, isLogin);
		// showProgressDialog(this, UIUtils.getString(R.string.loading));
		// httpUtils.send(HttpMethod.GET, URL, new RequestCallBack<String>() {
		// @Override
		// public void onFailure(HttpException arg0, String arg1) {
		// dismissProgressDialog();
		// if (arg0.getExceptionCode() == GlobalParameters.STATUS_CODE_401) {
		// logOff();
		// } else {
		// UIUtils.showToast("Failed to connect server");
		// }
		// }
		//
		// @Override
		// public void onSuccess(ResponseInfo<String> arg0) {
		// dismissProgressDialog();
		// beanType = new TypeToken<ResponseBean<Order>>() {
		// }.getType();
		// ResponseBean<?> rb = (ResponseBean<?>) JsonHelper.getInstance()
		// .jsonToBean(arg0.result, beanType);
		// if (rb != null && rb.code.equals(GlobalParameters.STATUS_1001)) {
		// Order order = (Order) rb.data;
		// et_name.setText(order.recipient_name);
		// et_name.setSelection(order.recipient_name.length());
		// et_phone.setText(order.recipient_phone_number);
		// et_address.setText(order.address);
		// } else {
		// UIUtils.showToast("Failed to connect server");
		// }
		// }
		// });
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
		final String address = et_address.getText().toString();
		final String phone = et_phone.getText().toString();
		final String recipient = et_name.getText().toString();
		if (address.isEmpty()) {
			UIUtils.showToast("Address is null");
			et_address.requestFocus();
			return;
		}
		if (phone.isEmpty()) {
			UIUtils.showToast("Phone is null");
			et_phone.requestFocus();
			return;
		}
		if (recipient.isEmpty()) {
			UIUtils.showToast("Recipient is null");
			et_name.requestFocus();
			return;
		}
		String encodeResult = "uid=" + sharedPref.getString(Constants.UID, "")
				+ "&token=" + sharedPref.getString(Constants.TOKEN, "")
				+ "&order_no=" + order_no + "&address=" + address + "&phone="
				+ phone + "&recipient=" + recipient;
		String param = "";
		try {
			encodeResult = EncryptUtils.encode(encodeResult);
			param = URLEncoder.encode(encodeResult, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String URL = GlobalParameters.MODIFY_DELIVERY_ADDRESS + param;
		showProgressDialog(this, " Updateing");
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
					Intent intent = new Intent();
					intent.putExtra("address", address);
					intent.putExtra("phone", phone);
					intent.putExtra("recipient", recipient);
					setResult(RESULT_OK, intent);
					finish();
				} else {
					UIUtils.showToast("Failed to change address");
				}
			}
		});
	}
}
