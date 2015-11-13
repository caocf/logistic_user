package com.cn.xtouch.fodelforuser.fragment;
import java.lang.reflect.Type;

import com.cn.xtouch.fodelforuser.R;
import com.cn.xtouch.fodelforuser.activity.LoginActivity;
import com.cn.xtouch.fodelforuser.constant.Constants;
import com.cn.xtouch.fodelforuser.manager.MyAcitivityManager;
import com.cn.xtouch.fodelforuser.utils.UIUtils;
import com.cn.xtouch.fodelforuser.widget.ProcessProgressDialog;
import com.lidroid.xutils.HttpUtils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

	public View view;
	public Context context;
	protected HttpUtils httpUtils;
	protected SharedPreferences sharedPref;
	protected Type beanType=null;
	protected boolean isLogin;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		context = getActivity();
		httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(1000);
		// 15s 请求超时设置
		httpUtils.configTimeout(1000 * 15);
		sharedPref = getActivity().getSharedPreferences(Constants.CONFIG_SHAREDPREF,
				Context.MODE_PRIVATE);
		isLogin = sharedPref.getBoolean(Constants.ISLOGIN, false);
		super.onCreate(savedInstanceState);
	}

	/**
	 * 初始化UI
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = initUI();
		return view;
	}

	/**
	 * 填充数据
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
	}

	public void onPause() {
		super.onPause();
	}

	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 退出登录
	 */
	public void logOff() {
		UIUtils.showToast(UIUtils.getString(R.string.exit_tip));
		sharedPref.edit().putBoolean(Constants.ISLOGIN, false).commit();
		Intent intent = new Intent(getActivity(), LoginActivity.class);
		startActivity(intent);
		MyAcitivityManager.finishAll();
	}

	/**
	 * 初始化界面
	 * 
	 * @return
	 */
	public abstract View initUI();

	/**
	 * 填充数据
	 */
	public abstract void initData();
	
	private Dialog dialog;

	/**
	 * 处理类加载
	 * 
	 * @param context
	 * @param msg
	 */
	protected void showProgressDialog(Context context, String msg) {
		if (dialog != null) {
			dialog.cancel();
		}
		dialog = new ProcessProgressDialog(context, msg);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	protected void dismissProgressDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			dialog = null;
		}
	}
	
}
