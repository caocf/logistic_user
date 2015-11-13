package com.cn.xtouch.fodelforuser.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.View;
import android.widget.TextView;
import com.cn.xtouch.fodelforuser.R;
import com.cn.xtouch.fodelforuser.adapter.CommentAdapter;
import com.cn.xtouch.fodelforuser.constant.GlobalParameters;
import com.cn.xtouch.fodelforuser.http.Response.MyOrder;
import com.cn.xtouch.fodelforuser.http.Response.Order;
import com.cn.xtouch.fodelforuser.http.Response.ResponseBean;
import com.cn.xtouch.fodelforuser.utils.JsonHelper;
import com.cn.xtouch.fodelforuser.utils.NetUtils;
import com.cn.xtouch.fodelforuser.utils.UIUtils;
import com.cn.xtouch.fodelforuser.widget.RefreshListView;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class HistoryActivity extends BaseActivity {

	// @ViewInject(R.id.tabs)
	// private PagerSlidingTabStrip tabs;
	// @ViewInject(R.id.pager)
	// private ViewPager pager;
	// private MyPagerAdapter adapter;
	@ViewInject(R.id.top_tv_title)
	private TextView title;
	@ViewInject(R.id.tv_no_data)
	private TextView tv_no_data;
	@ViewInject(R.id.listview)
	private RefreshListView listView;
	private List<Order> orderList = new ArrayList<>();
	private CommentAdapter commentAdapter;
	/**
	 * 查询条目的起始位置
	 */
	private int startIndex;

	// private List<Fragment> fragments = new ArrayList<>();

	@Override
	protected int getResourcesId() {
		return R.layout.activity_history;
	}

	@Override
	protected void init() {
		ViewUtils.inject(this);
		title.setText("History");
		// setTabsValue();
	}

	@Override
	protected void initData() {
		startIndex = 1;
		// fragments.add(new CommentFragment());
		// fragments.add(new CommentedFragment());
		// adapter = new MyPagerAdapter(getSupportFragmentManager());
		// pager.setAdapter(adapter);
		// final int pageMargin = (int) TypedValue.applyDimension(
		// TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
		// .getDisplayMetrics());
		// pager.setPageMargin(pageMargin);
		// tabs.setViewPager(pager);
		// pager.setCurrentItem(0);
		commentAdapter = new CommentAdapter(this, orderList);
		listView.setAdapter(commentAdapter);
		fillData(true, true);
		listView.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
			@Override
			public void onLoadingMore() {
				fillData(false, false);
			}

			@Override
			public void onDownPullRefresh() {
				fillData(false, true);
			}
		});
	}

	private void fillData(boolean isFirst, final boolean isRefresh) {
		if (isRefresh) {
			startIndex = 1;
		} else {
			startIndex++;
		}
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("page", String.valueOf(startIndex));
		parameterMap.put("type", "1");
		String url = NetUtils.changeUrl(GlobalParameters.GET_HISTORY_ORDER,
				parameterMap, sharedPref, isLogin);
		if (isFirst) {
			showProgressDialog(this, UIUtils.getString(R.string.loading));
		}
		httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				dismissProgressDialog();
				if (arg0.getExceptionCode() == GlobalParameters.STATUS_CODE_401) {
					logOff();
				} else {
					UIUtils.showToast("Failed to connect server");
				}
				listView.hideFooterView();
				listView.hideHeaderView();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				dismissProgressDialog();
				listView.hideFooterView();
				listView.hideHeaderView();
				beanType = new TypeToken<ResponseBean<MyOrder>>() {
				}.getType();
				ResponseBean<?> rb = (ResponseBean<?>) JsonHelper.getInstance()
						.jsonToBean(arg0.result, beanType);
				if (rb != null) {
					if (rb.code.equals(GlobalParameters.STATUS_1001)) {
						MyOrder myOrder = (MyOrder) rb.data;
						listView.setVisibility(View.VISIBLE);
						List<Order> list = myOrder.list;
						if (list.size() > 0) {
							if (isRefresh) {
								orderList = new ArrayList<>();
								orderList = list;
								commentAdapter.setList(orderList);
							} else {

								orderList.addAll(list);
								commentAdapter.notifyDataSetChanged();
							}
						}
					}
				} else {
					if (startIndex == 1) {
						tv_no_data.setVisibility(View.VISIBLE);
					} else {
						UIUtils.showToast(UIUtils
								.getString(R.string.no_more_data));
					}
				}
			}
		});
	}

	/**
	 * 对PagerSlidingTabStrip的各项属性进行赋值。
	 * 
	 * private void setTabsValue() { // 设置Tab是自动填充满屏幕的
	 * tabs.setShouldExpand(true); // 设置Tab的分割线是透明的
	 * tabs.setDividerColor(Color.TRANSPARENT);
	 * tabs.setIndicatorColor(Color.parseColor("#009688"));
	 * tabs.setSelectedTextColor(Color.parseColor("#009688")); }
	 * 
	 * public class MyPagerAdapter extends FragmentPagerAdapter {
	 * 
	 * private final String[] TITLES = { "Comment", "Commented" };
	 * 
	 * public MyPagerAdapter(FragmentManager fm) { super(fm);
	 * 
	 * }
	 * 
	 * @Override public CharSequence getPageTitle(int position) { return
	 *           TITLES[position]; }
	 * @Override public int getCount() { return TITLES.length; }
	 * @Override public Fragment getItem(int position) { return
	 *           fragments.get(position); } }
	 */

	@OnClick({ R.id.top_back_btn })
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.top_back_btn:
			finish();
			break;
		}
	}
}
