package com.cn.xtouch.fodelforuser.fragment;

import com.cn.xtouch.fodelforuser.R;
import com.lidroid.xutils.ViewUtils;

import android.view.View;

public class CommentedFragment extends BaseFragment {

	@Override
	public View initUI() {
		View view = View.inflate(context, R.layout.fragment_comment, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

}
