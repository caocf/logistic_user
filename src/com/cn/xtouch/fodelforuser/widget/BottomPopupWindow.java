package com.cn.xtouch.fodelforuser.widget;
import com.cn.xtouch.fodelforuser.R;
import com.cn.xtouch.fodelforuser.http.Response.Order;
import com.cn.xtouch.fodelforuser.utils.UIUtils;
import com.lidroid.xutils.util.LogUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class BottomPopupWindow extends PopupWindow {

	private View mMenuView;
	private TextView okBtn;
	private TextView callBtn, messgeBtn, tv_name, tv_phone_num;
	private ImageView headImg;
	private WheelView wheelView1, wheelView2;
	private TextView btn_first, btn_second, btn_cancel;
	public static String date = null;
	public static String hours = null;
	public static boolean isUpdate = false;

	// private static final String[] Date = new String[]{"2015-11-6",
	// "2015-11-7", "2015-11-8", "2015-11-9"};
	// private static final String[] Time = new String[]{"09:00-11:00",
	// "11:00-13:00", "13:00-15:00", "15:00-17:00"};
	public BottomPopupWindow(final Context context,
			OnClickListener onClickListener, int resId, final Order order,
			int padding) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(resId, null);
		switch (resId) {
		case R.layout.change_delivery_time_popupwindow:
			okBtn = (TextView) mMenuView.findViewById(R.id.ok_btn);
			btn_cancel = (TextView) mMenuView.findViewById(R.id.btn_cancel);
			wheelView1 = (WheelView) mMenuView
					.findViewById(R.id.wheel_view_date);
			wheelView2 = (WheelView) mMenuView
					.findViewById(R.id.wheel_view_time);
			String expected_time = order.expected_time;
			String dataString = "";
			String hoursString = "";
			if (!expected_time.isEmpty()) {
				String str[] = expected_time.split(" ");
				dataString = str[0];
				hoursString = str[1];
			}
			int currentDate = 0;
			int currentHours = 0;
			for (int i = 0; i < order.delivery_time_list.dates.size(); i++) {
				if (order.delivery_time_list.dates.get(i).equals(dataString)) {
					currentDate = i;
					break;
				}
			}
			for (int i = 0; i < order.delivery_time_list.hours.size(); i++) {
				if (order.delivery_time_list.hours.get(i).equals(hoursString)) {
					currentHours = i;
					break;
				}
			}
			wheelView1.setOffset(1);
			wheelView1.setItems(order.delivery_time_list.dates);
			wheelView1.setSeletion(currentDate);
			date = order.delivery_time_list.dates.get(currentDate);
			hours = order.delivery_time_list.hours.get(currentHours);
			wheelView1
					.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
						@Override
						public void onSelected(int selectedIndex, String item) {
							date = item;
						}
					});
			wheelView2.setOffset(1);
			wheelView2.setItems(order.delivery_time_list.hours);
			wheelView2.setSeletion(currentHours);
			wheelView2
					.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
						@Override
						public void onSelected(int selectedIndex, String item) {
							hours = item;
						}
					});
			okBtn.setOnClickListener(onClickListener);
			btn_cancel.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// 销毁弹出框
					dismiss();
				}
			});
			break;
		case R.layout.contact_carrier_popupwindow:
			callBtn = (TextView) mMenuView.findViewById(R.id.call_btn);
			messgeBtn = (TextView) mMenuView.findViewById(R.id.messge_btn);
			tv_name = (TextView) mMenuView.findViewById(R.id.tv_name);
			tv_phone_num = (TextView) mMenuView.findViewById(R.id.tv_phone_num);
			headImg = (ImageView) mMenuView.findViewById(R.id.img_head);
			tv_name.setText(order.carrier_name);
			tv_phone_num.setText(order.carrier_phone_number);
			loadImage(order.portrait);
			callBtn.setOnClickListener(onClickListener);
			messgeBtn.setOnClickListener(onClickListener);
			break;
		case R.layout.select_pic_popupwindow:
		case R.layout.select_exit_popupwindow:
			btn_first = (TextView) mMenuView.findViewById(R.id.btn_first);
			btn_second = (TextView) mMenuView.findViewById(R.id.btn_second);
			btn_cancel = (TextView) mMenuView.findViewById(R.id.btn_cancel);
			// // 取消按钮
			btn_cancel.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// 销毁弹出框
					dismiss();
				}
			});
			// 设置按钮监听
			btn_first.setOnClickListener(onClickListener);
			btn_second.setOnClickListener(onClickListener);
			btn_cancel.setOnClickListener(onClickListener);
			break;
		}
		mMenuView.setPadding(padding, 0, padding, 0);
		this.setContentView(mMenuView);
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为透明
		ColorDrawable dw = new ColorDrawable(0x0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			public boolean onTouch(View v, MotionEvent event) {
				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});
	}

	/**
	 * 加载头像
	 * 
	 * @param uri
	 */
	private void loadImage(String uri) {
		// 加载图片设置
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.touxiang) // 没有图片资源时的默认图片
				.showImageOnFail(R.drawable.touxiang) // 加载失败时的图片
				.cacheInMemory(true) // 启用内存缓存
				.cacheOnDisk(true) // 启用外存缓存
				.considerExifParams(true) // 启用EXIF和JPEG图像格式
				.build();
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
							headImg.setImageBitmap(mBitmap);
						}
					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						LogUtils.e("cancel");
					}
				});
	}
}
