package com.cn.xtouch.fodelforuser.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.cn.xtouch.fodelforuser.R;
import com.cn.xtouch.fodelforuser.fragment.SignUpFirstFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class SignUpActivity extends BaseActivity {

	@ViewInject(R.id.top_tv_title)
	private TextView top_tv_title;
	private FragmentTransaction fragmentTransaction;
	private FragmentManager fragmentManager;
	private int fl_content = R.id.fl_content;

	@Override
	protected int getResourcesId() {
		return R.layout.activity_sign_up;
	}

	@Override
	protected void init() {
		ViewUtils.inject(this);
		top_tv_title.setText("Sign Up");
	}

	@Override
	protected void initData() {
		fragmentManager = getSupportFragmentManager();
		SignUpFirstFragment signUpFirstFragment = new SignUpFirstFragment();
		addFragment(signUpFirstFragment);
	}

	@OnClick({ R.id.top_back_btn })
	public void onclick(View view) {
		switch (view.getId()) {
		case R.id.top_back_btn:
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 添加fragment
	 * 
	 * @param fragment
	 *            fragment
	 * @param tag
	 *            fragment对应的标志
	 */
	private void addFragment(Fragment fragment) {
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add(fl_content, fragment);
		fragmentTransaction.commit();
	}

}
