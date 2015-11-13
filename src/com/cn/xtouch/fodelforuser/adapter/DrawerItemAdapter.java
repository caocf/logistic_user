package com.cn.xtouch.fodelforuser.adapter;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import com.cn.xtouch.fodelforuser.R;
import com.cn.xtouch.fodelforuser.constant.Constants;
import com.cn.xtouch.fodelforuser.model.DrawerItem;
import com.cn.xtouch.fodelforuser.model.DrawerMenu;
import com.cn.xtouch.fodelforuser.utils.UIUtils;
import com.lidroid.xutils.util.LogUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class DrawerItemAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private final List<DrawerItem> drawerItems;
	private OnItemClickListener listener;
	private SharedPreferences sharedPref;
	private DisplayImageOptions options;
	private ImageView head_img;

	public DrawerItemAdapter(List<DrawerItem> drawerItems,
			SharedPreferences sharedPreferences) {
		this.drawerItems = drawerItems;
		sharedPref = sharedPreferences;
		// 加载图片设置
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.touxiang) // 没有图片资源时的默认图片
				.showImageOnFail(R.drawable.touxiang) // 加载失败时的图片
				.cacheInMemory(true) // 启用内存缓存
				.cacheOnDisk(true) // 启用外存缓存
				.considerExifParams(true) // 启用EXIF和JPEG图像格式
				.build();
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		this.listener = listener;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
			int viewType) {
		switch (DrawerItem.Type.values()[viewType]) {
		case HEADER:
			View headerRootView = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.drawer_header, parent, false);
			return new HeaderViewHolder(headerRootView);
		case DIVIDER:
			View dividerRootView = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.drawer_divider, parent, false);
			return new DividerViewHolder(dividerRootView);
		case MENU:
			View menuRootView = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.drawer_menu, parent, false);
			return new MenuViewHolder(menuRootView);
		default:
			return null;
		}
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder,
			final int position) {
		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (listener != null)
					listener.onClick(view, position);
			}
		});
		DrawerItem drawerItem = drawerItems.get(position);
		switch (drawerItem.getType()) {
		case MENU:
			MenuViewHolder menuViewHolder = (MenuViewHolder) holder;
			DrawerMenu drawerMenu = (DrawerMenu) drawerItem;
			menuViewHolder.itemTextView.setText(drawerMenu.getText());
			menuViewHolder.itemTextView
					.setCompoundDrawablesWithIntrinsicBounds(
							drawerMenu.getIconRes(), 0, 0, 0);
			break;

		}
	}

	@Override
	public int getItemViewType(int position) {
		return drawerItems.get(position).getType().ordinal();
	}

	@Override
	public int getItemCount() {
		return drawerItems.size();
	}

	private TextView userName;

	private class HeaderViewHolder extends RecyclerView.ViewHolder {
		public HeaderViewHolder(View rootView) {
			super(rootView);
			head_img = (ImageView) rootView.findViewById(R.id.profile_image);
			userName = (TextView) rootView.findViewById(R.id.user_name);
			loadInfo();
		}
	}

	/**
	 * 加载头像
	 * 
	 * @param uri
	 */
	public void loadInfo() {
		if (userName != null) {
			userName.setText(sharedPref.getString(Constants.USERNAME, ""));
		}
		if (head_img != null) {
			ImageLoader.getInstance().loadImage(
					sharedPref.getString(Constants.PORTRAIT, ""), options,
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
								head_img.setImageBitmap(mBitmap);
							}
						}

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
							LogUtils.e("cancel");
						}
					});
		}
	}

	private static class DividerViewHolder extends RecyclerView.ViewHolder {

		public DividerViewHolder(View rootView) {
			super(rootView);
		}
	}

	private static class MenuViewHolder extends RecyclerView.ViewHolder {

		private TextView itemTextView;

		public MenuViewHolder(View rootView) {
			super(rootView);
			itemTextView = (TextView) rootView.findViewById(R.id.item);
		}
	}

	public static interface OnItemClickListener {
		void onClick(View view, int position);
	}
}