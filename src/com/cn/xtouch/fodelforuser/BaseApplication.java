package com.cn.xtouch.fodelforuser;

import java.io.File;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application {
	private static Context context;

	public static Context getContext() {
		return context;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		init();
		initImageLoader(context);
	}

	private void init() {
		context = getApplicationContext();
	}

	/**
	 * 初始化UIL配置
	 * 
	 * @param context
	 */
	public void initImageLoader(Context context) {
		File file = new File(getApplicationContext().getExternalCacheDir()
				+ "/logisticforUser", "images");

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.threadPoolSize(5)
				// 线程池内加载的数量
				.threadPriority(3)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(10 * 1024 * 1024))
				.memoryCacheSize(10 * 1024 * 1024)
				.diskCache(new UnlimitedDiscCache(file))
				.diskCacheSize(100 * 1024 * 1024)
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				.diskCacheFileCount(1000)
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.imageDownloader(
						new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout
																				// (5
																				// s),
																				// readTimeout
																				// (30
																				// s)超时时间
				// .writeDebugLogs() // Remove for release app
				.build();// 开始构建

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
}
