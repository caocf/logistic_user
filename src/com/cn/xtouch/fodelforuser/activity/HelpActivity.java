package com.cn.xtouch.fodelforuser.activity;

import android.annotation.SuppressLint;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.cn.xtouch.fodelforuser.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class HelpActivity extends BaseActivity {

	@ViewInject(R.id.wv_main)
	private WebView webView;
	@ViewInject(R.id.top_tv_title)
	private TextView tv_title;
    @ViewInject(R.id.ry_loading)
    private RelativeLayout ry_loading;
	private String webUrl = "http://www.mshede.com/help/customer/category.html";
	@Override
	protected int getResourcesId() {
		return R.layout.activity_help;
	}

	@Override
	protected void init() {
		ViewUtils.inject(this);
		tv_title.setText("Help");
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void initData() {
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(webUrl);
		// webView.setWebChromeClient(new WebChromeClient());
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				ry_loading.setVisibility(View.VISIBLE);
				view.loadUrl(url);// 使用当前WebView处理跳转
				return true;// true表示此事件在此处被处理，不需要再广播
			}

			@Override
			// 转向错误时的处理
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// TODO Auto-generated method stub
				Toast.makeText(HelpActivity.this, "Oh no! " + description,
						Toast.LENGTH_SHORT).show();
			}
		});
		
		 //显示进度
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress >= 100){
                    ry_loading.setVisibility(View.GONE);
                }
            }
        });
	}

	@OnClick({ R.id.top_back_btn })
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.top_back_btn:
			finish();
			break;
		}
	}

	@Override
	// 默认点回退键，会退出Activity，需监听按键操作，使回退在WebView内发生
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
