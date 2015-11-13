package com.cn.xtouch.fodelforuser.activity;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cn.xtouch.fodelforuser.R;
import com.cn.xtouch.fodelforuser.adapter.DrawerItemAdapter;
import com.cn.xtouch.fodelforuser.adapter.ViewPagerAdapter;
import com.cn.xtouch.fodelforuser.constant.Constants;
import com.cn.xtouch.fodelforuser.constant.GlobalParameters;
import com.cn.xtouch.fodelforuser.fragment.FinishFragment;
import com.cn.xtouch.fodelforuser.fragment.UnFinishFragment;
import com.cn.xtouch.fodelforuser.http.Response.MyOrder;
import com.cn.xtouch.fodelforuser.http.Response.Order;
import com.cn.xtouch.fodelforuser.http.Response.ResponseBean;
import com.cn.xtouch.fodelforuser.model.CurrentOrder;
import com.cn.xtouch.fodelforuser.model.DrawerItem;
import com.cn.xtouch.fodelforuser.model.DrawerMenu;
import com.cn.xtouch.fodelforuser.utils.EncryptUtils;
import com.cn.xtouch.fodelforuser.utils.JsonHelper;
import com.cn.xtouch.fodelforuser.utils.NetUtils;
import com.cn.xtouch.fodelforuser.utils.UIUtils;
import com.cn.xtouch.fodelforuser.widget.BottomPopupWindow;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

@SuppressLint("InflateParams")
public class HomeActivity extends BaseActivity implements OnPageChangeListener {

	@ViewInject(R.id.drawer_layout)
	private DrawerLayout drawerLayout;

	@ViewInject(R.id.drawer_options)
	private RecyclerView drawerOptions;

	@ViewInject(R.id.tool_bar)
	private Toolbar toolbar;
	private ActionBarDrawerToggle drawerToggle;
	private DrawerItemAdapter drawerItemAdapter;
	private Locale locale;
	@ViewInject(R.id.tack_parcel_btn)
	private TextView tack_parcel_btn;
	@ViewInject(R.id.contact_carrier_btn)
	private TextView contact_carrier_btn;
	@ViewInject(R.id.delivery_address_btn)
	private TextView delivery_address_btn;
	@ViewInject(R.id.delivery_time_btn)
	private TextView delivery_time_btn;
	@ViewInject(R.id.viewpager)
	private ViewPager viewPager;
	private ArrayList<View> views = new ArrayList<>();
	private ViewPagerAdapter adapter;
	@ViewInject(R.id.viewGroup)
	private LinearLayout viewGroup;
	private List<Order> orderList;
	private Order currentOrder = null;
	private int fl_content = R.id.fl_content;
	@ViewInject(R.id.ry_head)
	private RelativeLayout ry_head;

	@Override
	protected int getResourcesId() {
		return R.layout.activity_home;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void init() {
		ViewUtils.inject(this);
		if (locale == null) {
			locale = getResources().getConfiguration().locale;
		}
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);
		drawerLayout.setStatusBarBackground(R.color.white);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
				R.string.drawer_open, R.string.drawer_close);
		drawerLayout.setDrawerListener(drawerToggle);
		List<DrawerItem> drawerItems = Arrays.asList(
				new DrawerItem(DrawerItem.Type.HEADER),
				new DrawerMenu().setIconRes(R.drawable.menu_adbout_me_selector)
						.setText(
								UIUtils.getString(R.string.about_me)
										.toUpperCase(locale)),
				new DrawerMenu().setIconRes(R.drawable.menu_history_selector)
						.setText(
								UIUtils.getString(R.string.history)
										.toUpperCase(locale)),
				new DrawerMenu().setIconRes(R.drawable.menu_help_selector)
						.setText(
								UIUtils.getString(R.string.help).toUpperCase(
										locale)));
		drawerOptions.setLayoutManager(new LinearLayoutManager(this));
		drawerItemAdapter = new DrawerItemAdapter(drawerItems, sharedPref);
		drawerItemAdapter
				.setOnItemClickListener(new DrawerItemAdapter.OnItemClickListener() {
					@Override
					public void onClick(View view, int position) {
						onDrawerMenuSelected(position);
					}
				});
		drawerOptions.setAdapter(drawerItemAdapter);
		drawerOptions.setHasFixedSize(true);

		adapter = new ViewPagerAdapter(views);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(this);
	}

	private void onDrawerMenuSelected(int position) {
		switch (position) {
		case 1:
			gotoAboutMe();
			break;
		case 2:
			gotoHistory();
			break;
		case 3:
			gotoHelp();
			break;
		}
		drawerLayout.closeDrawers();
	}

	private void gotoHistory() {
		Intent intent = new Intent(this, HistoryActivity.class);
		startActivity(intent);
	}

	private void gotoHelp() {
		Intent intent = new Intent(this, HelpActivity.class);
		startActivity(intent);
	}

	private void gotoAboutMe() {
		Intent intent = new Intent(this, AboutMeActivity.class);
		startActivity(intent);
	}

	/**
	 * 刷新视图
	 */
	public void refreshView() {
		orderList.remove(currentOrderIndex);
		views.remove(currentOrderIndex);
		adapter.notifyDataSetChanged();
		currentOrderIndex = currentOrderIndex++;
		if (orderList.size() > currentOrderIndex) {
			currentOrder = orderList.get(currentOrderIndex);
		}
		refreshFragment();
		refreshImageView();
	}

	@Override
	protected void initData() {
		showProgressDialog(this, UIUtils.getString(R.string.loading));
		String url = NetUtils.changeUrl(GlobalParameters.GET_MY_ORDERS, null,
				sharedPref, isLogin);
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
				beanType = new TypeToken<ResponseBean<MyOrder>>() {
				}.getType();
				ResponseBean<?> rb = (ResponseBean<?>) JsonHelper.getInstance()
						.jsonToBean(arg0.result, beanType);
				if (rb != null && rb.code.equals(GlobalParameters.STATUS_1001)) {
					MyOrder myOrder = (MyOrder) rb.data;
					orderList = new ArrayList<>();
					orderList.addAll(myOrder.list);
					ry_head.setVisibility(View.VISIBLE);
					addViews();
				} else {
					UIUtils.showToast("No data");
				}
			}
		});
	}

	private ImageView[] imageViews;
	private View view = null;
	private View currentView = null;
	private int currentOrderIndex = 0;
	private FragmentTransaction fragmentTransaction;
	private FragmentManager fragmentManager;

	/**
	 * 组装Views
	 */
	private void addViews() {
		for (Order order : orderList) {
			view = createView(order);
			views.add(view);
		}
		adapter.notifyDataSetChanged();
		currentOrder = orderList.get(currentOrderIndex);
		currentView = views.get(currentOrderIndex);
		refreshFragment();
		refreshImageView();
	}

	/**
	 * 刷新页码图标
	 */
	private void refreshImageView() {
		viewGroup.removeAllViews();
		// 如果只有一条数据，就不用显示列表黑点了。
		if (views.size() == 1) {
			return;
		}
		imageViews = new ImageView[views.size()];

		for (int i = 0; i < views.size(); i++) {
			LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			// 设置每个小圆点距离左边的间距
			margin.setMargins(10, 0, 0, 0);
			ImageView imageView = new ImageView(this);
			// 设置每个小圆点的宽高
			imageView.setLayoutParams(new LayoutParams(15, 15));
			imageViews[i] = imageView;
			if (i == currentOrderIndex) {
				// 默认选中第一张图片
				imageViews[i]
						.setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				// 其他图片都设置未选中状态
				imageViews[i]
						.setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
			viewGroup.addView(imageViews[i], margin);
		}
	}

	/**
	 * 页面数据初始化
	 * 
	 * @param order
	 */
	private View createView(Order order) {
		view = getLayoutInflater().inflate(R.layout.home_viewpager_item, null);
		// 这里判断当前视图是否为空区分是初始进来还是修改进来的。
		if (currentView != null) {
			view = currentView;
		}
		((TextView) view.findViewById(R.id.tv_order_no))
				.setText(order.order_no);
		((TextView) view.findViewById(R.id.tv_expected_time))
				.setText(order.expected_time);
		((TextView) view.findViewById(R.id.tv_carrier_name))
				.setText(order.recipient_name);
		((TextView) view.findViewById(R.id.tv_carrier_phone_number))
				.setText(order.recipient_phone_number);
		((TextView) view.findViewById(R.id.tv_address)).setText(order.address);
		return view;
	}

	private Bundle bundle = null;
	private Fragment fragment = null;

	/**
	 * 更新首页下面主题部分
	 */
	private void refreshFragment() {
		fragmentManager = getSupportFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		if (fragment != null) {
			fragmentTransaction.remove(fragment);
		}
		if (currentOrder == null) {
			return;
		}
		bundle = new Bundle();
		// 未完成订单
		if (currentOrder.status.equals("0")) {
			fragment = new UnFinishFragment();
		}
		// 完成订单
		else if ((currentOrder.status.equals("1"))) {
			fragment = new FinishFragment();
			bundle.putParcelable("currentOrder", currentOrder);
			fragment.setArguments(bundle);
		}
		fragmentTransaction.add(fl_content, fragment);
		fragmentTransaction.commit();
	}

	// @OnClick({ R.id.top_img_right })
	// public void OnClick(View view) {
	// switch (view.getId()) {
	// case R.id.top_img_right:
	// break;
	// }
	// }

	/**
	 * 替换fragment
	 * 
	 * @param fragment
	 *            fragment
	 * @param tag
	 *            fragment对应的标志名
	 */
	// private void replaceFragment(Fragment fragment, String tag) {
	// fragmentTransaction = fragmentManager.beginTransaction();
	// fragmentTransaction.remove(fragment);
	// fragmentTransaction.add(fl_content, fragment, tag);
	// fragmentTransaction.commit();
	// }

	@Override
	protected void onResume() {
		drawerItemAdapter.loadInfo();
		super.onResume();
	}

	public void tackParcel() {
		Intent intent = new Intent(this, LogisticInfoActivity.class);
		intent.putExtra(Constants.ORDER_NO, currentOrder.order_no);
		startActivity(intent);
	}

	public static final int CHANGE_ADDRESS = 100; // 改收件地址
	private CurrentOrder order = null;

	/**
	 * 修改配送地址
	 */
	public void changetDeliveryAddress() {
		Intent intent = new Intent(this, ChangeAddressActivity.class);
		// intent.putExtra("order_no", currentOrder.order_no);
		// intent.putExtra("address", currentOrder.address);
		// intent.putExtra("phone", currentOrder.recipient_phone_number);
		// intent.putExtra("recipient", currentOrder.recipient_name);
		bundle = new Bundle();
		order = new CurrentOrder(currentOrder.order_no,
				currentOrder.recipient_phone_number, currentOrder.address,
				currentOrder.recipient_name);
		bundle.putSerializable("order", order);
		intent.putExtras(bundle);
		startActivityForResult(intent, CHANGE_ADDRESS);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		switch (requestCode) {
		case CHANGE_ADDRESS:
			if (resultCode == RESULT_OK) {
				String address = intent.getExtras().getString("address");
				String phone = intent.getExtras().getString("phone");
				String recipient = intent.getExtras().getString("recipient");
				currentOrder.address = address;
				currentOrder.recipient_phone_number = phone;
				currentOrder.recipient_name = recipient;
				createView(currentOrder);
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, intent);
	}

	private BottomPopupWindow bottomPopupWindow;

	public void contactCarrier() {
		backgroundAlpha(0.5f);
		bottomPopupWindow = new BottomPopupWindow(this,
				itemsOnClick_contact_carrier,
				R.layout.contact_carrier_popupwindow, currentOrder, 0);
		bottomPopupWindow.setOnDismissListener(new PopupDismissListener());
		bottomPopupWindow.showAtLocation(this.findViewById(R.id.main),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
	}

	public void openChangeDeliveryTime() {
		backgroundAlpha(0.5f);
		bottomPopupWindow = new BottomPopupWindow(this,
				itemsOnClick_change_time,
				R.layout.change_delivery_time_popupwindow, currentOrder, 0);
		bottomPopupWindow.setOnDismissListener(new PopupDismissListener());
		bottomPopupWindow.showAtLocation(this.findViewById(R.id.main),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
	}

	// protected Animation getAnimation() {
	// Animation anim = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
	// Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
	// 0.5f);// 从0.5倍放大到1倍
	// anim.setDuration(1500);
	// return anim;
	// }

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick_change_time = new OnClickListener() {
		public void onClick(View v) {
			bottomPopupWindow.dismiss();
			changeDeliveryTime();
		}
	};

	/**
	 * 更改派送时间
	 */
	public void changeDeliveryTime() {
		String encodeResult = "uid=" + sharedPref.getString(Constants.UID, "")
				+ "&token=" + sharedPref.getString(Constants.TOKEN, "")
				+ "&order_no=" + currentOrder.order_no + "&date="
				+ BottomPopupWindow.date + "&hours=" + BottomPopupWindow.hours;
		String param = "";
		try {
			encodeResult = EncryptUtils.encode(encodeResult);
			param = URLEncoder.encode(encodeResult, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String URL = GlobalParameters.MODIFY_DELIVERY_TIME + param;
		showProgressDialog(this, " updateing");
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
					// initData();
					currentOrder.expected_time = BottomPopupWindow.date + " "
							+ BottomPopupWindow.hours;
					createView(currentOrder);
				} else {
					UIUtils.showToast("Failed to connect server");
				}
			}
		});
	}

	private OnClickListener itemsOnClick_contact_carrier = new OnClickListener() {
		public void onClick(View v) {
			bottomPopupWindow.dismiss();
			switch (v.getId()) {
			case R.id.call_btn:
				Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ currentOrder.carrier_phone_number));
				if (i.resolveActivity(getPackageManager()) == null) {
					return;
				}
				startActivity(i);
				break;
			case R.id.messge_btn:
				Uri smsToUri = Uri.parse("smsto:"
						+ currentOrder.carrier_phone_number);
				Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
				if (intent.resolveActivity(getPackageManager()) == null) {
					return;
				}
				startActivity(intent);
				break;
			}
		}
	};

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		currentOrderIndex = arg0;
		currentOrder = orderList.get(currentOrderIndex);
		currentView = views.get(currentOrderIndex);
		// 遍历数组让当前选中图片下的小圆点设置颜色
		if (imageViews.length > 0) {
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[currentOrderIndex]
						.setBackgroundResource(R.drawable.page_indicator_focused);
				if (currentOrderIndex != i) {
					imageViews[i]
							.setBackgroundResource(R.drawable.page_indicator_unfocused);
				}
			}
		}
		refreshFragment();
	}
}
