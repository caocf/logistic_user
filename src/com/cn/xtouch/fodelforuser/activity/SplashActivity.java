package com.cn.xtouch.fodelforuser.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.TextView;
import com.cn.xtouch.fodelforuser.R;
import com.cn.xtouch.fodelforuser.constant.Constants;
import com.cn.xtouch.fodelforuser.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class SplashActivity extends BaseActivity {
	private SharedPreferences.Editor et;
	@ViewInject(R.id.tv_splash_version)
	private TextView tv_splash_version;

	@Override
	protected int getResourcesId() {
		return R.layout.activity_splash;
	}

	@Override
	protected void init() {
		ViewUtils.inject(this);
	}

	@Override
	protected void initData() {
		et = sharedPref.edit();
		tv_splash_version.setText("V" + getVersionName());
		updateApp();
		enterHome();
	}

	/**
	 * 进入主界面
	 * 
	 * @param isLogin
	 *            是否登录
	 */
	private void enterHome() {
		if (isLogin) {
			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			finish();
		}
	}

	/**
	 * 自动更新平台
	 */
	private void updateApp() {
		UmengUpdateAgent.setDefault();
		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateOnlyWifi(true);
		UmengUpdateAgent.setDeltaUpdate(true); // 增量更新
		UmengUpdateAgent.setRichNotification(true); // 启用高级样式，4.1以上支持，暂停/继续和取消按钮
		UmengUpdateAgent.setUpdateUIStyle(UpdateStatus.STYLE_NOTIFICATION);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int statusCode,
					final UpdateResponse updateResponse) {
				if (UpdateStatus.Yes == statusCode) {
					// 有更新
					UmengUpdateAgent.showUpdateNotification(
							SplashActivity.this, updateResponse); // 使用通知栏进行提醒
					et.putBoolean(Constants.AppHasNew, true);
					et.commit();
				}

				if (UpdateStatus.No == statusCode) {
					// 没有更新
					et.putBoolean(Constants.AppHasNew, false);
					et.commit();
				}
			}
		});
		// 静默下载
		UmengUpdateAgent.silentUpdate(SplashActivity.this);
	}

	private static String getVersionName() {
		// 拿到包管理器
		PackageManager pManager = UIUtils.getContext().getPackageManager();
		try {
			// 拿到当前包的信息,getPackageName()返回当前的包名
			PackageInfo packageInfo = pManager.getPackageInfo(UIUtils
					.getContext().getPackageName(), 0);
			return packageInfo.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			// can't reach 这里是无法达到的
			return "";
		}
	}

	@OnClick({ R.id.login_btn, R.id.signup_btn })
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.login_btn:
            gotoLoginActivity();
			break;
		case R.id.signup_btn:
			gotoSignUpActivity();
			break;
		}
	}

	private void gotoSignUpActivity() {
		Intent intent=new Intent(this,SignUpActivity.class);
		startActivity(intent);
	}

	private void gotoLoginActivity() {
		Intent intent=new Intent(this,LoginActivity.class);
		startActivity(intent);
		finish();
	}
}
