package com.cn.xtouch.fodelforuser.activity;

import java.lang.reflect.Type;

import com.cn.xtouch.fodelforuser.R;
import com.cn.xtouch.fodelforuser.constant.Constants;
import com.cn.xtouch.fodelforuser.manager.MyAcitivityManager;
import com.cn.xtouch.fodelforuser.utils.NetUtils;
import com.cn.xtouch.fodelforuser.utils.UIUtils;
import com.cn.xtouch.fodelforuser.widget.BottomPopupWindow;
import com.cn.xtouch.fodelforuser.widget.ProcessProgressDialog;
import com.lidroid.xutils.HttpUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

public abstract class BaseActivity extends AppCompatActivity {

	protected HttpUtils httpUtils;
	protected SharedPreferences sharedPref;
	protected Type beanType = null;
	protected boolean isLogin;
	protected DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyAcitivityManager.addActivity(this);
		httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(1000);
		// 加载图片设置
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.touxiang) // 没有图片资源时的默认图片
				.showImageOnFail(R.drawable.touxiang) // 加载失败时的图片
				.cacheInMemory(true) // 启用内存缓存
				.cacheOnDisk(true) // 启用外存缓存
				.considerExifParams(true) // 启用EXIF和JPEG图像格式
				.build();
		// 15s 请求超时设置
		httpUtils.configTimeout(1000 * 15);
		sharedPref = getSharedPreferences(Constants.CONFIG_SHAREDPREF,
				Context.MODE_PRIVATE);
		isLogin = sharedPref.getBoolean(Constants.ISLOGIN, false);
		setContentView(getResourcesId());
		init();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (NetUtils.getNetType(this) == NetUtils.UNCONNECTED) {
			UIUtils.showToast(UIUtils.getString(R.string.no_network));
		}
	}

	/**
	 * 这个方法传递要加载的布局文件
	 * 
	 * @return
	 */
	protected abstract int getResourcesId();

	/**
	 * 第一步要进行的初始化操作
	 */
	protected abstract void init();

	/**
	 * 初始化数据
	 */
	protected abstract void initData();

	/**
	 * 退出登录
	 */
	public void logOff() {
		UIUtils.showToast(UIUtils.getString(R.string.exit_tip));
		sharedPref.edit().putBoolean(Constants.ISLOGIN, false).commit();
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		MyAcitivityManager.finishAll();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MyAcitivityManager.removeActivity(this);
	}

	protected void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha;// 0.0-1.0
		getWindow().setAttributes(lp);
	}

	class PopupDismissListener implements BottomPopupWindow.OnDismissListener {
		@Override
		public void onDismiss() {
			backgroundAlpha(1f);
		}
	}

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
