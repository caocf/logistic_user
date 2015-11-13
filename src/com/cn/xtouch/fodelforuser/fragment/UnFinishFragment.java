package com.cn.xtouch.fodelforuser.fragment;
import com.cn.xtouch.fodelforuser.R;
import com.cn.xtouch.fodelforuser.activity.HomeActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import android.view.View;
public class UnFinishFragment extends BaseFragment {
	@Override
	public View initUI() {
		View view = View.inflate(context, R.layout.fragment_un_finish, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void initData() {
		//Bundle data = getArguments();// 获得从activity中传递过来的值
		//currentOrder = data.getParcelable("currentOrder");
	}

	@OnClick({ R.id.tack_parcel_btn, R.id.contact_carrier_btn,
			R.id.delivery_time_btn, R.id.delivery_address_btn })
	public void OnClick(View view) {
		switch (view.getId()) {
		case R.id.tack_parcel_btn:
			((HomeActivity)getActivity()).tackParcel();
			break;
		case R.id.contact_carrier_btn:
			((HomeActivity)getActivity()).contactCarrier();
			break;
		case R.id.delivery_time_btn:
			((HomeActivity)getActivity()).openChangeDeliveryTime();
			break;
		case R.id.delivery_address_btn:
			((HomeActivity)getActivity()).changetDeliveryAddress();
			break;
		}
	}
//
//	private void tackParcel() {
//		Intent intent = new Intent(getActivity(), LogisticInfoActivity.class);
//		intent.putExtra(Constants.ORDER_NO, currentOrder.order_no);
//		startActivity(intent);
//	}
//
//	private BottomPopupWindow bottomPopupWindow;
//
//	private void contactCarrier() {
//		backgroundAlpha(0.5f);
//		bottomPopupWindow = new BottomPopupWindow(getActivity(),
//				itemsOnClick_contact_carrier,
//				R.layout.contact_carrier_popupwindow, currentOrder, 0);
//		bottomPopupWindow.setOnDismissListener(new PopupDismissListener());
//		bottomPopupWindow.showAtLocation(getActivity().findViewById(R.id.main),
//				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
//	}
//
//	private void openChangeDeliveryTime() {
//		backgroundAlpha(0.5f);
//		bottomPopupWindow = new BottomPopupWindow(getActivity(),
//				itemsOnClick_change_time,
//				R.layout.change_delivery_time_popupwindow, currentOrder, 0);
//		bottomPopupWindow.setOnDismissListener(new PopupDismissListener());
//		bottomPopupWindow.showAtLocation(getActivity().findViewById(R.id.main),
//				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
//	}
//
//	private OnClickListener itemsOnClick_change_time = new OnClickListener() {
//		public void onClick(View v) {
//			bottomPopupWindow.dismiss();
//			changeDeliveryTime();
//		}
//	};
//
//	/**
//	 * 更改派送时间
//	 */
//	private void changeDeliveryTime() {
//		String encodeResult = "uid=" + sharedPref.getString(Constants.UID, "")
//				+ "&token=" + sharedPref.getString(Constants.TOKEN, "")
//				+ "&order_no=" + currentOrder.order_no + "&date="
//				+ BottomPopupWindow.date + "&hours=" + BottomPopupWindow.hours;
//		String param = "";
//		try {
//			encodeResult = EncryptUtils.encode(encodeResult);
//			param = URLEncoder.encode(encodeResult, "UTF-8");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		String URL = GlobalParameters.MODIFY_DELIVERY_TIME + param;
//		showProgressDialog(getActivity(), " updateing");
//		httpUtils.send(HttpMethod.GET, URL, new RequestCallBack<String>() {
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				dismissProgressDialog();
//				if (arg0.getExceptionCode() == GlobalParameters.STATUS_CODE_401) {
//					logOff();
//				} else {
//					UIUtils.showToast("Failed to connect server");
//				}
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> arg0) {
//				dismissProgressDialog();
//				beanType = new TypeToken<ResponseBean<Object>>() {
//				}.getType();
//				ResponseBean<?> rb = (ResponseBean<?>) JsonHelper.getInstance()
//						.jsonToBean(arg0.result, beanType);
//				if (rb != null && rb.code.equals(GlobalParameters.STATUS_1001)) {
//					initData();
//				} else {
//					UIUtils.showToast("Failed to change delivery time");
//				}
//			}
//		});
//	}
//
//	private OnClickListener itemsOnClick_contact_carrier = new OnClickListener() {
//		public void onClick(View v) {
//			bottomPopupWindow.dismiss();
//			switch (v.getId()) {
//			case R.id.call_btn:
//				Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
//						+ currentOrder.carrier_phone_number));
//				if (i.resolveActivity(getActivity().getPackageManager()) == null) {
//					return;
//				}
//				startActivity(i);
//				break;
//			case R.id.messge_btn:
//				Uri smsToUri = Uri.parse("smsto:"
//						+ currentOrder.carrier_phone_number);
//				Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
//				if (intent.resolveActivity(getActivity().getPackageManager()) == null) {
//					return;
//				}
//				startActivity(intent);
//				break;
//			}
//		}
//	};
	
//	public static final int CHANGE_ADDRESS = 100; // 改收件地址
//
//	private void changetDeliveryAddress() {
//		Intent intent = new Intent(getActivity(), ChangeAddressActivity.class);
//		intent.putExtra(Constants.ORDER_NO, currentOrder.order_no);
//		startActivityForResult(intent, CHANGE_ADDRESS);
//	}
	
	

//	protected void backgroundAlpha(float bgAlpha) {
//		WindowManager.LayoutParams lp = getActivity().getWindow()
//				.getAttributes();
//		lp.alpha = bgAlpha;// 0.0-1.0
//		getActivity().getWindow().setAttributes(lp);
//	}
//
//	class PopupDismissListener implements BottomPopupWindow.OnDismissListener {
//		@Override
//		public void onDismiss() {
//			backgroundAlpha(1f);
//		}
//	}

}
