package com.cn.xtouch.fodelforuser.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.cn.xtouch.fodelforuser.R;
import com.cn.xtouch.fodelforuser.constant.Constants;
import com.cn.xtouch.fodelforuser.constant.GlobalParameters;
import com.cn.xtouch.fodelforuser.http.Response.Portrait;
import com.cn.xtouch.fodelforuser.http.Response.ResponseBean;
import com.cn.xtouch.fodelforuser.manager.MyAcitivityManager;
import com.cn.xtouch.fodelforuser.utils.JsonHelper;
import com.cn.xtouch.fodelforuser.utils.NetUtils;
import com.cn.xtouch.fodelforuser.utils.UIUtils;
import com.cn.xtouch.fodelforuser.widget.BottomPopupWindow;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class AboutMeActivity extends BaseActivity {

	@ViewInject(R.id.top_tv_title)
	private TextView top_tv_title;
	@ViewInject(R.id.img_head)
	private ImageView img_head;
	@ViewInject(R.id.tv_name)
	private TextView tv_name;
	@ViewInject(R.id.tv_phone_num)
	private TextView tv_phone_num;
	private Bitmap head;// 头像Bitmap
	private static String path = UIUtils.getContext().getFilesDir()
			+ "/myHead/";// 头像存放路径

	@Override
	protected int getResourcesId() {
		return R.layout.activity_about_me;
	}

	@Override
	protected void init() {
		ViewUtils.inject(this);
		top_tv_title.setText("About Me");
		File file = UIUtils.getDiskCacheDir(AboutMeActivity.this, "head");
		if (!file.exists()) {
			file.mkdir();
		}
		String photoName = "head.jpg";
		path = file.getAbsoluteFile() + File.separator + photoName;
	}

	@Override
	protected void initData() {
		loadImage(sharedPref.getString(Constants.PORTRAIT, ""));
		tv_name.setText(sharedPref.getString(Constants.USERNAME, ""));
		tv_phone_num.setText(sharedPref.getString(Constants.PHONE, ""));
	}

	@OnClick({ R.id.top_back_btn, R.id.change_portrait_btn,
			R.id.change_name_btn, R.id.btn_logoff })
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.top_back_btn:
			finish();
			break;
		case R.id.change_portrait_btn:
			changPortrait();
			break;
		case R.id.change_name_btn:
			Intent intent1 = new Intent(AboutMeActivity.this,
					EditInfoActivity.class);
			intent1.putExtra(Constants.CHANGE_INFO, tv_name.getText()
					.toString());
			intent1.putExtra(Constants.CHANGE_TYPE, Constants.CHANGE_NAME);
			startActivityForResult(intent1, CHANGE_NAME_REQUEST);
			break;
		// case R.id.change_phone_btn:
		// Intent intent2 = new Intent(AboutMeActivity.this,
		// EditInfoActivity.class);
		// intent2.putExtra(Constants.CHANGE_INFO, tv_phone_num.getText()
		// .toString());
		// intent2.putExtra(Constants.CHANGE_TYPE, Constants.CHANGE_PHONE);
		// startActivityForResult(intent2, CHANGE_PHONE_REQUEST);
		// break;
		case R.id.btn_logoff:
			exit();
			break;
		}
	}

	private BottomPopupWindow bottomPopupWindow;

	/**
	 * 修改头像
	 */
	private void changPortrait() {
		backgroundAlpha(0.5f);
		bottomPopupWindow = new BottomPopupWindow(this,
				itemsOnClickChangePortrait, R.layout.select_pic_popupwindow,
				null, 25);
		bottomPopupWindow.setOnDismissListener(new PopupDismissListener());
		// 显示窗口
		bottomPopupWindow.showAtLocation(this.findViewById(R.id.main),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
	}

	/**
	 * 退出
	 */
	private void exit() {
		// 实例化SelectPicPopupWindow
		backgroundAlpha(0.5f);
		bottomPopupWindow = new BottomPopupWindow(this, exitItemsOnClick,
				R.layout.select_exit_popupwindow, null, 25);
		bottomPopupWindow.setOnDismissListener(new PopupDismissListener());
		// 显示窗口
		bottomPopupWindow.showAtLocation(this.findViewById(R.id.main),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
	}

	private OnClickListener exitItemsOnClick = new OnClickListener() {
		public void onClick(View v) {
			bottomPopupWindow.dismiss();
			switch (v.getId()) {
			case R.id.btn_first:
				exitAccount();
				break;
			case R.id.btn_second:
				MyAcitivityManager.exitApp();
				break;
			}
		}
	};

	/**
	 * 退出账号
	 */
	private void exitAccount() {
		sharedPref.edit().putBoolean(Constants.ISLOGIN, false).commit();
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		MyAcitivityManager.finishAll();
	}

	private OnClickListener itemsOnClickChangePortrait = new OnClickListener() {
		public void onClick(View v) {
			bottomPopupWindow.dismiss();
			backgroundAlpha(1f);
			switch (v.getId()) {
			case R.id.btn_first:
				Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				File bmp = new File(path);
				intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(bmp));
				startActivityForResult(intent1, 1);
				break;
			case R.id.btn_second:
				Intent intent2 = new Intent(Intent.ACTION_PICK, null);
				intent2.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent2, 2);
				break;
			}
		}
	};

	public static final int CHANGE_NAME_REQUEST = 100; // 改名字
	public static final int CHANGE_PHONE_REQUEST = 200; // 改电话

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				cropPhoto(Uri.fromFile(new File(path)));// 裁剪图片
			}
			break;
		case 2:
			if (resultCode == RESULT_OK) {
				cropPhoto(data.getData());// 裁剪图片
			}
			break;
		case 3:
			if (data != null) {
				Bundle extras = data.getExtras();
				head = extras.getParcelable("data");
				if (head != null) {
					savePortrait(head);
				}
			}
			break;
		case CHANGE_NAME_REQUEST:
			onChangeNameRequest(resultCode, data);
			break;
		case CHANGE_PHONE_REQUEST:
			onChangePhoneRequest(resultCode, data);
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	};

	private void onChangeNameRequest(int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			String name = data.getStringExtra(Constants.CHANGE_INFO);
			tv_name.setText(name);
			sharedPref.edit().putString(Constants.USERNAME, name).commit();
		}
	}

	private void onChangePhoneRequest(int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			String phone = data.getStringExtra(Constants.CHANGE_INFO);
			tv_phone_num.setText(phone);
		}
	}

	/**
	 * 调用系统的裁剪
	 * 
	 * @param uri
	 */
	public void cropPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 80);
		intent.putExtra("outputY", 80);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}

	/***
	 * 保存头像
	 * 
	 * @param mBitmap
	 */
	private void savePortrait(Bitmap mBitmap) {
		File file = new File(path);
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(file));
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭流
				bos.flush();
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		RequestParams params = new RequestParams();
		params.addBodyParameter("picture", file);
		params.addBodyParameter("uid", sharedPref.getString(Constants.UID, ""));
		params.addBodyParameter("token",
				sharedPref.getString(Constants.TOKEN, ""));
		String url = NetUtils.changeUrl(GlobalParameters.CHANGE_PORTRAIT, null,
				sharedPref, false);
		showProgressDialog(this, " Save");
		httpUtils.send(HttpMethod.POST, url, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						if (arg0.getExceptionCode() == GlobalParameters.STATUS_CODE_401) {
							logOff();
						} else {
							UIUtils.showToast("Failed to save portrait");
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						dismissProgressDialog();
						beanType = new TypeToken<ResponseBean<Portrait>>() {
						}.getType();
						ResponseBean<?> rb = (ResponseBean<?>) JsonHelper
								.getInstance()
								.jsonToBean(arg0.result, beanType);
						if (rb != null) {
							Portrait portrait = (Portrait) rb.data;
							if (rb.code.equals(GlobalParameters.STATUS_1001)) {
								sharedPref
										.edit()
										.putString(Constants.PORTRAIT,
												portrait.url).commit();
								head = UIUtils.toRoundCorner(head, 180);
								img_head.setImageBitmap(head);// 用ImageView显示出来
							} else {
								UIUtils.showToast("Failed to save portrait");
							}
						} else {
							UIUtils.showToast("Failed to save portrait");
						}
					}
				});
	}

	/**
	 * 加载头像
	 * 
	 * @param uri
	 */
	private void loadImage(String uri) {
		ImageLoader.getInstance().loadImage(uri, options,
				new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						LogUtils.e("start");
					}

					@Override
					public void onLoadingFailed(String arg0, View arg1,
							FailReason arg2) {
						LogUtils.e("failed");
					}

					@Override
					public void onLoadingComplete(String arg0, View arg1,
							Bitmap mBitmap) {
						if (mBitmap != null) {
							mBitmap = UIUtils.toRoundCorner(mBitmap, 180);
							img_head.setImageBitmap(mBitmap);
						}
					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						LogUtils.e("cancel");
					}
				});
	}
}
